package app.socialmedia.service;


import app.socialmedia.model.Response;
import app.socialmedia.model.User;
import app.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public Response updateUser(int id, User user){
        User updateUser = userRepository.findById(id);
        Response updateResponse = new Response();
        if (updateUser == null) {
            updateResponse.setStatus(false);
            updateResponse.setMessage("user does not found with this id: ");

        }else{

            if(user.getFullName()!=null)
                updateUser.setFullName(user.getFullName());
            if(user.getUserName()!=null)
                updateUser.setUserName(user.getUserName());
            updateUser.setProfilePicUrl(user.getProfilePicUrl());
            if(user.getPhoneNumber()!=null)
                updateUser.setPhoneNumber(user.getPhoneNumber());
            if(user.getPassword()!=null)
            {
                String salt = BCrypt.gensalt();
                String hashPassword = BCrypt.hashpw(user.getPassword() ,salt);
                updateUser.setPassword(hashPassword);
                updateUser.setSalt(salt);
            }

            userRepository.save(updateUser);

            updateResponse.setStatus(true);
            updateResponse.setMessage("Update Successful");

        }
        return updateResponse;
    }
}

