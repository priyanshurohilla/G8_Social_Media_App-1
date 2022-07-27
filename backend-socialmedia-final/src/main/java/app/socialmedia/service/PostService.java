package app.socialmedia.service;

import app.socialmedia.model.Post;
import app.socialmedia.model.Response;
import app.socialmedia.model.User;
import app.socialmedia.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    MongoDbSequenceGeneratorService mongoDbSequenceGeneratorService;

    public Response createPost(Post post){
        Response response = new Response();
        try{
            int postId=mongoDbSequenceGeneratorService.getNextSequence(Post.POST_SEQUENCE_NAME);
            post.setPostId(postId);
            postRepository.save(post);
            response.setStatus(true);
            response.setMessage("Created Post : " + post.getPostId());
        }
        catch (Exception e){
            response.setStatus(false);
            response.setMessage("Could not create post");
        }
        return response;
    }

    public Response editPost(Post post){
        Response response = new Response();
        if(post.getContent().equals("") && post.getImageUrl().equals("")){
            response = new Response();
            response.setStatus(false);
            response.setMessage("Empty Post");
            return response;
        }
        try{
            Post postToBeEdited = postRepository.findById(post.getPostId());
            postToBeEdited.setContent(post.getContent());
            postToBeEdited.setImageUrl(post.getImageUrl());
            postRepository.save(postToBeEdited);
            response.setStatus(true);
            response.setMessage("Post edited successfully : " + post.getPostId());
        }catch (Exception e){
            response.setStatus(false);
            response.setMessage("Post could not be edited");
        }
        return response;
    }
}
