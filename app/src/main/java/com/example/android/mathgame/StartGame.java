package com.example.android.mathgame;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class StartGame extends AppCompatActivity {
    int operand1, operand2, correctAnswer, incorrectAnswer;
    long millisUntilFinished;
    int points, numberOfQuestions, correctAnswerPosition;

    //Array to hold the ids of the buttons
    int[] btnIds;

    //Array List of incorrect answers
    ArrayList<Integer> incorrectAnswers;

    //String Array for the math operators
    String[] operatorArray;

    TextView tvSum, tvPoints, tvTimer, tvResult;
    CountDownTimer countDownTimer;
    Button btn0, btn1, btn2, btn3;
    Random random;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_game);
        getSupportActionBar().hide();//Made the view full screen

        //Initialize the variables
        operand1 = 0;
        operand2 = 0;
        correctAnswer = 0;
        points = 0;
        numberOfQuestions = 0;
        btnIds = new int[]{R.id.button0, R.id.button1, R.id.button2, R.id.button3};
        correctAnswerPosition = 0;
        incorrectAnswers = new ArrayList<>();
        /*
        To write division symbol
        1. Turn Num lock on
        2. Hold alt while pressing 0247 on the num keypad
        */
        operatorArray = new String[]{"+", "-", "*", "÷"};

        tvSum = findViewById(R.id.tvSum);
        tvPoints = findViewById(R.id.tvPoints);
        tvTimer = findViewById(R.id.tvTimer);
        tvResult = findViewById(R.id.tvResult);
        btn0 = findViewById(R.id.button0);
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);

        //30 seconds + 100 milliseconds more for the activity to load
        millisUntilFinished = 30100;
        random = new Random();

        startGame();
    }

    private void startGame() {
        //Populating the timer
        tvTimer.setText("" + (millisUntilFinished / 1000) + "s");
        //Populating the points
        tvPoints.setText("" + (points + "/" + numberOfQuestions));

        generateQuestions();

        countDownTimer = new CountDownTimer(millisUntilFinished, 1000) {
            //Called regularly with a 1 second interval
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("" + (millisUntilFinished / 1000) + "s");
            }

            //When the time is up
            @Override
            public void onFinish() {
                //Set the buttons to not clickable
                btn0.setClickable(false);
                btn1.setClickable(false);
                btn2.setClickable(false);
                btn3.setClickable(false);
                //Go to game over screen with the points when timer is finished
                Intent intent = new Intent(StartGame.this, GameOver.class);
                intent.putExtra("points", points);
                startActivity(intent);
                finish();//Don't return back to this activity from the Game Over Activity
            }
        }.start(); //Start the countdown
    }

    private void generateQuestions() {
        //increment the number of questions
        numberOfQuestions++;
        operand1 = random.nextInt(10);
        //Since there is division, we don't want a 0 hence 1-9
        operand2 = 1 + random.nextInt(9);
        /*
        String to hold the selected operator
        operatorArray receives a random number between 0-3
        */
        String selectedOperator = operatorArray[random.nextInt(4)];
        correctAnswer = getAnswer(selectedOperator);
        tvSum.setText(operand1 + " "+ selectedOperator+ " " + operand2 + " = ");
        //The correct answer position generated can be from 0-3
        correctAnswerPosition = random.nextInt(4);
        //Setting the sum
        ((Button) findViewById(btnIds[correctAnswerPosition])).setText("" + correctAnswer);
        while (true) {
            if (incorrectAnswers.size() > 3) {
                break;//Break from the loop
            }
            operand1 = random.nextInt(10);
            operand2 = 1 + random.nextInt(9);
            selectedOperator = operatorArray[random.nextInt(4)];
            incorrectAnswer = getAnswer(selectedOperator);
            //If 
            if (incorrectAnswer == correctAnswer) {
                continue;//continue with the loop
            }
            incorrectAnswers.add(incorrectAnswer);
        }
        //Display the incorrect answers in the corresponding 3 buttons
        for (int i = 0; i < 3; i++) {
            if (i == correctAnswerPosition) {
                continue; //continue with the loop
            }
            ((Button) findViewById(btnIds[i])).setText("" + incorrectAnswers.get(i));
        }
        incorrectAnswers.clear();
    }

    private int getAnswer(String selectedOperator) {
        int answer = 0;
        switch (selectedOperator){
            case "+" :
                answer = operand1 + operand2;
                break;
            case "-" :
                answer = operand1 - operand2;
                break;
            case "*" :
                answer = operand1 * operand2;
                break;
            case "÷" :
                answer = operand1 / operand2;
                break;
        }
        return answer;
    }

    public void chooseAnswer(View view) {
        int answer = Integer.parseInt(((Button) view).getText().toString());
        if (answer == correctAnswer) {
            points++;
            tvResult.setText("Correct!");
        } else {
            tvResult.setText("Incorrect!");
        }
        tvPoints.setText(points + "/" + numberOfQuestions);
        generateQuestions();
    }
}
