package com.lagosa.meteorit;

import android.widget.ImageView;

import java.util.Random;

public class MeteoritRed{

    private float meteoritX, meteoritY;
    private ImageView meteoritRed;
    public int VELOCITY = 20;
    private Random rand = new Random();

    public MeteoritRed(float x, float y, ImageView image){
        meteoritX = x;
        meteoritY = y;
        meteoritRed = image;
    }

    public void moveMeteorit(){
        meteoritY += VELOCITY + rand.nextInt(20);
        meteoritX += rand.nextInt(10) - rand.nextInt(10);

        meteoritRed.setX(meteoritX);
        meteoritRed.setY(meteoritY);



    }
    public float getX(){
        return meteoritX;
    }
    public float getY(){
        return meteoritY;
    }
    public void setX(float x){
        this.meteoritX = x;
        meteoritRed.setX(meteoritX);
    }
    public void setY(float y){
        this.meteoritY = y;
        meteoritRed.setY(meteoritY);
    }
    public void setVELOCITY(int velocity){
        this.VELOCITY = velocity;
    }

}
