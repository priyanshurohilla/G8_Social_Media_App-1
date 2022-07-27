package app.socialmedia.controller;

import app.socialmedia.model.Post;
import app.socialmedia.model.Response;
import app.socialmedia.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    @Autowired
    PostService postService;

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
}
