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
    GameBackground ggg, bgonly,ggg2, outofwater;
    ArrayList<CreateSprite> bullets = new ArrayList<CreateSprite>();
    public GameLogic(Context context, int screenW, int screenH) {
        super(context);
        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = screenW;
        this.screenHeight = screenH;

        this.car = new CreateSprite(this.getContext(), this.screenWidth -300, 700, R.drawable.audi);
        this.gun = new CreateSprite(this.getContext(), 100, this.screenHeight - 500, R.drawable.gun);
        this.bullet = new CreateSprite(this.getContext(), 100, this.screenHeight - 500, R.drawable.rec);
        this.leftwall = new GameBackground(this.getContext(),-100,-screenHeight,R.drawable.leftwall);
        this.rightWall = new GameBackground(this.getContext(),screenWidth - 100, -screenHeight, R.drawable.rightwall);
        this.ggg = new GameBackground(this.getContext(),0, 0, R.drawable.rrr);
        this.ggg2 = new GameBackground(this.getContext(),0, 0, R.drawable.rrr);
        this.bgonly = new GameBackground(this.getContext(),0, 0, R.drawable.bgonly);
        this.outofwater = new GameBackground(this.getContext(),0, 0, R.drawable.outofwater);
    }
    @Override
    public void run() {
        while (gameIsRunning == true) {
            steps();
            drawsteps();
            controlFPS();
        }
    }
    boolean carMovingUp = true;
    boolean carMovingDown = true;
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

            if(fishingstring <= 500)
            ggg.setyPosition((ggg.getyPosition() + 10));
            fishingstring += 10;
            Log.d("outofwater", outofwater.getyPosition() + "");

            if ((fishingstring >= 500)) {
                    currtime = (int) System.currentTimeMillis();
                    if((currtime - time) > newTime )
                    {
                        time = currtime;
//                        bgMovingUp = false;
//                        bgMovingDown = false;
                       // newTime = (int) System.currentTimeMillis();
                        if((outofwater.getyPosition() + 500) <= 500){
                        outofwater.setyPosition(outofwater.getyPosition() + 10);}
                        if(spacetop <=500)
                        {
                        spacetop += 10;}
//                        time = (int) System.currentTimeMillis();
                    }
//
            }



            if (ggg.getyPosition() > screenHeight) {
                ggg.setyPosition(0);
//            if ((fishingstring >= 100)) {
//                spacetop += 10;
//            }
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
//            leftwall.setyPosition(leftwall.getyPosition() + 40);
//            rightWall.setyPosition((rightWall.getyPosition() + 40));
//            if(leftwall.getyPosition() > screenHeight)
//            {
//                leftwall.setyPosition(-screenHeight);
//            }
//
//            if(rightWall.getyPosition() > screenHeight)
//            {
//                rightWall.setyPosition(0-screenHeight);
//            }
    }


//        if(isBgMovingUp)
//        {
//            bgimage.setyPosition(bgimage.getyPosition() - 20);
//            if(bgimage.getyPosition() + screenHeight < 0)
//            {
//                bgimage.setyPosition(0);
//            }
//        }

//
//        if(bullet_count <= 0 || life_count <= 0)
//        {
//            car_speed  = 0;
//            carMovingDown = false;
//            carMovingUp = false;
//            bullet_speed = 0;
//        }
//
//        if(carMovingUp == true)
//        {
//            this.car.setyPosition(this.car.getyPosition() - car_speed);
//            this.car.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.audi));
//
//        }else if (carMovingDown == true)
//        {
//            this.car.setyPosition(this.car.getyPosition() + car_speed);
//            this.car.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.audi2));
//        }
//
//        if(car.getyPosition() < 0 )
//        {
//            car_speed = car_speed + 2;
//            carMovingUp = false;
//            carMovingDown = true;
//        }
//        else if (car.getyPosition() + this.car.image.getHeight() >= this.screenHeight - 200)
//        {
//            car_speed = car_speed + 2;
//            carMovingUp = true;
//            carMovingDown = false;
//        }
//        for(int i = 0; i< bullets.size(); i++) {
//            newX = bullets.get(i).getxPosition() + (int) (mouseXm[i] * bullet_speed);
//            newY = bullets.get(i).getyPosition() + (int) (mouseYm[i] * bullet_speed);
//            bullets.get(i).setxPosition(newX);
//            bullets.get(i).setyPosition(newY);
//
//            if (this.bullets.get(i).getHitbox().intersect(this.car.getHitbox())) {
//                bullets.remove(i);
//                life_count -= 1;
//            }
//        }


    public void drawsteps() {
        if (holder.getSurface().isValid()) {

            canvas = holder.lockCanvas();
//            canvas.drawColor(Color.argb(255, 153, 0, 76));
            paintbrush.setStyle(Paint.Style.FILL);
            paintbrush.setStrokeWidth(8);
            //flat background
            Rect bgg = new Rect (0, 0,screenWidth,screenHeight);
            canvas.drawBitmap(bgonly.getImage(),null,bgg,null);

            Rect waterout = new Rect (0, outofwater.getyPosition(),screenWidth,outofwater.getyPosition() + 500);
            canvas.drawBitmap(outofwater.getImage(),null,waterout,null);



            //moving background
            Rect rr = new Rect (0, spacetop + ggg.getyPosition(),screenWidth,screenHeight + ggg.getyPosition());
            canvas.drawBitmap(ggg.getImage(),null,rr,null);

            if((ggg.getyPosition() > 0) && bgMovingDown)
            {
                Rect spacecover = new Rect (0,(ggg.getyPosition() - (screenHeight+spacetop)),screenWidth,(screenHeight + ggg.getyPosition()) - screenHeight);
                canvas.drawBitmap(ggg2.getImage(),null,spacecover,null);
            }
            if(((ggg.getyPosition()+screenHeight) < screenHeight) && bgMovingUp)
            {
                Rect spacecover2 = new Rect (0,(ggg.getyPosition() + (screenHeight+spacetop)),screenWidth,(screenHeight + ggg.getyPosition()) + screenHeight);
                canvas.drawBitmap(ggg2.getImage(),null,spacecover2,null);
            }


//            canvas.drawBitmap(leftwall.getImage(),leftwall.getxPosition(),leftwall.getyPosition(),null);
//            canvas.drawBitmap(rightWall.getImage(),rightWall.getxPosition(),rightWall.getyPosition(),null);
//
//            if(bgMovingDown && (this.leftwall.getyPosition() > 0))
//            {
//                canvas.drawBitmap(leftwall.getImage(),leftwall.getxPosition(),(leftwall.getyPosition() - screenHeight),null);
//                canvas.drawBitmap(rightWall.getImage(),rightWall.getxPosition(),(rightWall.getyPosition() - screenHeight),null);
//
//            }




//            canvas.drawBitmap(this.car.getImage(), this.car.getxPosition(), this.car.getyPosition(), paintbrush);
//            canvas.drawBitmap(this.gun.getImage(), this.gun.getxPosition(), this.gun.getyPosition(), paintbrush);
//            for(int i = 0; i< bullets.size(); i++) {
//                canvas.drawBitmap(this.bullets.get(i).getImage(), this.bullets.get(i).getxPosition(), this.bullets.get(i).getyPosition(), paintbrush);
//            }
//
//            paintbrush.setTextSize(60);
//            paintbrush.setStrokeWidth(5);
//            paintbrush.setColor(Color.WHITE);
//            String bullets_left = "Bullets Left: " + bullet_count;
//            canvas.drawText(bullets_left, 10, 100, paintbrush);
//
//            paintbrush.setTextSize(60);
//            paintbrush.setStrokeWidth(5);
//            String lives_left = "Lives: " + life_count;
//            canvas.drawText(lives_left, this.screenWidth - 250, 100, paintbrush);
//
//
//            paintbrush.setTextSize(100);
//            paintbrush.setStrokeWidth(5);
//            if(bullet_count > 0 && life_count <= 0)
//            {
//                String win = "You Win ";
//                canvas.drawText(win, this.screenWidth/2 - 200, this.screenHeight/2 - 200, paintbrush);
//            }
//            if(bullet_count <= 0 && life_count > 0)
//            {
//                String lose = "You Loose";
//                canvas.drawText(lose, this.screenWidth/2 - 200, this.screenHeight/2 - 200, paintbrush);
//            }
//
//            paintbrush.setStyle(Paint.Style.STROKE);
//            paintbrush.setColor(Color.RED);
//            this.gun.updateHitbox();
//            this.car.updateHitbox();
//
//            for(int i = 0; i< bullets.size(); i++) {
//            this.bullets.get(i).updateHitbox();
//        }

            holder.unlockCanvasAndPost(canvas);
        }
    }


    public void controlFPS() {
        try {
            gameThread.sleep(17);
        }
        catch (InterruptedException e) {
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
//            bgMovingDown = true;
//            spacetop = 0;

//            if(bullet_count > 0 && (life_count > 0)) {
//                this.bullet = new CreateSprite(this.getContext(), 100, this.screenHeight - 500, R.drawable.rec);
//                bullets.add(bullet);
//                bullet_count -= 1;
//
//                double a = this.MOUSETAP_X - bullet.getxPosition();
//                double b = this.MOUSETAP_Y - bullet.getyPosition();
//                list_length = bullets.size();
//                double d = Math.sqrt((a * a) + (b * b));
//                mouseXm[list_length - 1] = (a / d);
//                mouseYm[list_length - 1] = (b / d);
//            }

        }
        else if (userAction == MotionEvent.ACTION_UP) {
        }
        return true;
    }
    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        }
        catch (InterruptedException e) {
        }
    }
    public void  resumeGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

}

