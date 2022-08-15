package com.bibimbap.bibimweb.domain.role;

public enum RoleName {
    MEMBER("MEMBER"),LEADER("LEADER");
    private final String name;

    RoleName(String name) {
        this.name = name;
    }
}
