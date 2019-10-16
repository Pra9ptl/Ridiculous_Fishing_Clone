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
    private static int initalPinXpos;

    double MOUSETAP_X = 100;
    double MOUSETAP_Y = 700;

    GameBackground movingbg, bgonly, outofwater, pin;

    //list of targets in game
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
        initalPinXpos = this.pin.getxPosition();

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
    boolean targetMovingDown = false;
    boolean targetMovingUp = false;
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
    int total_Target_Count = 0;
    boolean itsTime = false;

    public void steps() {

        //movement of the targets in the game
        if(itsTime){
            for(int i = 0; i<target_fishes.size(); i++) {
                //method is decalred below takes object of Fish_Sprite
                move_target_animals(target_fishes.get(i));
            }
        }

        /*if fishing string is finished, move the background in opposite direction*/
        if (fishingstring <= 0 && /*(movingbg.getyPosition() + screenHeight) >= screenHeight &&*/ bgMovingUp) {
            bgMovingUp = false;
            bgMovingDown = true;
            targetMovingDown = true;
            targetMovingUp = false;
            drawbgDown = true;
            outofwater.setyPosition(-900);
            time = (int) System.currentTimeMillis();
        }
        /************************************/

        Log.d("fishing", fishingstring + "");
        //background moving down
        if (bgMovingDown == true) {
            if (fishingstring <= timetofish)
                movingbg.setyPosition((movingbg.getyPosition() + 20));
            //Grabbing back the fishing string
            fishingstring += 10;


            /*IF TOP IS NEAR THEN MOVE BOATBACKGROUND BACK AT TIS PLACE*/
            if ((fishingstring >= (timetofish - 900))) {
                if ((outofwater.getyPosition() + 900) <= 900) {
                    outofwater.setyPosition(outofwater.getyPosition() + 20);
                }
                if (outofwater.getyPosition() >= 0) {
                    bgMovingUp = false;
                    bgMovingDown = false;
                    targetMovingDown = false;
                    targetMovingUp = false;
                    pinup = true;
                    this.pin.setxPosition(initalPinXpos);
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
            movingbg.setyPosition((movingbg.getyPosition() - 20));
            //moving boatbackground up
            if ((outofwater.getyPosition() + 900) >= 0) {
                outofwater.setyPosition(outofwater.getyPosition() - 20);
            }
            if((outofwater.getyPosition() + 900) <= 0) {
                Log.d("calc", "decrease this much: " + (timetofish - fishingstring)+ "");
                Log.d("calc", "boat photo position" + (outofwater.getyPosition() + 900)+ "");
                itsTime = true;
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


    }
    public void move_target_animals(Fish_Sprite fs){

            if(fs.getxPosition() > (screenWidth-fs.getImage().getWidth())) {
                //fish.setxPosition(fish.getxPosition() + 20);
                fs.setMoving_left(false);
            }
            else if(fs.getxPosition() < 0){
                fs.setMoving_left(true);
            }

            if(fs.isMoving_left() == true){
                fs.setxPosition(fs.getxPosition() + 10);
            }else{
                fs.setxPosition(fs.getxPosition() - 10);
            }

            if(targetMovingUp == true){
                fs.setyPosition(fs.getyPosition() - 20);
            }
            if(targetMovingDown == true){
                fs.setyPosition(fs.getyPosition() + 20);
            }
    }

    long currentTime = 0;
    long previousTime = 0;

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
            for (int i = 0; i < target_fishes.size(); i++) {
                canvas.drawBitmap(target_fishes.get(i).getImage(), target_fishes.get(i).getxPosition(), target_fishes.get(i).getyPosition(), null);
            }
            //boat background
            Rect waterout = new Rect(0, outofwater.getyPosition(), screenWidth, outofwater.getyPosition() + 900);
            canvas.drawBitmap(outofwater.getImage(), null, waterout, null);

            currentTime = System.currentTimeMillis();
            if ((currentTime - previousTime) > 500) {
                System.out.println("tapout done");

                //Draw target sprites
                if (itsTime == true) {
                    if (total_Target_Count < 20) {


                        int rx = rX_Pos_Gen();
                        int ry = screenHeight - 20;
                        Fish_Sprite target = null;
                        Random r = new Random();
                        int sprite_Index = r.nextInt(3);

                        if (sprite_Index == 0) {
                            target = new Fish_Sprite(this.getContext(), rx, ry, R.drawable.fish);
                        }

                        if (sprite_Index == 1) {
                            target = new Fish_Sprite(this.getContext(), rx, ry, R.drawable.octopus);
                        }

                        if (sprite_Index == 2) {
                            target = new Fish_Sprite(this.getContext(), rx, ry, R.drawable.seahorse);
                        }
                        if (target != null) {
                            target_fishes.add(target);
                        }

                        total_Target_Count++;
                    }
                }
                previousTime = currentTime;
            }
            //drawing pin
            canvas.drawBitmap(pin.getImage(), pin.getxPosition(), pin.getyPosition(), null);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public int rX_Pos_Gen(){
        Random r_num = new Random();
        int pos = r_num.nextInt(screenWidth-32);
        return pos;
    }

    public int rY_Pos_Gen(){
        Random r_num = new Random();
        int pos = r_num.nextInt(screenHeight-32);
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
            targetMovingUp = true;
            drawbgUp = true;
            //moving pin
            pindown = true;
        } else if (userAction == MotionEvent.ACTION_UP) {
        } else if (userAction == MotionEvent.ACTION_MOVE){
            int tapX = (int) event.getX();
            pin.setxPosition(tapX);
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