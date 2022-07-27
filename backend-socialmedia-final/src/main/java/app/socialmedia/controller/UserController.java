package app.socialmedia.controller;


import app.socialmedia.model.Response;
import app.socialmedia.model.User;
import app.socialmedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
