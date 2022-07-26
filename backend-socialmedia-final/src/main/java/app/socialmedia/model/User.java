package app.socialmedia.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int userId;

  @NotBlank
  @Size(min = 3, max = 20)
  private String fullName;

  @NotBlank
  @Size(min = 3, max = 15)
  private String userName;

  @NotBlank
  @Size(max = 40)
  @Email
  private String email;

  private String profilePicUrl;

  @Size(min = 0, max = 10)
  private String phoneNumber;

  @NotBlank
  @Size(min = 6, max = 20)
  private String password;

  List<String> following = new ArrayList<>();

  List<String> follower = new ArrayList<>();

}
