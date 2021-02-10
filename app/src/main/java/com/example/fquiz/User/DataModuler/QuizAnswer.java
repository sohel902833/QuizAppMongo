package com.example.fquiz.User.DataModuler;

import java.io.Serializable;

public class QuizAnswer implements Serializable {

    int position;

    String question,option1,option2,option3,option4;
    int answerNumber,providedAnswerNumber;


    public QuizAnswer(int position, String question, String option1, String option2, String option3, String option4, int answerNumber, int providedAnswerNumber) {
        this.position=position;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answerNumber = answerNumber;
        this.providedAnswerNumber = providedAnswerNumber;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public int getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(int answerNumber) {
        this.answerNumber = answerNumber;
    }

    public int getProvidedAnswerNumber() {
        return providedAnswerNumber;
    }

    public void setProvidedAnswerNumber(int providedAnswerNumber) {
        this.providedAnswerNumber = providedAnswerNumber;
    }
}
