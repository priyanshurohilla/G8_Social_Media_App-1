package app.socialmedia.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Document
public class Post {

    @Transient
    public static final  String POST_SEQUENCE_NAME="postSequenceName";
    @Id
    private int postId;

    private int createdByUserId;

    private String content;

    private String imageUrl;

    private Instant timestamp;

    private int numberOfLikes;

    private List<Integer> likedBy = new ArrayList<>();

    private List<String> comments = new ArrayList<>();

}
