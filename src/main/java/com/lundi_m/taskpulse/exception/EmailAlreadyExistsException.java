package com.lundi_m.taskpulse.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email){
      super("Email already registered: " + email);
    }
}
