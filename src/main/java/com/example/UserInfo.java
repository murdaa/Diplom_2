package com.example;

public class UserInfo {

    private String email;
    private String name;

    public UserInfo(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public UserInfo() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
