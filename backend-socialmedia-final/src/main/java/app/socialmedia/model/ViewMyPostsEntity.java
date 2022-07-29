package app.socialmedia.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@Data
public class ViewMyPostsEntity {
    private int postId;
    private String fullName;
    private String userName;

    private String content;

    private String imageUrl;

    private Instant timestamp;

    private int numberOfLikes;
    private int numberOfComments;

}
