package com.example.book.service.fileService;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@EnableScheduling
@Component
public class FileSyncJob {

    public static final String localDirectory = "D:\\codes\\Java\\book\\temp";
    @Value("${application.bucket.name}")
    private String bucketName;
    @Autowired
    private AmazonS3 s3Client;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.DAYS)
    public void syncFiles() {
        syncDirectory(new File(localDirectory), "");
    }

    private void syncDirectory(File dir, String s3Prefix) {
        ListObjectsV2Request listRequest = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(s3Prefix);

        ListObjectsV2Result listResult = s3Client.listObjectsV2(listRequest);
        List<S3ObjectSummary> s3Objects = listResult.getObjectSummaries();

        for (File localFile : Objects.requireNonNull(dir.listFiles())) {

            if (localFile.isDirectory()) {
                createS3Directory(s3Prefix + localFile.getName());
                String s3SubdirectoryPrefix = s3Prefix + localFile.getName() + "/";
                syncDirectory(localFile, s3SubdirectoryPrefix);
            } else {
                String s3Key = s3Prefix + localFile.getName();
                S3ObjectSummary matchingS3Object = findMatchingS3Object(s3Objects, s3Key);

                if (matchingS3Object == null || localFile.lastModified() > matchingS3Object.getLastModified().getTime()) {
                    PutObjectRequest putRequest = new PutObjectRequest(bucketName, s3Key, localFile);
                    s3Client.putObject(putRequest);
                }
            }
        }

        for (S3ObjectSummary s3Object : s3Objects) {
            String s3Key = s3Object.getKey();
            String localPath = dir.getAbsolutePath() + File.separator + s3Key.substring(s3Prefix.length());

            if (!new File(localPath).exists()) {
                s3Client.deleteObject(bucketName, s3Key);
            }
        }
    }

    private void createS3Directory(String s3Key) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);
        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3Key + "/", emptyContent, metadata);
        s3Client.putObject(putObjectRequest);
    }

    private S3ObjectSummary findMatchingS3Object(List<S3ObjectSummary> s3Objects, String s3Key) {
        for (S3ObjectSummary s3Object : s3Objects) {
            if (s3Object.getKey().equals(s3Key)) {
                return s3Object;
            }
        }
        return null;
    }
}
