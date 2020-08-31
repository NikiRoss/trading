package com.fdm.trading.utils.mail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class NewUserEmail {

    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
