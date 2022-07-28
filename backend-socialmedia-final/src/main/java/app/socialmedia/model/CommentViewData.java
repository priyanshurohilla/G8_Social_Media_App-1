package app.socialmedia.model;

import lombok.Data;

import java.time.Instant;

@Data
public class CommentViewData {

    private String fullName;
    private String content;
    private Instant timeStamp;

}
