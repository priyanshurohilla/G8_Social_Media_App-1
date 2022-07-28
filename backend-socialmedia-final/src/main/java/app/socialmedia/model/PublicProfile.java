package app.socialmedia.model;

import lombok.Data;

@Data
public class PublicProfile {
    String fullName;
    String userName;
    String profilePicUrl;
    int noOfFollowers;
    int noOfFollowing;
}
