package app.socialmedia.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class LoginRequest
{
    @NotBlank(message = "email is required")
    @Email( message = "invalid email, try again ")
    private String email;

    @NotBlank(message = "password is required")
    private String password;
}
