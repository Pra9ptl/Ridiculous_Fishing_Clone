package com.jagtar.fishing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.graphics.Point;
import android.view.Display;

public class MainActivity extends AppCompatActivity {

    private GameLogic gameLogic;
    Display display;
    Point size;
    int screenHeight;
    int screenWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        gameLogic = new GameLogic(this, screenWidth, screenHeight);
        setContentView(gameLogic);
    }
    @Override
    protected void onPause() {
        super.onPause();
        gameLogic.pauseGame();
    }
    @Override
    protected void onResume() {
        super.onResume();
        gameLogic.resumeGame();
    }

}

