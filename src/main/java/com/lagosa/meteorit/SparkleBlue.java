package com.lagosa.meteorit;

import android.widget.ImageView;

import java.util.Random;

public class SparkleBlue {
    ImageView sparkleView;
    float x,y;
    Random rand = new Random();

    public SparkleBlue(float x, float y, ImageView sparkle){
        this.x = x;
        this.y = y;
        sparkleView = sparkle;
    }

    public void SparkleMove(){
        y-=1;
        x+=rand.nextInt(3);

        sparkleView.setX(x);
        sparkleView.setY(y);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
