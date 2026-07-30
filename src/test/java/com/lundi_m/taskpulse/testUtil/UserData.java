package com.lundi_m.taskpulse.testUtil;

import com.lundi_m.taskpulse.model.entity.TaskPulseUser;

import java.time.Instant;

public class UserData {

    public static TaskPulseUser createUser(){
        return TaskPulseUser.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@gmail.com")
                .password("LostAndFoundUnknownEncoded")
                .createdAt(Instant.now())
                .build();
    }
}
