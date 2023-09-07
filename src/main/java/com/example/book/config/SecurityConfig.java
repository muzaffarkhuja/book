package com.example.book.config;

import com.example.book.model.AuthUser;
import com.example.book.repository.UserRepository;
import com.example.book.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private UserRepository userRepository;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        return http
                .cors(cors -> cors.configurationSource(configurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, "/user/**").permitAll().requestMatchers(HttpMethod.GET, "/user/**").authenticated())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/v3/api-docs.yaml", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll())
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "**").permitAll())
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo.userService(oauth2UserService())))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(googleClientRegistration());
    }

    private ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
                .clientId("297180380195-a5e6lvptrhrr8hem87l05oqevffr0rjt.apps.googleusercontent.com")
                .clientSecret("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")
                .redirectUri("http://localhost:9988/login/oauth2/code/google")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .clientName("Google")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .scope("email",
                        "profile",
                        "https://www.googleapis.com/auth/user.gender.read",
                        "https://www.googleapis.com/auth/user.birthday.read",
                        "https://www.googleapis.com/auth/user.addresses.read")
                .build();
    }

    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        return new DefaultOAuth2UserService() {
            @Override
            public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
                OAuth2User user = super.loadUser(userRequest);

                String email = user.getAttribute("email");
                if(userRepository.findByUsername(email).isEmpty()){
                    AuthUser authUser = new AuthUser();
                    authUser.setUsername(email);
                    authUser.setFullName(user.getAttribute("name"));
                    authUser.setGender(user.getAttribute("genders"));
                    authUser.setAddress(user.getAttribute("addresses"));
                    authUser.setBirthDate(user.getAttribute("birthdays"));

                    for (Map.Entry<String, Object> stringObjectEntry : user.getAttributes().entrySet()) {
                        System.out.println(stringObjectEntry);
                    }
                    System.out.println(user);
                    userRepository.save(authUser);
                }

                return user;
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService detailsService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(detailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(provider);
    }

    private CorsConfigurationSource configurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedHeaders(List.of("SECRET-HEADER", "Authorization", "Access-Control-Allow-Origin", "Content-Type"));
        cors.addAllowedMethod("*");
        cors.addAllowedOrigin("null");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

