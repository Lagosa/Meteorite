package com.lagosa.meteorit;

import android.widget.ImageView;

import java.util.Random;

public class MeteoritBlue{
    ImageView meteoritBlue;
     float meteoritX, meteoritY;
    float VELOCITY = 40;
    Random rand = new Random();


    public MeteoritBlue(int x, int y, ImageView image){
        meteoritX = x;
        meteoritY = y;
        meteoritBlue = image;

        meteoritBlue.setX(meteoritX);
        meteoritBlue.setY(meteoritY);
    }


    public void moveMeteorit(){
        meteoritY += VELOCITY + rand.nextInt(10);
        meteoritX += rand.nextInt(10) - rand.nextInt(10);

        meteoritBlue.setX(meteoritX);
        meteoritBlue.setY(meteoritY);



    }
    public float getX(){
        return meteoritX;
    }
    public float getY(){
        return meteoritY;
    }
    public void setVelocity(float velocity){
        VELOCITY = velocity;
    }
    public void setX(float x){
        this.meteoritX = x;
        meteoritBlue.setX(meteoritX);
    }
    public void setY(float y){
        this.meteoritY = y;
        meteoritBlue.setY(meteoritY);
    }
}
