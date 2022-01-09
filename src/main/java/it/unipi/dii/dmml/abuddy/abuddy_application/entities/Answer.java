package it.unipi.dii.dmml.abuddy.abuddy_application.entities;

import java.util.ArrayList;

public class Answer {

    private String username;
    private String age;
    private String gender;
    private String university;
    private String average;
    private String year;
    private ArrayList<String> answers;

    public Answer(String username, String age, String gender, String university, String average, String year, ArrayList<String> answers) {
        this.username = username;
        this.age = age;
        this.gender = gender;
        this.university = university;
        this.average = average;
        this.year = year;
        this.answers = answers;
    }

    public Answer(){};

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }


    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
