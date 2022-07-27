package app.socialmedia.service;

import app.socialmedia.model.Post;
import app.socialmedia.model.Response;
import app.socialmedia.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;
    public Response createPost(Post post){
        Response response = new Response();
        try{
            postRepository.save(post);
            response.setStatus(true);
            response.setMessage("Created Post :" + post.getPostId());
        }
        catch (Exception e){
            response.setStatus(false);
            response.setMessage("Could not create post");
        }
        return response;
    }
}
