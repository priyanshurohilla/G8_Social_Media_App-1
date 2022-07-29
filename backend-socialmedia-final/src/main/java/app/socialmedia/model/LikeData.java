package app.socialmedia.model;

import lombok.Data;

@Data
public class LikeData {
    private int userId;
    private String fullName;
    private String profilePicUrl;
}
