package com.lagosa.meteorit;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class StartGame extends AppCompatActivity{

    // Ad related elements
    private InterstitialAd mInterstitialAd;


    // Display elements
    ImageView pause;
    TextView scoreView;
    TextView highScoreView;
    Display display;
    int displayHeight, displayWidth;

    // Database element
    DatabaseHelper db = new DatabaseHelper(this,null,null,1);

    // Gameover window elements
    LinearLayout gameOverLayout;
    TextView restartScoreView;
    TextView restartHighScoreView;
    Button restart;

    // Pause window elements
    LinearLayout pauseLayout;
    Button pauseButton;

    // Game logic variables
    Timer timer;
    Timer timerYellow;
    Timer timerBlue;
    Handler handler;
    Handler handlerYellow;
    boolean gameOver;
    boolean isPaused;
    Random rand;
    int score = 0;
    int highScore;
    boolean yellowM;
    boolean blueM;
    int VELOCITY;
    int monsterHeight;
    float displayDetectionBorder;
    int displayWidthMeteoriteRed;
    float monsterSetHeight;
    float monsterBottomLimit;

    // Monster characteristics
    ImageView monsterI;
    Drawable monsterD[];
    int frame;

    // Red meteorit characteristics
    int numberOfRedMeteorits = 10;
    ImageView meteoritRedView[];
    float meteoritRedX, meteoritRedY;
    MeteoritRed[] meteoritRed;

    // Yellow meteorit characteristics
    int numberOfYellowMeteorits = 3, numberOfYellowMeteoritsActive;
    ImageView meteoritYellowView[];
    float meteoritYellowX, meteoritYellowY;
    MeteoritYellow[] meteoritYellow;

    // Blue meteorit characteristics
    ImageView meteoritBlueView;
    float meteoritBlueX, meteoritBlueY;
    MeteoritBlue meteoritBlue;

    // Red sparkle
    ImageView sparkleRedView;
    SparkleRed sparkleRed;
    int sparkleRI;

    // Yellow sparkle
    ImageView sparkleYellowView;
    SparkleYellow sparkleYellow;
    int sparkleYI;

    // Blue sparkle
    ImageView sparkleBlueView;
    SparkleBlue sparkleBlue;
    int sparkleBI;

    //Level exposed
    ImageView monster_lvl_locked_1, monster_lvl_locked_2, monster_lvl_locked_3, monster_lvl_locked_4 ;
    Drawable monster_lvl_1[], monster_lvl_2[], monster_lvl_3[], monster_lvl_4[];


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display elements initializations
        setContentView(R.layout.activity_game);
        pause = findViewById(R.id.pause);
        scoreView = findViewById(R.id.score);
        highScoreView = findViewById(R.id.high_score);
        scoreView.setText(""+score);
        highScore = db.getData();
        highScoreView.setText(""+highScore);
        display = this.getWindowManager().getDefaultDisplay();
        displayHeight = display.getHeight();
        displayWidth = display.getWidth();
        VELOCITY = displayHeight/70;
        displayDetectionBorder = displayHeight - displayHeight/2f;

        // Ad initialization
       mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6893044542765685/9639580905");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        Log.d("startGame", "ADS INITIALIZED");
        mInterstitialAd.setAdListener(new AdListener(){
                                        @Override
                                        public void onAdClosed(){
                                            gameOver();
                                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                                        }
                                      }
        );

        // Gameover window initialization
        gameOverLayout = findViewById(R.id.gameOver);
        gameOverLayout.setVisibility(View.INVISIBLE);
        restart = findViewById(R.id.restartButton);
        restartScoreView = findViewById(R.id.yourScore);
        restartHighScoreView = findViewById(R.id.highScore);

        // Pause window initialization
        pauseLayout = findViewById(R.id.pauseLayout);
        pauseLayout.setVisibility(View.INVISIBLE);
        pauseButton = findViewById(R.id.startButton);

        // Game logic initialization
        gameOver = false;
        isPaused = false;

        // Monster initialization
        monsterI = findViewById(R.id.monster);
        monsterD = new Drawable[2];
        monsterD[0] = getResources().getDrawable(R.drawable.monster);
        monsterD[1] = getResources().getDrawable(R.drawable.monster2);
        monsterHeight = monsterI.getHeight() - 20;

        // Red meteorit initialization
        meteoritRedView = new ImageView[numberOfRedMeteorits];
        meteoritRedView[0] = findViewById(R.id.meteorit1);
        meteoritRedView[1] = findViewById(R.id.meteorit2);
        meteoritRedView[2] = findViewById(R.id.meteorit3);
        meteoritRedView[3] = findViewById(R.id.meteorit4);
        meteoritRedView[4] = findViewById(R.id.meteorit5);
        meteoritRedView[5] = findViewById(R.id.meteorit6);
        meteoritRedView[6] = findViewById(R.id.meteorit7);
        meteoritRedView[7] = findViewById(R.id.meteorit8);
        meteoritRedView[8] = findViewById(R.id.meteorit9);
        meteoritRedView[9] = findViewById(R.id.meteorit10);

        // Yellow meteorit initialization
        meteoritYellowView = new ImageView[numberOfYellowMeteorits];
        meteoritYellowView[0] = findViewById(R.id.meteoritYellow);
        meteoritYellowView[1] = findViewById(R.id.meteoritYellow2);
        meteoritYellowView[2] = findViewById(R.id.meteoritYellow3);

        // Blue meteorit initialization
        meteoritBlueView = findViewById(R.id.meteoritBlue);

        // Red sparkle initialization
        sparkleRedView = findViewById(R.id.sparkleRed);

        // Yellow sparkle initialization
        sparkleYellowView = findViewById(R.id.sparkleYellow);

        // Blue sparkle initialization
        sparkleBlueView = findViewById(R.id.sparkleBlue);

        // Locked level views initialization
        monster_lvl_locked_1 = findViewById(R.id.monster_level_locked_4);
        monster_lvl_locked_2 = findViewById(R.id.monster_level_locked_6);
        monster_lvl_locked_3 = findViewById(R.id.monster_level_locked_8);
        monster_lvl_locked_4 = findViewById(R.id.monster_level_locked_10);

        monster_lvl_1 = new Drawable[2];
        monster_lvl_1[0] = getResources().getDrawable(R.drawable.monster3);
        monster_lvl_1[1] = getResources().getDrawable(R.drawable.monster4);
        monster_lvl_2 = new Drawable[2];
        monster_lvl_2[0] = getResources().getDrawable(R.drawable.monster5);
        monster_lvl_2[1] = getResources().getDrawable(R.drawable.monster6);
        monster_lvl_3 = new Drawable[2];
        monster_lvl_3[0] = getResources().getDrawable(R.drawable.monster7);
        monster_lvl_3[1] = getResources().getDrawable(R.drawable.monster8);
        monster_lvl_4 = new Drawable[2];
        monster_lvl_4[0] = getResources().getDrawable(R.drawable.monster9);
        monster_lvl_4[1] = getResources().getDrawable(R.drawable.monster10);

        monsterSetHeight = displayHeight - displayHeight/2.8f;
        displayWidthMeteoriteRed = displayWidth-meteoritRedView[0].getWidth() - displayWidth/8;
        monsterBottomLimit = monsterSetHeight + monsterHeight + monsterSetHeight/4;


        // Calls to start the game
        startGame();
    }

    public void startGame(){
        gameOver = false;
        isPaused = false;
        yellowM = false;
        blueM = false;
        score = 0;
        highScore = db.getData();
        highScoreView.setText("" + highScore);

        // Sets image for the monster
        final long highScore = db.getData();
        if(highScore > 545){
            monsterD = monster_lvl_4;
            monster_lvl_locked_1.setImageDrawable(monster_lvl_1[1]);
            monster_lvl_locked_2.setImageDrawable(monster_lvl_2[1]);
            monster_lvl_locked_3.setImageDrawable(monster_lvl_3[1]);
            monster_lvl_locked_4.setImageDrawable(monster_lvl_4[1]);
        }else if(highScore > 410){
            monsterD = monster_lvl_3;
            monster_lvl_locked_3.setImageDrawable(monster_lvl_3[1]);
            monster_lvl_locked_2.setImageDrawable(monster_lvl_2[1]);
            monster_lvl_locked_1.setImageDrawable(monster_lvl_1[1]);
        }else if(highScore > 275){
            monsterD = monster_lvl_2;
            monster_lvl_locked_2.setImageDrawable(monster_lvl_2[1]);
            monster_lvl_locked_1.setImageDrawable(monster_lvl_1[1]);
        }else if(highScore > 140){
            monsterD = monster_lvl_1;
            monster_lvl_locked_1.setImageDrawable(monster_lvl_1[1]);
        }

        // Sets positions for the monster
        monsterI.setY(monsterSetHeight);
        monsterI.setX(displayWidth/2 - monsterI.getWidth());

        // Sets position for the meteorits
        rand = new Random();
        meteoritRed = new MeteoritRed[numberOfRedMeteorits];

        for(int i=0; i<numberOfRedMeteorits; i++){
            meteoritRed[i] = new MeteoritRed(rand.nextInt(displayWidthMeteoriteRed),rand.nextInt(150 ),meteoritRedView[i]);
            meteoritRed[i].setVELOCITY(VELOCITY);
        }

        // Sets invisible the yellow meteorits
        for(int i=0;i<numberOfYellowMeteorits;i++){
           meteoritYellowView[i].setVisibility(View.INVISIBLE);
        }

        // Sets invisible the blue meteorit
        meteoritBlueView.setVisibility(View.INVISIBLE);

        // Sets up the red sparkle
        sparkleRed = new SparkleRed(0,0,sparkleRedView);
        sparkleRedView.setVisibility(View.INVISIBLE);

        // Sets up the yellow sparkle
        sparkleYellow = new SparkleYellow(0,0,sparkleYellowView);
        sparkleYellowView.setVisibility(View.INVISIBLE);

        // Sets up the blue sparkle
        sparkleBlue = new SparkleBlue(0,0,sparkleBlueView);
        sparkleBlueView.setVisibility(View.INVISIBLE);

        // Begins the game, repeats the tasks in 50ms
        timer = new Timer();
        handler = new Handler();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!gameOver && !isPaused) {
                                changePos();
                                changeMonster();
                                scoreView.setText("" + score);

                                pause.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        setPause();
                                    }
                                });
                            }
                        }
                    });
            }
        },0,50);
    }

    // Sets the position of the monster wherever the finger moved
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            if(event.getY() >= 0) {
                monsterI.setX(event.getX()-monsterI.getWidth()/2);
            }
        }
        return true;
    }

    // Updates the position of the Red meteorits
    public void changePos(){
        for(int i=0; i<numberOfRedMeteorits;i++){
            meteoritRed[i].moveMeteorit();
            meteoritRedX = meteoritRed[i].getX();
            meteoritRedY = meteoritRed[i].getY();

            if(meteoritRedX > displayWidthMeteoriteRed){
                meteoritRedX = displayWidthMeteoriteRed;
                meteoritRed[i].setX(displayWidthMeteoriteRed);
            }
            if(meteoritRedX < 0){
                meteoritRedX = 0;
                meteoritRed[i].setX(0);
            }

            // checks if the meteorit is out of the world
            if(meteoritRedY >= monsterBottomLimit){
                gameOverAd();
            }

            // checks if the meterit has collided with the monster
            if(meteoritRedY >= displayDetectionBorder && collision(meteoritRedX, meteoritRedY, meteoritRedView[0],monsterI)){
                // repositions the meteorit on the top of the display
                meteoritRedX = rand.nextInt(displayWidthMeteoriteRed);
                meteoritRedY = rand.nextInt(150);
                while(meteoritRedY==0){
                    meteoritRedY = rand.nextInt(150);
                }
                meteoritRed[i].setX(meteoritRedX);
                meteoritRed[i].setY(meteoritRedY);
                score++;
                sparkleRedShow();
            }

            if(score%80 == 0 && score != 0) {
                meteoritRed[i].setVELOCITY(2*VELOCITY);
            }
            if(score%135 == 0 && score != 0){
                meteoritRed[i].setVELOCITY(VELOCITY);
            }

        }
        watchScore();
    }

    // Triggers actions in function of the value of the score
    public void watchScore(){
        // Checks the score for yellow meteorits
        if(score >= 40 && score%40 == 0 && !yellowM){
            generateYellowMeteorits();
            repeatYellowMeteorits();
            yellowM = true;
        }

        // Checks the score for blue meteorits
        if(score%200 == 0 && score != 0 && !blueM){
            repeatBlueMeteorits();
            blueM = true;
        }

        // Unlocks levels
        if(score >= 140 && score <= 160){
            changeLevel(monster_lvl_locked_1,monster_lvl_1[0], monster_lvl_1[1]);
        }

        if(score >= 275 && score <= 280){
            changeLevel(monster_lvl_locked_2,monster_lvl_2[0], monster_lvl_2[1]);
        }

        if(score >= 410 && score <= 460){
            changeLevel(monster_lvl_locked_3,monster_lvl_3[0], monster_lvl_3[1]);
        }

        if(score >= 545 && score <= 565){
            changeLevel(monster_lvl_locked_4,monster_lvl_4[0], monster_lvl_4[1]);
        }
    }

    // Sets up the yellow meteorites
    private void generateYellowMeteorits(){
        numberOfYellowMeteoritsActive = numberOfYellowMeteorits;
        meteoritYellow = new MeteoritYellow[numberOfYellowMeteorits];
        for(int i=0;i<numberOfYellowMeteorits;i++){
            meteoritYellowView[i].setVisibility(View.VISIBLE);
            meteoritYellow[i] = new MeteoritYellow(rand.nextInt(displayWidth-meteoritYellowView[0].getWidth()),rand.nextInt(150),meteoritYellowView[i]);
            meteoritYellow[i].setVelocity(2f*VELOCITY);
        }
    }

    // Sets a timer for the yellow meteorites
    public void repeatYellowMeteorits(){
        timerYellow = new Timer();
        handlerYellow = new Handler();
        timerYellow.schedule(new TimerTask() {
            @Override
            public void run() {
                handlerYellow.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!gameOver && !isPaused && yellowM) {
                           moveYellowMeteorits();
                        }
                    }
                });
            }
        },0,60);

    }

    // Changes the yellow meteorite's position
    private void moveYellowMeteorits(){
        for(int i=0;i<numberOfYellowMeteorits;i++) {
            meteoritYellow[i].moveMeteorit();
            meteoritYellowX = meteoritYellow[i].getX();
            meteoritYellowY = meteoritYellow[i].getY();

            if(meteoritYellowX > displayWidthMeteoriteRed){
                meteoritYellowX = displayWidthMeteoriteRed;
                meteoritYellow[i].setX(displayWidthMeteoriteRed);
            }
            if(meteoritYellowX < 0){
                meteoritYellowX = 0;
                meteoritYellow[i].setX(0);
            }

            // Checks if the meteorite is out of the world
            if(meteoritYellowY >= monsterBottomLimit){
                timerYellow.cancel();
                yellowM = false;
                gameOver = true;
                gameOverAd();
            }

            // Checks if the meteorites collides with the monster
            if (meteoritYellowY >= displayDetectionBorder && collision(meteoritYellowX, meteoritYellowY, meteoritYellowView[0], monsterI)) {
                meteoritYellowView[i].setVisibility(View.INVISIBLE);
                meteoritYellow[i].setY(0);
                numberOfYellowMeteoritsActive--;
                score+=5;
                sparkleYellowShow();
                if(numberOfYellowMeteoritsActive<=0){
                    timerYellow.cancel();
                    yellowM = false;
                }
            }
        }

    }

    private void repeatBlueMeteorits(){
        meteoritBlueView.setVisibility(View.VISIBLE);
        meteoritBlue = new MeteoritBlue(rand.nextInt(displayWidthMeteoriteRed), rand.nextInt(150), meteoritBlueView);
        meteoritBlue.setVelocity(2*VELOCITY);
        timerBlue = new Timer();
        handler = new Handler();
        timerBlue.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                       if(!gameOver && !isPaused) {
                           moveBlueMeteorits();
                       }
                    }
                });
            }
        },0,30);
    }

    private void moveBlueMeteorits(){
        meteoritBlue.moveMeteorit();
        meteoritBlueX = meteoritBlue.getX();
        meteoritBlueY = meteoritBlue.getY();

        if(meteoritBlueX > displayWidthMeteoriteRed){
            meteoritBlueX = displayWidthMeteoriteRed;
            meteoritBlue.setX(displayWidthMeteoriteRed);
        }
        if(meteoritBlueX < 0){
            meteoritBlueX = 0;
            meteoritBlue.setX(0);
        }

        // Checks if the meteorite is out of the world
        if(meteoritBlueY >= monsterBottomLimit){
            timerBlue.cancel();
            blueM = false;
            gameOver = true;
            gameOverAd();
        }

        // Checks if the meteorites collides with the monster
        if (meteoritBlueY >= displayDetectionBorder && collision(meteoritBlueX, meteoritBlueY, meteoritBlueView, monsterI)) {
            meteoritBlueView.setVisibility(View.INVISIBLE);
            meteoritBlue.setY(0);
            score+=20;
            sparkleBlueShow();
            timerBlue.cancel();
            blueM = false;
            // TODO: popup image for cogratulation
        }
    }
    // Changes the frame image of the monster to create the eat effect
    public void changeMonster(){
        if(frame == 1){
            frame = 0;
        }else {
            frame = 1;
        }

        monsterI.setImageDrawable(monsterD[frame]);
    }

    // Checks if the meteorit is colliding with the meteorit
    public boolean collision(float meteoritX, float meteoritY, ImageView meteorit, ImageView monsterI){
        if(meteoritX <= monsterI.getX() + monsterI.getWidth() && meteoritX+meteorit.getWidth() >= monsterI.getX() && meteoritY+meteorit.getHeight() >= monsterI.getY()){
            return true;
        }else {
            return false;
        }
    }

    // Shows the ad
    public void gameOverAd(){
       int r = rand.nextInt(4);
       if(mInterstitialAd.isLoaded() && r == 1){
           mInterstitialAd.show();
           Log.d("startGame", "ADS SHOWED");
       }else if(!mInterstitialAd.isLoaded()){
           mInterstitialAd.loadAd(new AdRequest.Builder().build());
           Log.d("startGame", "ADS FILED TO LOAD");
       }
       gameOver();
    }

    // Creates the gameover layout
    private void gameOver(){
        gameOver = true;
        gameOverLayout.setVisibility(View.VISIBLE);
        restartScoreView.setText(""+score);
        restartHighScoreView.setText(""+highScore);
        if(score > highScore){
            db.updateData(score);
        }
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartView();
            }
        });
    }

    // Restarts the game
    private void restartView(){
        gameOver = false;
        gameOverLayout.setVisibility(View.INVISIBLE);
        timer.cancel();
        startGame();
    }

    // Creates the pause layout
    public void setPause(){
        if(!gameOver) {
            isPaused = true;
            pauseLayout.setVisibility(View.VISIBLE);
            pauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // starts the game
                    pauseLayout.setVisibility(View.INVISIBLE);
                    isPaused = false;
                }
            });
        }
    }

    // Creates a red sparkle
    private void sparkleRedShow(){
        int monsterX = Math.round(monsterI.getX()) , monsterY = Math.round(monsterI.getY());
        int maxX = monsterX + monsterI.getWidth();
        int maxY = monsterY + 50;
        sparkleRedView.setVisibility(View.VISIBLE);
        sparkleRed.setX(rand.nextInt(maxX - monsterX+1)+monsterX);
        sparkleRed.setY(rand.nextInt(maxY- monsterY+1)+monsterY-50);

        sparkleRI = 0;
        final Timer timer = new Timer();
        final Handler handler = new Handler();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        sparkleRed.SparkleMove();
                        sparkleRI++;
                        if(sparkleRI >=60){
                            sparkleRedView.setVisibility(View.INVISIBLE);
                            timer.cancel();

                        }
                    }
                });
            }
        },0,50);
    }

    // Create yellow sparkle
    private void sparkleYellowShow(){
        int monsterX = Math.round(monsterI.getX()) , monsterY = Math.round(monsterI.getY());
        int maxX = monsterX + monsterI.getWidth();
        int maxY = monsterY + 50;
        sparkleYellowView.setVisibility(View.VISIBLE);
        sparkleYellow.setX(rand.nextInt(maxX - monsterX+1)+monsterX);
        sparkleYellow.setY(rand.nextInt(maxY- monsterY+1)+monsterY-50);

        sparkleYI = 0;
        final Timer timer = new Timer();
        final Handler handler = new Handler();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        sparkleYellow.SparkleMove();
                        sparkleYI++;
                        if(sparkleYI >=60){
                            sparkleYellowView.setVisibility(View.INVISIBLE);
                            timer.cancel();

                        }
                    }
                });
            }
        },0,50);
    }

    // Create blue sparkle
    private void sparkleBlueShow(){
        int monsterX = Math.round(monsterI.getX()) , monsterY = Math.round(monsterI.getY());
        int maxX = monsterX + monsterI.getWidth();
        int maxY = monsterY + 50;
        sparkleBlueView.setVisibility(View.VISIBLE);
        sparkleBlue.setX(rand.nextInt(maxX - monsterX+1)+monsterX);
        sparkleBlue.setY(rand.nextInt(maxY- monsterY+1)+monsterY-50);

        sparkleBI = 0;
        final Timer timer = new Timer();
        final Handler handler = new Handler();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        sparkleBlue.SparkleMove();
                        sparkleBI++;
                        if(sparkleBI >=60){
                            sparkleBlueView.setVisibility(View.INVISIBLE);
                            timer.cancel();

                        }
                    }
                });
            }
        },0,50);
    }

    //Change level
    private void changeLevel(ImageView levelImage, Drawable levelImageUnlocked1, Drawable levelImageUnlocked2){
        levelImage.setImageDrawable(levelImageUnlocked2);
        monsterD[0] = levelImageUnlocked1;
        monsterD[1] = levelImageUnlocked2;
    }
}
