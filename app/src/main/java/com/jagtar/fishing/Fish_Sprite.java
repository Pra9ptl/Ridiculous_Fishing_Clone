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

    public Fish_Sprite(Context context, int x, int y, int imageName) {
        this.xPosition = x;
        this.yPosition = y;

        this.image = BitmapFactory.decodeResource(context.getResources(), imageName);

        this.hitbox = new Rect(
                this.xPosition,
                this.yPosition,
                this.xPosition + this.image.getWidth(),
                this.yPosition + this.image.getHeight()
        );

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
        this.hitbox.left = this.xPosition;
        this.hitbox.top = this.yPosition;
        this.hitbox.right = this.xPosition + this.image.getWidth();
        this.hitbox.bottom = this.yPosition + this.image.getHeight();
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
}
