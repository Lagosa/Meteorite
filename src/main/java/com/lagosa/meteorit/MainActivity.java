package com.lagosa.meteorit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private Button playButton;
    public TextView highScoreView;
    public int highScore;

    // Database stuff
    DatabaseHelper databaseHelper = new DatabaseHelper(this,null,null,1);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        highScore = databaseHelper.getData();

        playButton = findViewById(R.id.start_button);
        highScoreView = findViewById(R.id.high_score);

        highScoreView.setText(""+highScore);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
            }
        });
    }
    public void startNewGame() {
        Intent intent = new Intent(this, StartGame.class);
        startActivity(intent);
        finish();
    }

    public long getHighScore() {
        return highScore;
    }

    public void setHighScore(int score) {
        this.highScore = score;
    }

}
