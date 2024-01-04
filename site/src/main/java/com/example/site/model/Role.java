package com.example.site.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

public enum Role{

    USER(Set.of(Permission.USER)), TEACHER(Set.of(Permission.USER, Permission.TEACHER)), ADMIN(Set.of(Permission.USER, Permission.TEACHER, Permission.ADMIN));

    final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<SimpleGrantedAuthority> getAuthority(){
        return permissions.stream().map(permission -> new SimpleGrantedAuthority(permission.value)).toList();
    }
}
