package com.jagtar.fishing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Fish_Sprite {
    private int xPosition;
    private int yPosition;
    Bitmap image;
    Rect hitbox;
    String i_name;

    double xn = 0;
    double yn = 0;

    //property for moving right and left
    boolean moving_left = true;

    public boolean isMoving_left() {
        return moving_left;
    }

    public void setMoving_left(boolean moving_left) {
        this.moving_left = moving_left;
    }

    public Fish_Sprite(Context context, int x, int y, int imageName, String name) {
        this.xPosition = x;
        this.yPosition = y;

        this.image = BitmapFactory.decodeResource(context.getResources(), imageName);

        this.hitbox = new Rect(
                this.xPosition + 25,
                this.yPosition + 15,
                this.xPosition + this.image.getWidth() - 25,
                this.yPosition + this.image.getHeight() - 50
        );

        i_name = name;

    }
    public double getXn() {
        return xn;
    }

    public void setXn(double xn)
    {
        this.xn = xn;
    }

    public double getYn() {
        return yn;
    }

    public void setYn(double yn)
    {
        this.yn = yn;
    }

    public Rect getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rect hitbox) {
        this.hitbox = hitbox;
    }

    public void updateHitbox() {
        this.hitbox.left = this.xPosition + 22;
        this.hitbox.top = this.yPosition + 15;
        this.hitbox.right = this.xPosition + this.image.getWidth() - 22;
        this.hitbox.bottom = this.yPosition + this.image.getHeight() - 50;
    }
    public int getxPosition()
    {
        return xPosition;
    }

    public void setxPosition(int xPosition)
    {
        this.xPosition = xPosition;
    }

    public int getyPosition()
    {
        return yPosition;
    }

    public void setyPosition(int yPosition)
    {
        this.yPosition = yPosition;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getI_name() {
        return i_name;
    }

    public void setI_name(String i_name) {
        this.i_name = i_name;
    }
}
