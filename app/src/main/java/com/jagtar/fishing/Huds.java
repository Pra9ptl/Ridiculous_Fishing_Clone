package com.jagtar.fishing;

public class Huds {
    private String start;
    private int Score;
    private int num_of_fisshes;
    private String fishString;

    public Huds() {
         start = "Tap to start!";
         Score = 0;
         num_of_fisshes = 0;
         fishString = "50/50";
    }

    public void setFishString(String fishString) {
        this.fishString = fishString;
    }

    public String getFishString() {
        return fishString;
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
