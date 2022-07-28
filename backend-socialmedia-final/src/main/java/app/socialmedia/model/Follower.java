package app.socialmedia.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class Follower {
    @Id
    private int userId;

    @NotBlank(message = "full name is required")
    @Size(min = 3, max = 20, message = "full name must be between 3 and 20 characters long")
    private String fullName;

    private String profilePicUrl;
}
