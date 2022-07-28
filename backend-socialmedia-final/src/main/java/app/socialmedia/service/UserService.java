package app.socialmedia.service;


import app.socialmedia.Exception.NotLoggedInException;
import app.socialmedia.model.Follower;
import app.socialmedia.model.Response;
import app.socialmedia.model.User;
import app.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthService authService;

    public Response updateUser(int id, User user) {
        if (authService.loggedInUser != null) {

            User updateUser = userRepository.findById(id);
            Response updateResponse = new Response();
            if (updateUser == null) {
                updateResponse.setStatus(false);
                updateResponse.setMessage("user does not found with this id: ");

            } else {

                if (user.getFullName() != null)
                    updateUser.setFullName(user.getFullName());
                if (user.getUserName() != null)
                    updateUser.setUserName(user.getUserName());
                updateUser.setProfilePicUrl(user.getProfilePicUrl());
                if (user.getPhoneNumber() != null)
                    updateUser.setPhoneNumber(user.getPhoneNumber());
                if (user.getPassword() != null) {
                    String salt = BCrypt.gensalt();
                    String hashPassword = BCrypt.hashpw(user.getPassword(), salt);
                    updateUser.setPassword(hashPassword);
                    updateUser.setSalt(salt);
                }

                userRepository.save(updateUser);

                updateResponse.setStatus(true);
                updateResponse.setMessage("Update Successful");

            }
            return updateResponse;
        } else {
            String exceptionMessage = "You are not logged in.";
            throw new NotLoggedInException(exceptionMessage);
        }
    }

    public Response addFollower(int userId, int followerUserId){
        Response response = new Response();

        User user = userRepository.findById(userId);

        User follower = userRepository.findById(followerUserId);

        boolean find = false;
        try{
            if(user != null && follower != null){
                List<Integer> followersIds = user.getFollower();
                List<Integer> followingIds = follower.getFollowing();

                for(int Id : followersIds){
                    if(Id == followerUserId){
                        find = true;
                        break;
                    }
                }
                if(find){
                    response.setStatus(false);
                    response.setMessage("Follower already exists");
                    return response;
                }else{
                    followersIds.add(followerUserId);
                    followingIds.add(userId);
                    user.setFollower(followersIds);
                    follower.setFollowing(followingIds);
                    userRepository.save(user);
                    userRepository.save(follower);
                    response.setStatus(true);
                    response.setMessage("Follower added successfully");
                }
            }else if(user == null){
                response.setStatus(false);
                response.setMessage("User doesn't exist");
            }else{
                response.setStatus(false);
                response.setMessage("Follower doesn't exist");
            }
            return response;
        }catch (Exception e){
            response.setStatus(false);
            response.setMessage("Follower could not be added");
            return response;
        }
    }

    public Response viewFollower(int userId){
        User user = userRepository.findById(userId);

        List<Follower> followerDetails = new ArrayList<>();
        Response response = new Response();
        Follower follower = new Follower();

        if(user != null) {
            List<Integer> followersList = user.getFollower();
            if(followersList.isEmpty()){
                response.setStatus(false);
                response.setMessage("user "+userId+" have no followers");
            } else {
                for (int followerId : followersList) {
                    User follow = userRepository.findById(followerId);
                    follower.setUserId(follow.getUserId());
                    follower.setFullName(follow.getFullName());
                    follower.setProfilePicUrl(follow.getProfilePicUrl());
                    followerDetails.add(follower);
                    response.setStatus(true);
                    response.setMessage("Found followers list");
                    response.setPayload(followerDetails);
                }
            }

        }else{
            response.setStatus(false);
            response.setMessage("User not found");
            response.setPayload(null);
        }

        return response;
    }

    public Response removeFollower(int userId, int followerUserId){
        Response response = new Response();
        User user = userRepository.findById(userId);
        User follower = userRepository.findById(followerUserId);
            List<Integer> followersIds = user.getFollower();
            List<Integer> followingIds = follower.getFollowing();
            if(followersIds.contains(followerUserId) && followingIds.contains(userId)){
                followersIds.remove(followersIds.indexOf(followerUserId));
                followingIds.remove(followingIds.indexOf(userId));
            }
            user.setFollower(followersIds);
            follower.setFollowing(followingIds);
            userRepository.save(user);
            userRepository.save(follower);
            response.setStatus(true);
            response.setMessage("Follower removed");

            return response;

    }

}

