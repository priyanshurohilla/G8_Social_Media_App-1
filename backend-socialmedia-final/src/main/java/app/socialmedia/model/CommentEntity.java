package app.socialmedia.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document
public class CommentEntity {
    @Id
    private String commentId;
    private int postId;
    private int userId;
    private String fullName;
    private String profileUrl;
    private String content;
    private Instant timeStamp;
}
