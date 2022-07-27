package app.socialmedia.repository;

import app.socialmedia.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User,Integer>

{
   User findByEmail(String emailId);

   User findById(int userId);
}
