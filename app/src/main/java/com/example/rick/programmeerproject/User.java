package com.example.rick.programmeerproject;
/**
 * Created by Rick on 24-1-2018.
 */
public class User {
    private String id;
    private Integer rating;
    private Integer visit;
    private String comment;

    public User() {
    }

    User(String id, Integer rating, Integer visit, String comment) {
        this.id = id;
        this.rating = rating;
        this.visit = visit;
        this.comment = comment;
    }
    String getID(){
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

