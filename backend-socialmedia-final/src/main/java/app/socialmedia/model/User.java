package app.socialmedia.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Data
@Document
public class User {

  @Id
  private int userId;

  @NotBlank(message = "full name is required")
  @Size(min = 3, max = 20, message = "full name must be between 3 and 20 characters long")
  private String fullName;

  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 15, message = "Username must be between 3 and 15 characters long")
  private String userName;

  @NotBlank(message = "email is required")
  @Email( message = "invalid email, try again ")
  private String email;

  private String profilePicUrl;

  @Size(min = 0, max = 10)
  private String phoneNumber;

  @NotBlank(message = "password is required")
  @Size(min = 6, max = 20, message = "password must be between 6 and 20 characters long")
  private String password;

  private String salt;

 private List<String> following = new ArrayList<>();

  private List<String> follower = new ArrayList<>();

}
