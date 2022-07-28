package app.socialmedia.service;


import app.socialmedia.Exception.UserNotFoundException;
import app.socialmedia.model.LoginRequest;
import app.socialmedia.model.Response;
import app.socialmedia.model.User;
import app.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired UserRepository userRepository;
  @Autowired MongoDbSequenceGeneratorService mongoDbSequenceGeneratorService;

  @Value("${pepper}")
  String pepper;

  User loggedInUser;

  public Response signupUser(User user) {
    User newUser = userRepository.findByEmail(user.getEmail());
    Response Response = new Response();
    if (newUser != null) {
      Response.setStatus(false);
      Response.setMessage("User already exist with this email: " + user.getEmail());

    } else {

      String salt = BCrypt.gensalt();
      String hashedPwd = BCrypt.hashpw(user.getPassword(), salt + pepper);

      user.setUserId(mongoDbSequenceGeneratorService.getNextSequence(User.USER_SEQUENCE_NAME));
      user.setSalt(salt);
      user.setPassword(hashedPwd);
      userRepository.save(user);
      Response.setStatus(true);
      Response.setMessage("Signup success for user :" + user.getUserName());
    }
    return Response;
  }

  public Response loginUser(LoginRequest loginRequest) {
    User user = userRepository.findByEmail(loginRequest.getEmail());
    Response loginResponse = new Response();

    if (user == null) {
      loginResponse.setStatus(false);
      loginResponse.setMessage("user does not found with this email: " + loginRequest.getEmail());

    }else {
      String salt = user.getSalt();
      String hashedPwd = BCrypt.hashpw(loginRequest.getPassword(), salt + pepper);
      if (hashedPwd.equals(user.getPassword())) {
        loggedInUser = user;
        loginResponse.setStatus(true);
        loginResponse.setMessage("Login successful for user : " + user.getUserName());
        loginResponse.setPayload(user);

      } else {
        loginResponse.setStatus(false);
        loginResponse.setMessage("invalid password: "+loginRequest.getPassword());
      }
    }
    return loginResponse;
  }
  public Response logoutUser(int userId){
    Response logoutResponse = new Response();
    if(loggedInUser==null){
      String exceptionMessage = "User with userId " + userId + " is not logged in.";
      throw new UserNotFoundException(exceptionMessage);
    }
    else if (loggedInUser.getUserId()==userId) {

      loggedInUser = null;
      logoutResponse.setStatus(true);
      logoutResponse.setMessage("Logout successful");

    } else {
      logoutResponse.setStatus(false);
      logoutResponse.setMessage("You are trying to access someone's else account which is not allowed");
    }
    return logoutResponse;
  }
}
