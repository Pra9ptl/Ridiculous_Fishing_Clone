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
import java.util.List;
import java.util.Random;

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

    GameBackground movingbg, bgonly, outofwater, pin;

    Fish_Sprite fish, octo, s_horse, target;
    List<Fish_Sprite> target_fishes;

    public GameLogic(Context context, int screenW, int screenH) {
        super(context);
        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = screenW;
        this.screenHeight = screenH;

        this.movingbg = new GameBackground(this.getContext(), 0, 0, R.drawable.rrr);
        this.bgonly = new GameBackground(this.getContext(), 0, 0, R.drawable.bgonly);
        this.outofwater = new GameBackground(this.getContext(), 0, 0, R.drawable.boatbackground);
        this.pin = new GameBackground(this.getContext(), 470, 680, R.drawable.pin32);

        this.fish = new Fish_Sprite(this.getContext(), 0, 0, R.drawable.fish);
        this.octo = new Fish_Sprite(this.getContext(), 70, 70, R.drawable.octopus);
        this.s_horse = new Fish_Sprite(this.getContext(), 140, 140, R.drawable.seahorse);

        this.target_fishes = new ArrayList<Fish_Sprite>();
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
    boolean drawbgDown = false;
    boolean drawbgUp = false;
    int newTime = 10;
    int fishingstring = 2000;
    int timetofish = 2000;
    boolean pindown = false;
    boolean pinup = false;
    int time = 0;
    int currtime = 0;
    int ccc = 0;
    boolean fish_moving_left = true;
    boolean octo_moving_left = false;
    boolean hrs_moving_left = true;

    public void steps() {
        /*if fishing string is finished, move the background in opposite direction*/
        if (fishingstring <= 0 && /*(movingbg.getyPosition() + screenHeight) >= screenHeight &&*/ bgMovingUp) {
            bgMovingUp = false;
            bgMovingDown = true;
            drawbgDown = true;
            outofwater.setyPosition(-900);
            time = (int) System.currentTimeMillis();
        }
        /************************************/

        Log.d("fishing", fishingstring + "");
        //background moving down
        if (bgMovingDown == true) {
            if (fishingstring <= timetofish)
                movingbg.setyPosition((movingbg.getyPosition() + 10));
            //Grabbing back the fishing string
            fishingstring += 10;
            Log.d("stringfish", fishingstring + "");

            /*IF TOP IS NEAR THEN MOVE BOATBACKGROUND BACK AT TIS PLACE*/
            if ((fishingstring >= (timetofish - 900))) {
                if ((outofwater.getyPosition() + 900) <= 900) {
                    outofwater.setyPosition(outofwater.getyPosition() + 10);
                }
                if (outofwater.getyPosition() >= 0) {
                    bgMovingUp = false;
                    bgMovingDown = false;
                    pinup = true;
                }
                /****************************************************************/
                if (movingbg.getyPosition() > screenHeight) {
                    movingbg.setyPosition(0);
                    ccc = 1;
                }
            }
        }
        if (bgMovingUp == true) {
            //Throwing fishing string
            fishingstring -= 10;
            Log.d("stringfish", fishingstring+ "");

            //moving the background
            movingbg.setyPosition((movingbg.getyPosition() - 10));
            //moving boatbackground up
            if ((outofwater.getyPosition() + 900) >= 0) {
                outofwater.setyPosition(outofwater.getyPosition() - 10);
            }
            if((outofwater.getyPosition() + 900) <= 0) {
                Log.d("calc", "decrease this much: " + (timetofish - fishingstring)+ "");
                Log.d("calc", "boat photo position" + (outofwater.getyPosition() + 900)+ "");
            }

            //resetting background when it reaches top
            if ((movingbg.getyPosition() + screenHeight) < 0) {
                movingbg.setyPosition(0);
                ccc = 1;
            }
        }

        /*********FISHING PIN MOVEMENT********/
        if (pin.getyPosition() >= (screenHeight / 2) - 200) {
            pindown = false;
        }
        if (pin.getyPosition() <= 680) {
            pinup = false;
        }
        if (pindown) {
            pin.setyPosition(pin.getyPosition() + 3);
        }
        if (pinup) {
            pin.setyPosition(pin.getyPosition() - 8);
        }
        /*********END FISHING PIN MOVEMENT********/

        /*********FISH MOVEMENT********/

        if(fish.getxPosition() > screenWidth-200) {
            //fish.setxPosition(fish.getxPosition() + 20);
            fish_moving_left = true;
        }
        else if(fish.getxPosition() < 0){
            fish_moving_left = false;
        }

        if(fish_moving_left == true){
            fish.setxPosition(fish.getxPosition() - 10);
        }else{
            fish.setxPosition(fish.getxPosition() + 10);
        }

        /*********END FISH MOVEMENT********/

        /*********OCTOPUS MOVEMENT********/

        if(octo.getxPosition() > screenWidth-200) {
            //fish.setxPosition(fish.getxPosition() + 20);
            octo_moving_left = true;
        }
        else if(octo.getxPosition() < 0){
            octo_moving_left = false;
        }

        if(octo_moving_left == true){
            octo.setxPosition(fish.getxPosition() - 10);
        }else{
            octo.setxPosition(fish.getxPosition() + 10);
        }

        /*********OCTOPUS MOVEMENT********/

        /*********SEA HORSE MOVEMENT********/

        if(s_horse.getxPosition() > screenWidth-200) {
            //fish.setxPosition(fish.getxPosition() + 20);
            hrs_moving_left = true;
        }
        else if(s_horse.getxPosition() < 0){
            hrs_moving_left = false;
        }

        if(hrs_moving_left == true){
            s_horse.setxPosition(s_horse.getxPosition() - 10);
        }else{
            s_horse.setxPosition(s_horse.getxPosition() + 10);
        }

        /*********SEA HORSE MOVEMENT********/
    }

    public void drawsteps() {
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();
            paintbrush.setStyle(Paint.Style.FILL);
            paintbrush.setStrokeWidth(8);
            //flat background
            Rect bgg = new Rect(0, 0, screenWidth, screenHeight);
            canvas.drawBitmap(bgonly.getImage(), null, bgg, null);



            //moving background
            Rect rr = new Rect(0,movingbg.getyPosition(), screenWidth, screenHeight + movingbg.getyPosition());
            canvas.drawBitmap(movingbg.getImage(), null, rr, null);

            if ((movingbg.getyPosition() > 0) && drawbgDown) {
                Rect spacecover = new Rect(0, (movingbg.getyPosition() - (screenHeight)), screenWidth, (screenHeight + movingbg.getyPosition()) - screenHeight);
                canvas.drawBitmap(movingbg.getImage(), null, spacecover, null);
            }
            if (((movingbg.getyPosition() + screenHeight) < screenHeight) && drawbgUp) {
                Rect spacecover2 = new Rect(0, (movingbg.getyPosition() + (screenHeight)), screenWidth, (screenHeight + movingbg.getyPosition()) + screenHeight);
                canvas.drawBitmap(movingbg.getImage(), null, spacecover2, null);
            }
            //boat background
            Rect waterout = new Rect(0, outofwater.getyPosition(), screenWidth, outofwater.getyPosition() + 900);
            canvas.drawBitmap(outofwater.getImage(), null, waterout, null);

            //Draw target sprites
            int rx = rX_Pos_Gen();
            int ry = rY_Pos_Gen();

            System.out.println("RX = " + rx);
            System.out.println("RY = " + ry);



            canvas.drawBitmap(fish.getImage(), fish.getxPosition(),fish.getyPosition(), null);
            canvas.drawBitmap(octo.getImage(), octo.getxPosition(), octo.getyPosition(), null);
            canvas.drawBitmap(s_horse.getImage(), s_horse.getxPosition(), s_horse.getyPosition(), null);

            //drawing pin
            canvas.drawBitmap(pin.getImage(), pin.getxPosition(), pin.getyPosition(), null);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public int rX_Pos_Gen(){
        Random r_num = new Random();
        int pos = r_num.nextInt(screenWidth-1);
        return pos;
    }

    public int rY_Pos_Gen(){
        Random r_num = new Random();
        int pos = r_num.nextInt(screenHeight-1);
        return pos;
    }

    public void controlFPS() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
        }
    }

    double mouseXm[] = new double[50];
    double mouseYm[] = new double[50];

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int userAction = event.getActionMasked();
        if (userAction == MotionEvent.ACTION_DOWN) {
            MOUSETAP_X = event.getX();
            MOUSETAP_Y = event.getY();
            newTime = 10;
            fishingstring = 2000;
            timetofish = 2000;
            pindown = false;
            pinup = false;
            time = 0;
            currtime = 0;
            ccc = 0;
            //moving bg on tap
            bgMovingUp = true;
            drawbgUp = true;
            //moving pin
            pindown = true;
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