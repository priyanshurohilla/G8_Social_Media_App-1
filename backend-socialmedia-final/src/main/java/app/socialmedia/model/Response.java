package app.socialmedia.model;

import lombok.Data;

@Data
public class Response {

  private boolean status;
  private String message;
  private Object Payload;
}
