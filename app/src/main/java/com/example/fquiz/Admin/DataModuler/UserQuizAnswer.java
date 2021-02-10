package com.example.fquiz.Admin.DataModuler;

public class UserQuizAnswer {
    String id,userName,userId,mark,timeSpent;

    public UserQuizAnswer(String id, String userName, String userId, String mark, String timeSpent) {
        this.id = id;
        this.userName = userName;
        this.userId = userId;
        this.mark = mark;
        this.timeSpent = timeSpent;
    }


    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getMark() {
        return mark;
    }

    public String getTimeSpent() {
        return timeSpent;
    }
}
