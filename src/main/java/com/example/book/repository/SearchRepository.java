package com.example.book.repository;

import com.example.book.dto.searchs.MostSearchedDTO;
import com.example.book.model.Searches;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends MongoRepository<Searches, String>, BaseRepository {

//    @Query(value = "SELECT new com.example.book.dto.searchs.MostSearchedDTO(s.keywords, COUNT(s)) FROM Searchs s GROUP BY s.keywords ORDER BY COUNT(s) DESC LIMIT 1")
//    MostSearchedDTO findMostSearched();

    @Aggregation(pipeline = {
            "{ $group:  {_id:  '$keywords', count: {$sum: 1}}}",
            "{ $sort:  {count:  -1}}",
            "{ $limit:  1 }",
            "{ $project:  { _id: 0, keywords: '$_id', count: 1}}"
    })
    MostSearchedDTO findMostSearched();
}
