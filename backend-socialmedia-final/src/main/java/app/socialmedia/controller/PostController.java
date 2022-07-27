package app.socialmedia.controller;

import app.socialmedia.model.Post;
import app.socialmedia.model.Response;
import app.socialmedia.service.AuthService;
import app.socialmedia.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    AuthService authService;

    @PostMapping(value = "/createpost", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> create(@RequestBody Post post){

        Response response;

        if(post.getContent().equals("") && post.getImageUrl().equals("")){
            response = new Response();
            response.setStatus(false);
            response.setMessage("Empty Post");
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }

        response = postService.createPost(post);

        if(response.isStatus()){
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping(value = "/editpost",consumes = "application/json", produces = "application/json")
    public  ResponseEntity<Response> edit(@RequestBody Post post) {

        Response response = postService.editPost(post);

        if (response.isStatus()) {
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value="/deletepost/{postid}", produces = "application/json")
    public ResponseEntity<Response> deletePost(@PathVariable(name = "postid") int postId){
        return new ResponseEntity<Response>(postService.deletePost(postId), HttpStatus.OK);
    }
}
