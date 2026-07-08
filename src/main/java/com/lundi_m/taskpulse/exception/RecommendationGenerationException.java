package com.lundi_m.taskpulse.exception;

public class RecommendationGenerationException extends RuntimeException {
    public RecommendationGenerationException(){
        super("Could not generate recommendation");
    }
    public RecommendationGenerationException(String message) {
        super(message);
    }
}
