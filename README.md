# TaskPulse

**TaskPulse** is an intelligent task management and recommendation system that helps users to decide _what to work on 
next_ based on their current mood, energy level, focus, and available time.

Unlike traditional to-do applications that only store and display tasks, TaskPulse actively ranks and recommends tasks 
using a rule-based scoring engine that adapts to the user's current mental state.

##  Core Idea

Most productivity tools assume that users already know what they should work on. In reality, people often struggle with 
decision fatigue when choosing tasks misalignment between energy levels and task difficulty procrastination caused by 
overwhelming task lists inefficient use of short time windows

TaskPulse is designed to remove that friction by turning a static task list into a dynamic, adaptive system that
continuously answers:
    **“Given how I feel right now, what should I do next?”**

## Final Product Overview

When completed, TaskPulse will be a full backend system that supports:

1. User Accounts

    Users will be able to:
   * register and log in securely
   * maintain a personal task space
   * track their productivity history
   
2. Task Management System

    Users can:
    - create tasks with descriptions, priorities, and deadlines
    - assign estimated completion time
    - update or remove tasks as needed

## **Example Flow**

1. User logs in
2. User submits current pulse(mood):
   - Mood: tired
   - Energy: low
   - Time available: 20 minutes
3. System evaluates all pending tasks
4. System returns:
   - “Reply to emails” (high score)
   - “Fix small bug” (medium score)
   - “Try yoga” (low score)

## **Planned Features**

* User authentication and account management
* Full CRUD task management
* Mood / energy “pulse” input system
* Task recommendation engine with scoring logic
* Priority-based task ranking
* RESTful API design

## Tech Stack

* Java
* Spring Boot
* PostgreSQL
* Spring Security (JWT)
* Docker
* JUnit / Mockito

## Status

This project is currently in early development and will be built iteratively, feature by feature, starting from
authentication and task management, then evolving into the recommendation engine core.