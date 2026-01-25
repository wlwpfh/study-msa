package org.example.userservice.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestLogin {
    @NotNull(message = "email cannot be null")
    @Size(min = 2, message= "email not be less than two characters")
    @Email
    private String email;

    @NotNull(message =  "password cannot be null")
    @Size(min = 8, message = "password must be equals or greater than 8 characters")
    private String password;


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
