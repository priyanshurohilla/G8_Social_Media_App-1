package app.socialmedia.service;


import app.socialmedia.Exception.ActionCannotBeCompletedException;
import app.socialmedia.Exception.NotAuthorizedException;
import app.socialmedia.Exception.NotLoggedInException;
import app.socialmedia.model.*;
import app.socialmedia.Exception.UserNotFoundException;

import app.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Null;
import java.util.Optional;

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
            if(authService.loggedInUser.getUserId() == user.getUserId()){
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
            }else{
                response.setStatus(false);
                response.setMessage("Error");
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

        if(authService.loggedInUser.getUserId() == user.getUserId()) {
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
        try{
            if(authService.loggedInUser.getUserId() == user.getUserId()){
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
            }else{
                response.setStatus(false);
                response.setMessage("Error");
            }
            return response;

        }catch (Exception e){
            response.setStatus(false);
            response.setMessage("Follower could not be removed");
            return response;
        }

    }

    public Response unFollow(int userId, int followingUserId){
        Response response = new Response();
        User user = userRepository.findById(userId);
        User following = userRepository.findById(followingUserId);
        try{
            if(authService.loggedInUser.getUserId() == user.getUserId()){
                List<Integer> followingIds = user.getFollowing();
                List<Integer> followerIds = following.getFollower();
                if(followingIds.contains(followingUserId) && followerIds.contains(userId)){
                    followingIds.remove(followingIds.indexOf(followingUserId));
                    followerIds.remove(followerIds.indexOf(userId));
                }
                user.setFollowing(followingIds);
                following.setFollower(followerIds);
                userRepository.save(user);
                userRepository.save(following);
                response.setStatus(true);
                response.setMessage("Unfollowed successfully");
            }else{
                response.setStatus(false);
                response.setMessage("Error");
            }
            return response;

        }catch (Exception e){
            response.setStatus(false);
            response.setMessage("Could not unfollow");
            return response;
        }

    }


    public Response deleteUser(int userId){
        if(authService.loggedInUser!=null){
            Response deleteUserResponse = new Response();

            User deleteUser = userRepository.findById(userId);

            if(deleteUser==null){
                String msg= "User with userId "+ userId+" does not exist";
                throw new UserNotFoundException(msg);
            }

                if (authService.loggedInUser.getUserId() == userId) {
                    try {
                        userRepository.delete(deleteUser);
                        deleteUserResponse.setStatus(true);
                        deleteUserResponse.setMessage("Deleted Successfully");
                    }
                    catch (Exception e){
                        String msg= "The user cannot be deleted";
                        throw new ActionCannotBeCompletedException(msg);
                    }
                } else {
                    String msg = "You cant delete someone else's profile";
                    throw new NotAuthorizedException(msg);
                }


            return deleteUserResponse;
        }
        else{
            String exceptionMessage = "You are not logged in.";
            throw new NotLoggedInException(exceptionMessage);
        }
    }

    public Response viewPublicProfile(String email){
            Response searchUserResponse= new Response();
            try{
                User user = userRepository.findByEmail(email);
                PublicProfile publicProfile= new PublicProfile();
                publicProfile.setFullName(user.getFullName());
                publicProfile.setUserName(user.getUserName());
                publicProfile.setProfilePicUrl(user.getProfilePicUrl());
                publicProfile.setNoOfFollowers(user.getFollower().size());
                publicProfile.setNoOfFollowing(user.getFollowing().size());
                searchUserResponse.setStatus(true);
                searchUserResponse.setMessage("Found successfully");
                searchUserResponse.setPayload(publicProfile);
            }
            catch(NullPointerException e){
                String msg= "User with emailId "+email+" does not exist";
                throw new UserNotFoundException(msg);
            }
            catch(Exception e){
                String msg = "Action cannot be completed";

                throw new ActionCannotBeCompletedException(msg);
            }
            return searchUserResponse;
        }

    public ViewProfile getProfile(String email){
        if (authService.loggedInUser != null) {
            User viewUser = userRepository.findByEmail(email);
            ViewProfile viewProfile = new ViewProfile();
            if(viewUser == null){
                throw new UserNotFoundException("could not found the emailId");
            }else{
                if(authService.loggedInUser.getEmail().equals(email)){
                    viewProfile.setFullName(viewUser.getFullName());
                    viewProfile.setUserName(viewUser.getUserName());
                    viewProfile.setEmail(viewUser.getEmail());
                    viewProfile.setPhoneNumber(viewUser.getPhoneNumber());
                    viewProfile.setFollower(viewUser.getFollower().size());
                    viewProfile.setFollowing(viewUser.getFollowing().size());
                }
                else{
                    throw new NotLoggedInException("You are not that person logged in");
                }
            }
            return viewProfile;
        }
        else{
            String exceptionMessage = "You are not logged in.";
            throw new NotLoggedInException(exceptionMessage);
            }
        }
    }


