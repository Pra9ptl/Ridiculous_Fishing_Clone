package com.jagtar.fishing;

public class Huds {
    private String start;
    private int Score;
    private int num_of_fisshes;

    public Huds() {
         start = "Tap to start!";
         Score = 0;
         num_of_fisshes = 0;
    }

    public String getStart() {
        return start;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public int getNum_of_fisshes() {
        return num_of_fisshes;
    }

    public void setNum_of_fisshes(int num_of_fisshes) {
        this.num_of_fisshes = num_of_fisshes;
    }
}
