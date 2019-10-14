package com.jagtar.fishing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GameLogic extends SurfaceView implements Runnable {
    private Thread gameThread = null;
    private volatile boolean gameIsRunning;
    private Canvas canvas;
    private Paint paintbrush;
    private SurfaceHolder holder;
    private int screenWidth;
    private int screenHeight;

    double MOUSETAP_X = 100;
    double MOUSETAP_Y = 700;
    int newX;
    int newY;
    double xn = 0.0;
    double yn = 0.0;
    int bullet_count = 50;
    int life_count = 5;
    int car_speed = 30;
    int bullet_speed = 100;

    CreateSprite gun;
    CreateSprite bullet;
    CreateSprite car;
    GameBackground leftwall;
    GameBackground rightWall;
    GameBackground ggg, bgonly, ggg2, outofwater;
    ArrayList<CreateSprite> bullets = new ArrayList<CreateSprite>();

    public GameLogic(Context context, int screenW, int screenH) {
        super(context);
        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = screenW;
        this.screenHeight = screenH;

        this.car = new CreateSprite(this.getContext(), this.screenWidth - 300, 700, R.drawable.audi);
        this.gun = new CreateSprite(this.getContext(), 100, this.screenHeight - 500, R.drawable.gun);
        this.bullet = new CreateSprite(this.getContext(), 100, this.screenHeight - 500, R.drawable.rec);
        this.leftwall = new GameBackground(this.getContext(), -100, -screenHeight, R.drawable.leftwall);
        this.rightWall = new GameBackground(this.getContext(), screenWidth - 100, -screenHeight, R.drawable.rightwall);
        this.ggg = new GameBackground(this.getContext(), 0, 0, R.drawable.rrr);
        this.ggg2 = new GameBackground(this.getContext(), 0, 0, R.drawable.rrr);
        this.bgonly = new GameBackground(this.getContext(), 0, 0, R.drawable.bgonly);
        this.outofwater = new GameBackground(this.getContext(), 0, 0, R.drawable.aaabbb);
    }

    @Override
    public void run() {
        while (gameIsRunning == true) {
            steps();
            drawsteps();
            controlFPS();
        }
    }

    boolean bgMovingDown = false;
    boolean bgMovingUp = false;
    int spacetop = 500;
    int newTime = 10;
    int fishingstring = 500;
    int time = 0;
    int currtime = 0;
    int ccc = 0;

    public void steps() {
        //if fishing string is finished, move the background in opposite direction
        if (fishingstring <= 0 && (ggg.getyPosition() + screenHeight) >= screenHeight && bgMovingUp) {
            bgMovingUp = false;
            bgMovingDown = true;
            outofwater.setyPosition(-500);
            time = (int) System.currentTimeMillis();
        }
        Log.d("fishing", fishingstring + "");
        //background moving down
        if (bgMovingDown == true) {

            if (fishingstring <= 500)
                ggg.setyPosition((ggg.getyPosition() + 10));
            fishingstring += 10;
            Log.d("outofwater", outofwater.getyPosition() + "");

            if ((fishingstring >= 500)) {
                currtime = (int) System.currentTimeMillis();
                if ((currtime - time) > newTime) {
                    time = currtime;
                    if ((outofwater.getyPosition() + 500) <= 500) {
                        outofwater.setyPosition(outofwater.getyPosition() + 10);
                    }
                    if (spacetop <= 500) {
                        spacetop += 10;
                    } else {
                        bgMovingUp = false;
                        bgMovingDown = false;
                    }
                }
            }
            if (ggg.getyPosition() > screenHeight) {
                ggg.setyPosition(0);
                ccc = 1;
            }
        }
        if (bgMovingUp == true) {
            //covering the space at top with moving bag
            if (spacetop != 0) spacetop -= 10;
            //decreasing fishing string
            fishingstring -= 10;
            //basic functionality
            ggg.setyPosition((ggg.getyPosition() - 10));
            //moving outofwater image
            if ((outofwater.getyPosition() + 500) >= 0) {
                outofwater.setyPosition(outofwater.getyPosition() - 20);
            }

            if ((ggg.getyPosition() + screenHeight) < 0) {
                ggg.setyPosition(0);
                ccc = 1;
            }
        }
    }

    public void drawsteps() {
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();
            paintbrush.setStyle(Paint.Style.FILL);
            paintbrush.setStrokeWidth(8);
            //flat background
            Rect bgg = new Rect(0, 0, screenWidth, screenHeight);
            canvas.drawBitmap(bgonly.getImage(), null, bgg, null);

            Rect waterout = new Rect(0, outofwater.getyPosition(), screenWidth, outofwater.getyPosition() + 500);
            canvas.drawBitmap(outofwater.getImage(), null, waterout, null);


            //moving background
            Rect rr = new Rect(0, spacetop + ggg.getyPosition(), screenWidth, screenHeight + ggg.getyPosition());
            canvas.drawBitmap(ggg.getImage(), null, rr, null);

            if ((ggg.getyPosition() > 0) && bgMovingDown) {
                Rect spacecover = new Rect(0, (ggg.getyPosition() - (screenHeight + spacetop)), screenWidth, (screenHeight + ggg.getyPosition()) - screenHeight);
                canvas.drawBitmap(ggg2.getImage(), null, spacecover, null);
            }
            if (((ggg.getyPosition() + screenHeight) < screenHeight) && bgMovingUp) {
                Rect spacecover2 = new Rect(0, (ggg.getyPosition() + (screenHeight + spacetop)), screenWidth, (screenHeight + ggg.getyPosition()) + screenHeight);
                canvas.drawBitmap(ggg2.getImage(), null, spacecover2, null);
            }
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void controlFPS() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
        }
    }
    double mouseXm[] = new double[50];
    double mouseYm[] = new double[50];
    int list_length = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int userAction = event.getActionMasked();
        if (userAction == MotionEvent.ACTION_DOWN) {
            MOUSETAP_X = event.getX();
            MOUSETAP_Y = event.getY();
            bgMovingUp = true;
        } else if (userAction == MotionEvent.ACTION_UP) {
        }
        return true;
    }

    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resumeGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

}

