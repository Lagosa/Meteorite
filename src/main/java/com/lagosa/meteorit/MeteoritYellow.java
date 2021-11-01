package com.lagosa.meteorit;

import android.widget.ImageView;

import java.util.Random;

public class MeteoritYellow {
    ImageView meteoritYellow;
    float meteoritX, meteoritY;
    float VELOCITY = 30;
    Random rand = new Random();


    public MeteoritYellow(int x, int y, ImageView image){
        meteoritX = x;
        meteoritY = y;
        meteoritYellow = image;

        meteoritYellow.setX(meteoritX);
        meteoritYellow.setY(meteoritY);
    }


    public void moveMeteorit(){
        meteoritY += VELOCITY + rand.nextInt(10);
        meteoritX += rand.nextInt(10) - rand.nextInt(10);

        meteoritYellow.setX(meteoritX);
        meteoritYellow.setY(meteoritY);



    }
    public void setVelocity(float velocity){
        VELOCITY = velocity;
    }
    public float getX(){
        return meteoritX;
    }
    public float getY(){
        return meteoritY;
    }
    public void setX(float x){
        this.meteoritX = x;
        meteoritYellow.setX(meteoritX);
    }
    public void setY(float y){
        this.meteoritY = y;
        meteoritYellow.setY(meteoritY);
    }
}
