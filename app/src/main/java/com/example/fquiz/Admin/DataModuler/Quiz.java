package com.example.fquiz.Admin.DataModuler;

public class Quiz {
    String id,quizName,image,totalQuestions;


    public Quiz(String id, String quizName, String image, String totalQuestions) {
        this.id = id;
        this.quizName = quizName;
        this.image = image;
        this.totalQuestions = totalQuestions;
    }

    public String getId() {
        return id;
    }

    public String getQuizName() {
        return quizName;
    }

    public String getImage() {
        return image;
    }

    public String getTotalQuestions() {
        return totalQuestions;
    }
}
