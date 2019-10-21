package com.jagtar.fishing;

public class Huds {
    private String start;
    private String Score;
    private String num_of_fisshes;

    public Huds() {
         start = "Tap to start!";
         Score = "0";
         num_of_fisshes = "0";
    }

    public String getStart() {
        return start;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public String getNum_of_fisshes() {
        return num_of_fisshes;
    }

    public void setNum_of_fisshes(String num_of_fisshes) {
        this.num_of_fisshes = num_of_fisshes;
    }
}
