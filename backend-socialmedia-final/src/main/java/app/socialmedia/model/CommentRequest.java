package app.socialmedia.model;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CommentRequest {

    private int userId;

    private int postId;

    private String content;
}
