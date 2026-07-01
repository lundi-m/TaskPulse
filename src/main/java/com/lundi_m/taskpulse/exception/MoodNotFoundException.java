package com.lundi_m.taskpulse.exception;

public class MoodNotFoundException extends RuntimeException {
    public MoodNotFoundException() {
        super("No mood logged yet. Log your mood first");
    }

    public MoodNotFoundException(String message){
        super(message);
    }
}
