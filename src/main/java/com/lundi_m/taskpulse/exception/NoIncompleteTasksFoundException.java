package com.lundi_m.taskpulse.exception;

public class NoIncompleteTasksFoundException extends RuntimeException {
    public NoIncompleteTasksFoundException(String message) {
        super(message);
    }

    public NoIncompleteTasksFoundException(){
      super("No incomplete tasks found");
    }

}
