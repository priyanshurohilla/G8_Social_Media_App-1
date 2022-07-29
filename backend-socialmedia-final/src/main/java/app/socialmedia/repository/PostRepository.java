package app.socialmedia.repository;

import app.socialmedia.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, Integer> {

    Post findById(int postId);
    List<Post> findByCreatedByUserId(int userId);


}
