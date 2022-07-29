package app.socialmedia.model;


import lombok.Data;

@Data
public class LikeRequest {
    private int postId;
    private int userId;
}
