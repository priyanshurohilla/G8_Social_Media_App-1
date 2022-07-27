package app.socialmedia.model;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class UserDbSequence {
    private String  sequenceName;
    private int sequence_number;
}
