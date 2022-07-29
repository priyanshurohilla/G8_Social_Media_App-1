package app.socialmedia.controller;

import app.socialmedia.model.*;
import app.socialmedia.service.AuthService;
import app.socialmedia.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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


    @PostMapping(value = "/getposts/{userId}")
    public ResponseEntity<Response> getPosts(@PathVariable(value = "userId") Integer userId){
        Response response = postService.fetchPosts(userId);
        if(response.isStatus()){
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PostMapping(value = "/likeupdate", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> like(@RequestBody LikeRequest likeRequest){
        Response response = postService.likePost(likeRequest);

        if(response.isStatus()){
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/viewlikes/{postId}")
    public ResponseEntity<Response> viewLikes(@PathVariable(value = "postId") Integer postId){
        Response response = postService.viewLikes(postId);
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

    @DeleteMapping(value="post/delete/{postid}", produces = "application/json")
    public ResponseEntity<Response> deletePost(@PathVariable(name = "postid") int postId) {
        return new ResponseEntity<Response>(postService.deletePost(postId), HttpStatus.OK);

    }

    @PostMapping(value = "/comment-add",consumes = "application/json", produces = "application/json")
    public  ResponseEntity<Response> addComment( @RequestBody CommentRequest commentRequest) {
          Response response=postService.addComment(commentRequest);
        if (response.isStatus()) {
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value="/comment-delete/{commentId}", produces = "application/json")
    public ResponseEntity<Response> deleteComment(@PathVariable(name = "commentId") String commentId){
        return new ResponseEntity<Response>(postService.deleteComment(commentId), HttpStatus.OK);
    }

    @GetMapping(value="/get-comments/{postId}", produces = "application/json")
    public ResponseEntity<Response> getViewComment(@PathVariable(name = "postId") int postId){
        return new ResponseEntity<Response>(postService.ViewAllComment(postId), HttpStatus.OK);
    }

    @GetMapping(value="/view-my-posts/{userId}", produces = "application/json")
    public ResponseEntity<Response> viewMyPosts(@PathVariable(name = "userId") int userId){
        return new ResponseEntity<Response>(postService.viewMyPosts(userId), HttpStatus.OK);
    }
}
