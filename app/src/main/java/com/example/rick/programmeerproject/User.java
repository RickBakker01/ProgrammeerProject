package com.example.rick.programmeerproject;
/**
 * This is a singleton for getting info from database
 */
@SuppressWarnings("WeakerAccess")
public class User {
    public String id;
    public Integer rating;
    public Integer visit;
    public String comment;

    public User() {
    }

    User(String id, Integer rating, Integer visit, String comment) {
        this.id = id;
        this.rating = rating;
        this.visit = visit;
        this.comment = comment;
    }

    String getID() {
        return id;
    }

    Integer getRating() {
        return rating;
    }

    Integer getVisit() {
        return visit;
    }

    String getComment() {
        return comment;
    }
}

