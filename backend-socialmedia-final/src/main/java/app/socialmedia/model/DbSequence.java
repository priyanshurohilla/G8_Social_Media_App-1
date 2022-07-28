package app.socialmedia.model;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class DbSequence {
    private String  sequenceName;
    private int sequence_number;
}
