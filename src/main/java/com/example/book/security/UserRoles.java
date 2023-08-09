package com.example.book.security;

import java.util.ArrayList;
import java.util.List;

import static com.example.book.security.UserAuthorities.*;

public enum UserRoles {
    ADMIN(List.of(READ, UPDATE, CREATE)),
    USER(List.of(READ)),
    SUPER_ADMIN(List.of(CREATE, READ, UPDATE, DELETE));

    UserRoles(List<UserAuthorities> authorities) {
        this.authorities = authorities;
    }

    List<UserAuthorities> authorities;

    public List<String> getAuthorities() {
        List<String> list = new ArrayList<>(this.authorities.stream()
                .map(UserAuthorities::getName)
                .toList());
        list.add("ROLE_" + this.name());

        return list;
    }
}
