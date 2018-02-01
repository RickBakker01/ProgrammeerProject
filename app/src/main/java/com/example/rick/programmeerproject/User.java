package com.example.rick.programmeerproject;
/**
 * Created by Rick on 24-1-2018.
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

