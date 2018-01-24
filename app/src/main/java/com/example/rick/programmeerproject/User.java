package com.example.rick.programmeerproject;
/**
 * Created by Rick on 24-1-2018.
 */
public class User {
    private Integer rating;
    private Integer visit;
    private String comment;

    public User() {
    }

    User(Integer rating, Integer visit, String comment) {
        this.rating = rating;
        this.visit = visit;
        this.comment = comment;
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

