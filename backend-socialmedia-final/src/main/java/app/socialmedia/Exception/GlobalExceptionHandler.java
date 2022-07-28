package app.socialmedia.Exception;

import app.socialmedia.model.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>>handleMethodArgsNotValidException(MethodArgumentNotValidException exception){
        Map<String,String> response=new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName=((FieldError)error).getField();
            String message=error.getDefaultMessage();
            response.put(fieldName,message);
        });

        return new ResponseEntity<Map<String,String>>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Response> handleLogoutException(UserNotFoundException e) {
        String exceptionMessage = e.getMessage();
        Response logoutResponse = new Response();
        logoutResponse.setStatus(false);
        logoutResponse.setMessage(exceptionMessage);
        return new ResponseEntity<Response>(logoutResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotLoggedInException.class)
    public ResponseEntity<Response> handleNotLoggedInException(NotLoggedInException e) {
        String exceptionMessage = e.getMessage();
        Response loginResponse = new Response();
        loginResponse.setStatus(false);
        loginResponse.setMessage(exceptionMessage);
        return new ResponseEntity<Response>(loginResponse, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<Response> handlePostNotFoundException(PostNotFoundException e) {
        String exceptionMessage = e.getMessage();
        Response postResponse = new Response();
        postResponse.setStatus(false);
        postResponse.setMessage(exceptionMessage);
        return new ResponseEntity<Response>(postResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ActionCannotBeCompletedException.class)
    public ResponseEntity<Response> handleActionCannotBeCompletedException(ActionCannotBeCompletedException e) {
        String exceptionMessage = e.getMessage();
        Response generalResponse = new Response();
        generalResponse.setStatus(false);
        generalResponse.setMessage(exceptionMessage);
        return new ResponseEntity<Response>(generalResponse, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<Response> handleNotAuthorizedException(NotAuthorizedException e) {
        String exceptionMessage = e.getMessage();
        Response authorizationResponse = new Response();
        authorizationResponse.setStatus(false);
        authorizationResponse.setMessage(exceptionMessage);
        return new ResponseEntity<Response>(authorizationResponse, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(AlreadyLoggedInException.class)
    public ResponseEntity<Response> handleAlreadyLoggedInException(AlreadyLoggedInException e) {
        String exceptionMessage = e.getMessage();
        Response loginResponse = new Response();
        loginResponse.setStatus(false);
        loginResponse.setMessage(exceptionMessage);
        return new ResponseEntity<Response>(loginResponse, HttpStatus.BAD_REQUEST);
    }
}
