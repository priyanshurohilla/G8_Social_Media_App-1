package app.socialmedia.repository;

import app.socialmedia.model.CommentEntity;
import app.socialmedia.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<CommentEntity,Integer> {
    CommentEntity findByCommentId(String commentId);
}
