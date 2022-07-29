package app.socialmedia.service;

import app.socialmedia.model.*;
import app.socialmedia.Exception.ActionCannotBeCompletedException;
import app.socialmedia.Exception.NotAuthorizedException;
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

    @Autowired
    PostRepository postRepository;
    @Autowired
    AuthService authService;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    MongoDbSequenceGeneratorService mongoDbSequenceGeneratorService;

    public Response createPost(Post post) {
        Response response = new Response();

        if (authService.loggedInUser != null) {
            if (authService.loggedInUser.getUserId() == post.getCreatedByUserId()) {

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
                String exceptionMessage = "You are not authorized.";
                throw new NotAuthorizedException(exceptionMessage);
            }
        } else {
            String exceptionMessage = "You are not logged in.";
            throw new NotLoggedInException(exceptionMessage);
        }
    }
    public Response deletePost(int postId){
        Response deletePostResponse = new Response();
        if(authService.loggedInUser!=null){

                    Post post = postRepository.findById(postId);
                    if(post==null){
                        String message = "Post with postId "+postId+" not Found";
                        throw new PostNotFoundException(message);
                    }


                    if(authService.loggedInUser.getUserId()==post.getCreatedByUserId()) {
                        try {
                            postRepository.delete(post);
                            deletePostResponse.setStatus(true);
                            deletePostResponse.setMessage("Deleted successfully");
                        } catch (Exception e) {
                            String msg = "The post cannot be deleted";
                            throw new ActionCannotBeCompletedException(msg);
                        }
                    }
                    else{
                        String msg = "You cant delete someone else's post";
                        throw new NotAuthorizedException(msg);
                    }
            }
        else{
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
public Response viewMyPosts(int userId){
      Response viewMyPostsResponse = new Response();
      if(authService.loggedInUser!=null){
          List<Post> posts = postRepository.findByCreatedByUserId(userId);
          if(posts.size()==0){
              String msg = "No posts found";
              throw new PostNotFoundException(msg);
          }
          ViewMyPostsEntity viewMyPostsEntity = new ViewMyPostsEntity();
          List<ViewMyPostsEntity> viewMyPosts = new ArrayList<>();
          if(authService.loggedInUser.getUserId()==userId){
            try {
                for (Post post : posts) {
                    viewMyPostsEntity.setPostId(post.getPostId());
                    viewMyPostsEntity.setContent(post.getContent());
                    viewMyPostsEntity.setNumberOfComments(post.getComments().size());
                    viewMyPostsEntity.setNumberOfLikes(post.getLikedBy().size());
                    viewMyPostsEntity.setUserName(authService.loggedInUser.getUserName());
                    viewMyPostsEntity.setFullName(authService.loggedInUser.getFullName());
                    viewMyPostsEntity.setTimestamp(post.getTimestamp());
                    viewMyPostsEntity.setImageUrl(post.getImageUrl());
                    viewMyPosts.add(viewMyPostsEntity);
                }
                viewMyPostsResponse.setStatus(true);
                viewMyPostsResponse.setMessage("Successfully fetched my posts");
                viewMyPostsResponse.setPayload(viewMyPosts);
            }
            catch(Exception e){
                String msg = "My posts cannot be fetched";
                throw new ActionCannotBeCompletedException(msg);
            }
          }
          else{
              String msg = "You cant view someone else's post";
              throw new NotAuthorizedException(msg);
          }
          return viewMyPostsResponse;
      }
      else{
          String exceptionMessage = "You are not logged in.";
          throw new NotLoggedInException(exceptionMessage);
      }
}


    public Response editPost(Post post) {
        Response response = new Response();
        if(authService.loggedInUser != null){
        if (authService.loggedInUser.getUserId() == post.getCreatedByUserId()) {

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
            String exceptionMessage = "You are not authorized.";
            throw new NotAuthorizedException(exceptionMessage);
        }
    } else {
            String message = "You are not authorized";
            throw new NotAuthorizedException(message);
        }
    }

    public Response addComment(CommentRequest commentRequest) {
        Response response = new Response();

        if(authService.loggedInUser != null) {
            if (authService.loggedInUser.getUserId() == commentRequest.getUserId()) {

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
            }
        else {
            String message = "You are not authorized";
            throw new NotAuthorizedException(message);
        }
        } else {
            String exceptionMessage = "You are not logged in.";
            throw new NotLoggedInException(exceptionMessage);
        }
    }


    public Response fetchPosts(int userId) {
        if(authService.loggedInUser != null) {
            if (authService.loggedInUser.getUserId() == userId) {
                User user = userRepository.findByUserId(userId);

                Response response = new Response();
                if (user == null) {
                    response.setStatus(false);
                    response.setMessage("User does not exist");
                } else {
                    List<Integer> following = new ArrayList<>();
                    following = user.getFollowing();

                    if (following.isEmpty()) {
                        response.setStatus(false);
                        response.setMessage("user " + userId + " follows no one");
                    } else {
                        List<Integer> postIds = new ArrayList<>();
                        for (int followerId : following) {

                            User follower = userRepository.findByUserId(followerId);
                            if (follower == null) {
                                List<Post> posts = postRepository.findByCreatedByUserId(followerId);
                                for (Post p : posts) {
                                    postRepository.delete(p);
                                }
                            }

                            List<Post> posts = postRepository.findByCreatedByUserId(followerId);

                            if (posts != null) {
                                for (Post p : posts) {
                                    postIds.add(p.getPostId());
                                }
                            } else {
                                continue;
                            }
                        }
                        if (postIds.isEmpty()) {
                            response.setStatus(false);
                            response.setMessage("No posts to show");
                        } else {
                            response.setStatus(true);
                            response.setMessage("Posts fetched successfully");
                            response.setPayload(getPosts(postIds));
                        }
                    }
                }
                return response;
            } else {
                String exceptionMessage = "You are not authorized.";
                throw new NotAuthorizedException(exceptionMessage);
            }
        } else {
            String exceptionMessage = "You are not authorized.";
            throw new NotLoggedInException(exceptionMessage);
        }
    }

    public List<Post> getPosts(List<Integer> postIds) {
        List<Post> responsePosts = new ArrayList<>();
        for (int postId : postIds) {
            Post post = postRepository.findByPostId(postId);
            if (post != null) {
                responsePosts.add(post);
            }
        }
        return responsePosts;
    }


    public Response likePost(LikeRequest likeRequest) {

        if(authService.loggedInUser != null) {
            if (authService.loggedInUser.getUserId() == likeRequest.getUserId()) {
                Response response = new Response();
                Post post = postRepository.findByPostId(likeRequest.getPostId());
                response = new Response();
                if (post.getLikedBy().contains(likeRequest.getUserId())) {
                    removeLike(post, likeRequest.getUserId());

                    response.setStatus(true);
                    response.setMessage("Unliked Successfully");
                } else {
                    post.setNumberOfLikes(post.getNumberOfLikes() + 1);
                    List<Integer> likedBy = post.getLikedBy();
                    likedBy.add(likeRequest.getUserId());
                    post.setLikedBy(likedBy);
                    postRepository.save(post);
                    response.setStatus(true);
                    response.setMessage("Liked Successfully");
                }
                return response;
            } else {
                String exceptionMessage = "You are not Authorized.";
                throw new NotAuthorizedException(exceptionMessage);
            }
        } else {
            String exceptionMessage = "You are not logged in.";
            throw new NotLoggedInException(exceptionMessage);
        }
    }


    public Response viewLikes(int postId) {

        if(authService.loggedInUser != null){
            Response response = new Response();
            response = new Response();
            Post post = postRepository.findByPostId(postId);
            if (post == null) {
                response.setStatus(false);
                response.setMessage("Post does not exist!!");
            } else if (post.getNumberOfLikes() < 1) {
                response.setStatus(false);
                response.setMessage("No one has liked the post yet");
            } else {
                List<Integer> likedBy = post.getLikedBy();
                List<LikeData> likers = new ArrayList<>();

                for (int userId : likedBy) {
                    User user = userRepository.findByUserId(userId);
                    if (user == null) {
                        removeUserIdFromLikeList(post, userId);
                        response.setStatus(false);
                        response.setMessage("User with userId: " + userId + " does not exist");
                    } else {
                        LikeData likeData = new LikeData();
                        likeData.setUserId(user.getUserId());
                        likeData.setFullName(user.getFullName());
                        likeData.setProfilePicUrl(user.getProfilePicUrl());
                        likers.add(likeData);

                        response.setStatus(true);
                        response.setMessage("view list successful");
                        response.setPayload(likers);
                    }
                }

            }
            return response;
        } else {
            String exceptionMessage = "You are not logged in.";
            throw new NotLoggedInException(exceptionMessage);
        }
    }

    public void removeLike(Post post, int userId) {
        post.setNumberOfLikes(post.getNumberOfLikes() - 1);
        List<Integer> likedBy = post.getLikedBy();
        likedBy.remove(likedBy.indexOf(userId));
        post.setLikedBy(likedBy);
        postRepository.save(post);
    }

    public void removeUserIdFromLikeList(Post post, int userId) {
        //Post post = postRepository.findByPostId(postId);
        removeLike(post, userId);
    }


    public Response deleteComment(String commentId) {
        Response deletePostResponse = new Response();
        if (authService.loggedInUser != null) {
            try {
                CommentEntity commentEntity = commentRepository.findByCommentId(commentId);
                if(authService.loggedInUser.getUserId() == commentEntity.getUserId()){
                    Post post = postRepository.findById(commentEntity.getPostId());
                    List<String> commentList = post.getComments();
                    commentList.remove(commentId);
                    post.setComments(commentList);
                    postRepository.save(post);
                    commentRepository.delete(commentEntity);
                    deletePostResponse.setStatus(true);
                    deletePostResponse.setMessage(" Comment successfully deleted");
                } else {
                    //String message = "User not authorised to perform this action";
                    //throw new NotAuthorisedException(message);
                }
            } catch (Exception e) {
                String message = "comment not found with commentId " + commentId;
                throw new PostNotFoundException(message);
            }

        } else {
            String exceptionMessage = "You are not logged in.";
            throw new NotLoggedInException(exceptionMessage);
        }
        return deletePostResponse;
    }

}

