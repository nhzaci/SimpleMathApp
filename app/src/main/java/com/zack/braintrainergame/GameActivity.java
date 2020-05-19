package com.zack.braintrainergame;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    TextView timeCountDown;
    TextView scoreText;
    TextView questionText;
    TextView questionCounter;
    GridLayout answerMatrix;
    TextView checkAnswerText;
    Button playAgainButton;
    TextView outOfTime;
    CountDownTimer countDown;
    boolean isActive;
    int answerIndex;
    int qnCount = 0;
    int score = 0;
    String correctText = "Correct!";
    String wrongText = "That is not correct...";
    ArrayList<Integer> questionArray = new ArrayList<>(Arrays.asList(3,5,6,7,4,8,9));
    ArrayList<Integer> answerArray = new ArrayList<>(Arrays.asList(32,12,53,50,23,45,21,57,23,10,6,5,7,9));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        timeCountDown = findViewById(R.id.timeCountDown);
        questionText = findViewById(R.id.questionText);
        questionCounter = findViewById(R.id.questionCounter);
        answerMatrix = findViewById(R.id.answerMatrix);
        checkAnswerText = findViewById(R.id.answerCheckText);
        playAgainButton = findViewById(R.id.playAgainButton);
        outOfTime = findViewById(R.id.outOfTime);
        scoreText = findViewById(R.id.scoreText);

        startGame();
    }

    private void startGame() {
        //Hide useless buttons
        hideView(playAgainButton);
        hideView(outOfTime);
        hideView(checkAnswerText); //hide first before first qn is answered

        isActive = true; //set game state to active

        //start timer -- onTick, set remaining time
        startTimer();
        getNextQuestionAnswer();
    }

    public void checkAnswer(View view) {
        if (view.getTag().toString().equals(Integer.toString(answerIndex))) {
            checkAnswerText.setText(correctText);
            score += 1;
        } else {
            checkAnswerText.setText(wrongText);
        }
        scoreText.setText(Integer.toString(score));
        showView(checkAnswerText);
        getNextQuestionAnswer();
    }

    private void getNextQuestionAnswer() {
        if (qnCount == 20) {
            endGame();
        } else {
            Random random = new Random();
            // 0 is addition, 1 is subtraction, 2 is multiplication and 3 is divide
            int questionType = random.nextInt(4); // get question type
            answerIndex = random.nextInt(4); // index of correct option
            int firstQnInt = questionArray.get(random.nextInt(questionArray.size()));
            int secondQnInt = questionArray.get(random.nextInt(questionArray.size()));
            String operation = "";
            int answer = 0;
            switch(questionType) {
                case 0:
                    //addition
                    answer = firstQnInt + secondQnInt;
                    operation = "+";
                    break;
                case 1:
                    //subtraction
                    answer = firstQnInt - secondQnInt;
                    operation = "-";
                    break;
                case 2:
                    //multiplication
                    answer = firstQnInt * secondQnInt;
                    operation = "x";
                    break;
                case 3:
                    //division
                    answer = firstQnInt / secondQnInt;
                    operation = "/";
                    break;
            }
            //Set up options in grid layout
            for (int i = 0; i < answerMatrix.getChildCount(); i++) {
                Button childButton = (Button) answerMatrix.getChildAt(i);
                if (childButton.getTag().toString().equals(Integer.toString(answerIndex))) {
                    childButton.setText(String.valueOf(answer));
                } else {
                    Integer randomValue = answerArray.get(random.nextInt(answerArray.size()));
                    if (randomValue == answer) {
                        randomValue += random.nextInt(5);
                    }
                    childButton.setText(String.valueOf(randomValue));
                }
            }
            //Set qn text
            questionText.setText(String.format(Locale.ENGLISH, "%d %s %d", firstQnInt, operation, secondQnInt));
            //Set qn counter
            String qnCounterTxt = String.format(Locale.ENGLISH, "%d/20", qnCount);
            questionCounter.setText(qnCounterTxt);
            qnCount++;
        }
    }

    private void endGame() {
        countDown.cancel();
        countDown.onFinish();
    }

    private void startTimer() {
        countDown = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //amend timeCountDown on every tick
                timeCountDown.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                //Disable answer buttons from being pressed
                for (int i = 0; i < answerMatrix.getChildCount(); i++) {
                    answerMatrix.getChildAt(i).setEnabled(false);
                }
                //Show outOfTime text and hide checkAnswerText
                showView(outOfTime);
                hideView(checkAnswerText);
                //Show playAgain button
                showView(playAgainButton);
                //Set gamestate to inactive
                isActive = false;
            }
        }.start();
    }

    public void playAgain(View view) {
        //to be executed by playAgainButton
        qnCount = 0;
        score = 0;
        scoreText.setText(Integer.toString(score));
        for (int i = 0; i < answerMatrix.getChildCount(); i++) {
            answerMatrix.getChildAt(i).setEnabled(true);
        }
        startGame();
    }

    private void hideView(View view) {
        view.setEnabled(false);
        view.setAlpha(0.0f);
    }

    private void showView(View view) {
        view.setEnabled(true);
        view.setAlpha(1.0f);
    }

}
