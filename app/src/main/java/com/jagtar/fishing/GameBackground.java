package com.jagtar.fishing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class GameBackground {

        private int xPosition;
        private int yPosition;
        private int imageWidth;
        private int imageHeight;
        Bitmap bgimage;
        Rect hitbox;

        double xn = 0;
        double yn = 0;

        public GameBackground(Context context, int x, int y, int imageName) {
            this.xPosition = x;
            this.yPosition = y;

            this.bgimage = BitmapFactory.decodeResource(context.getResources(), imageName);

            this.hitbox = new Rect(
                    this.xPosition + 30,
                    this.yPosition + 60,
                    this.xPosition + this.bgimage.getWidth() - 35,
                    this.yPosition + this.bgimage.getHeight() - 10
            );
        }


    public int getImageWidth() {
        return this.bgimage.getWidth();
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
        bgimage.setWidth(this.imageWidth);
    }

    public int getImageHeight() {
        return this.bgimage.getHeight();
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
        bgimage.setHeight(this.imageHeight);
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
            this.hitbox.left = this.xPosition + 35;
            this.hitbox.top = this.yPosition + 60;
            this.hitbox.right = this.xPosition + this.bgimage.getWidth() - 35;
            this.hitbox.bottom = this.yPosition + this.bgimage.getHeight() - 10;
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
            return bgimage;
        }

        public void setImage(Bitmap image) {
            this.bgimage = image;
        }
    }
