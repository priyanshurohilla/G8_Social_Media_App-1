package app.socialmedia.service;

import app.socialmedia.Exception.ActionCannotBeCompletedException;
import app.socialmedia.Exception.NotLoggedInException;
import app.socialmedia.Exception.PostNotFoundException;
import app.socialmedia.model.*;
import app.socialmedia.repository.CommentRepository;
import app.socialmedia.repository.PostRepository;
import app.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {

  @Autowired PostRepository postRepository;
  @Autowired AuthService authService;

  @Autowired CommentRepository commentRepository;

  @Autowired UserRepository userRepository;
  @Autowired MongoDbSequenceGeneratorService mongoDbSequenceGeneratorService;

  public Response createPost(Post post) {
    Response response = new Response();

    if (authService.loggedInUser != null) {

      try {
        int postId = mongoDbSequenceGeneratorService.getNextSequence(Post.POST_SEQUENCE_NAME);
        post.setPostId(postId);
        post.setTimestamp(Instant.now());
        postRepository.save(post);
        response.setStatus(true);
        response.setPayload(post);
        response.setMessage("Created Post :" + post.getPostId());
      } catch (Exception e) {
        response.setStatus(false);
        response.setMessage("Could not create post");
      }
      return response;
    } else {
      String exceptionMessage = "You are not logged in.";
      throw new NotLoggedInException(exceptionMessage);
    }
  }

  public Response deletePost(int postId) {
    Response deletePostResponse = new Response();
    if (authService.loggedInUser != null) {
      try {
        Post post = postRepository.findById(postId);
        postRepository.delete(post);
        deletePostResponse.setStatus(true);
        deletePostResponse.setMessage("Deleted successfully");

      } catch (Exception e) {
        String message = "Post with postId " + postId + " not Found";
        throw new PostNotFoundException(message);
      }

    } else {
      String exceptionMessage = "You are not logged in.";
      throw new NotLoggedInException(exceptionMessage);
    }
    return deletePostResponse;
  }

  public Response editPost(Post post) {
    Response response = new Response();
    if (authService.loggedInUser != null) {

      if (post.getContent().equals("") && post.getImageUrl().equals("")) {
        response = new Response();
        response.setStatus(false);
        response.setMessage("Empty Post");
        return response;
      }
      try {
        Post postToBeEdited = postRepository.findById(post.getPostId());
        postToBeEdited.setContent(post.getContent());
        postToBeEdited.setImageUrl(post.getImageUrl());
        postRepository.save(postToBeEdited);
        response.setStatus(true);
        response.setMessage("Post edited successfully : " + post.getPostId());
      } catch (Exception e) {
        response.setStatus(false);
        response.setMessage("Post could not be edited");
      }
      return response;
    } else {
      String exceptionMessage = "You are not logged in.";
      throw new NotLoggedInException(exceptionMessage);
    }
  }

  public Response addComment(CommentRequest commentRequest) {
    Response response = new Response();

    if (authService.loggedInUser != null) {

      try {

        User user = userRepository.findById(commentRequest.getUserId());
        Post post = postRepository.findById(commentRequest.getPostId());

        if (commentRequest.getContent().equals("")) {
          response = new Response();
          response.setStatus(false);
          response.setMessage("content should not be blank!");
          return response;
        } else {
          CommentEntity newComment = new CommentEntity();
          String commentId = UUID.randomUUID().toString();
          newComment.setCommentId(commentId);
          newComment.setPostId(post.getPostId());
          newComment.setUserId(commentRequest.getUserId());
          newComment.setFullName(user.getFullName());
          newComment.setProfileUrl(user.getProfilePicUrl());
          newComment.setContent(commentRequest.getContent());
          newComment.setTimeStamp(Instant.now());

          List<String> commentIdList = post.getComments();
          commentIdList.add(commentId);
          post.setComments(commentIdList);

          commentRepository.save(newComment);
          postRepository.save(post);

          response.setStatus(true);
          response.setMessage("Created comment and comment Id:" + newComment.getCommentId());
          response.setPayload(newComment);
        }
      } catch (Exception e) {
        response.setStatus(false);
        response.setMessage("Could not create comment, invalid user id or post Id");
      }
      return response;
    } else {
      String exceptionMessage = "You are not logged in.";
      throw new NotLoggedInException(exceptionMessage);
    }
  }

  public Response deleteComment(String commentId) {
    Response response = new Response();
    if (authService.loggedInUser != null) {
      try {
        CommentEntity commentEntity = commentRepository.findByCommentId(commentId);
        Post post = postRepository.findById(commentEntity.getPostId());
        List<String> commentList = post.getComments();
        commentList.remove(commentId);
        post.setComments(commentList);
        postRepository.save(post);
        commentRepository.delete(commentEntity);
        response.setStatus(true);
        response.setMessage(" Comment successfully deleted");
      } catch (Exception e) {
        String message = "comment not found with commentId " + commentId;
        throw new PostNotFoundException(message);
      }

    } else {
      String exceptionMessage = "You are not logged in.";
      throw new NotLoggedInException(exceptionMessage);
    }
    return response;
  }

  public Response ViewAllComment(int postId)
  {   Response response = new Response();

      List<CommentViewData>allCommenter= new ArrayList<CommentViewData>();
    if (authService.loggedInUser != null) {
      try {
          Post post =postRepository.findById(postId);
          List<String>commentList=post.getComments();
          for (String commentId :commentList ) {
               CommentEntity commentEntity=commentRepository.findByCommentId(commentId);
               CommentViewData commentViewData=new CommentViewData();
               commentViewData.setContent(commentEntity.getContent());
               commentViewData.setTimeStamp(commentEntity.getTimeStamp());
               commentViewData.setFullName(commentEntity.getFullName());
               allCommenter.add(commentViewData);
          }
        response.setStatus(true);
        response.setMessage("fetched all commenter successfully!");
        response.setPayload(allCommenter);
      } catch (Exception e) {
        String message = "post with postId "+postId+" does not found";
        throw new PostNotFoundException(message);
      }

    } else {
      String exceptionMessage = "You are not logged in.";
      throw new NotLoggedInException(exceptionMessage);
    }
    return  response;
  }





}
