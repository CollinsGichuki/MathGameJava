package com.example.android.mathgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameOver extends AppCompatActivity {
    TextView tvPoints;
    ImageView ivHighScore;
    TextView tvBestScore;
    //Use SharedPreference to store the score
    SharedPreferences sharedPreferences ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        getSupportActionBar().hide();//Made the view full screen

        //Get the points from the Start Game Activity
        int points = getIntent().getExtras().getInt("points");
        //Initialize the text view
        tvPoints = findViewById(R.id.tvPoints);

        ivHighScore = findViewById(R.id.ivHighScore);
        tvBestScore = findViewById(R.id.tvBestScore);

        sharedPreferences = getSharedPreferences("pref", 0);
        int pointsSharedPreference= sharedPreferences.getInt("pointsSP", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //If points>pointsSharedPreference, that becomes the new high score and is stored as the new shared preference value
        if (points > pointsSharedPreference){
            pointsSharedPreference = points;
            editor.putInt("pointsSP", pointsSharedPreference);
            //Commit the new sharedPref stored
            editor.apply();
            ivHighScore.setVisibility(View.VISIBLE);
        }
        //Show the current score
        tvPoints.setText(""+points);
        //Show the best score
        tvBestScore.setText(""+pointsSharedPreference);
    }

    public void restart(View view) {
        Intent intent = new Intent(GameOver.this, StartGame.class);
        startActivity(intent);
        finish();//Finish the activity
    }

    public void exit(View view) {
        finishAffinity();//Quit the app
    }
}
