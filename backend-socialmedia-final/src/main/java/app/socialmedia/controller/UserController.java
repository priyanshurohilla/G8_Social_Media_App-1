package app.socialmedia.controller;


import app.socialmedia.model.Follower;
import app.socialmedia.model.Response;
import app.socialmedia.model.User;
import app.socialmedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PutMapping(value="/editProfile",produces = "application/json")
    public ResponseEntity<Response> editUser(@RequestParam int userId, @RequestBody User userDetails){
        Response editResponse = userService.updateUser(userId,userDetails);
        if (editResponse.isStatus()) {
            return new ResponseEntity<Response>(editResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<Response>(editResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/addFollower", produces = "application/json")
    public ResponseEntity<Response> addFollowerToList(@RequestParam int userId, @RequestParam int userIdTwo){
        Response response = userService.addFollower(userId, userIdTwo);
        if(response.isStatus()){
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/removeFollower", produces = "application/json")
    public ResponseEntity<Response> removeFollowerFromList(@RequestParam int userId, @RequestParam int userIdTwo){
        Response response = userService.removeFollower(userId, userIdTwo);
        if(response.isStatus()){
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }else{
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/viewFollowers", produces = "application/json")
    public ResponseEntity<Response> viewFollowerDetails(@RequestParam int userId){
            Response response = userService.viewFollower(userId);
            if(response.isStatus()){
                return new ResponseEntity<Response>(response, HttpStatus.OK);
            }else{
                return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
            }
    }

}
