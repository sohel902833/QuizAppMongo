package com.example.fquiz.Admin.DataModuler;

public class PublishQuiz {

    String id,quizId,quizName,image,totalQuestion,time,endTime,className;

    public PublishQuiz(String id, String quizId, String quizName, String image, String totalQuestion, String time, String endTime, String className) {
        this.id = id;
        this.quizId = quizId;
        this.quizName = quizName;
        this.image = image;
        this.totalQuestion = totalQuestion;
        this.time = time;
        this.endTime = endTime;
        this.className = className;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(String totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
