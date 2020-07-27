package com.fdm.trading.domain;

public enum Role {
    ADMIN("ADMIN"), USER("USER");

    private String role;

    private Role(String role){
        this.role = role;
    }

    public String getRole(){
        return name();
    }

}
