package edu.orangecoastcollege.cs273.balbert.cs273superheroes;

import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = QuizActivity.class.getSimpleName();

    private static final int HEROES_IN_QUIZ = 10;

    private Button[] mButtons = new Button[4];
    private List<Superhero> mAllHeroesList;
    private List<Superhero> mQuizHeroesList;
    private Superhero mCorrectHero;
    private int mTotalGuesses;
    private int mCorrectGuesses;
    private SecureRandom rng;
    private Handler handler;

    private TextView mQuestionNumberTextView;
    private ImageView mHeroImageView;
    private TextView mQuestionTypeTextView;
    private TextView mAnswerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQuizHeroesList = new ArrayList<>(HEROES_IN_QUIZ);
        rng = new SecureRandom();
        handler = new Handler();

        mQuestionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        mHeroImageView = (ImageView) findViewById(R.id.heroImageView);
        mQuestionTypeTextView = (TextView) findViewById(R.id.questionTypeTextView);
        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);

        mButtons[0] = (Button) findViewById(R.id.button);
        mButtons[1] = (Button) findViewById(R.id.button2);
        mButtons[2] = (Button) findViewById(R.id.button3);
        mButtons[3] = (Button) findViewById(R.id.button4);

        mQuestionNumberTextView.setText(getString(R.string.question, 1, HEROES_IN_QUIZ));
        mQuestionTypeTextView.setText(getString(R.string.guess_option));

        try {
            mAllHeroesList = JSONLoader.loadJSONFromAsset(this);
        } catch (IOException e) {
            Log.e(TAG, "Error loading JSON file", e);
        }

        resetQuiz();
    }

    public void resetQuiz() {
        mCorrectGuesses = 0;
        mTotalGuesses = 0;
        mQuizHeroesList.clear();

        int numberOfHeroes = mAllHeroesList.size();
        int size = 0;
        Superhero randomHero;
        while ( size < HEROES_IN_QUIZ ) {
            randomHero = mAllHeroesList.get(rng.nextInt(numberOfHeroes));
            if ( ! mQuizHeroesList.contains(randomHero) ) {
                mQuizHeroesList.add(randomHero);
                size++;
            }
        }

        loadNextHero();
    }

    private void loadNextHero() {
        mCorrectHero = mQuizHeroesList.remove(0);
        mAnswerTextView.setText("");
        int questionNumber = HEROES_IN_QUIZ - mQuizHeroesList.size();
        mQuestionNumberTextView.setText(getString(R.string.question, questionNumber, HEROES_IN_QUIZ));

        AssetManager am = getAssets();

        try {
            InputStream stream = am.open(mCorrectHero.getFileName());
            Drawable image =  Drawable.createFromStream(stream, mCorrectHero.getName());
            mHeroImageView.setImageDrawable(image);
        } catch (IOException e) {
            Log.e(TAG, "Error loading image: " + mCorrectHero.getFileName(), e);
        }

        do {
            Collections.shuffle(mAllHeroesList);
        } while (mAllHeroesList.subList(0, mButtons.length).contains(mCorrectHero));

        for (int i = 0; i < mButtons.length; ++i) {
            mButtons[i].setEnabled(true);
            mButtons[i].setText(mAllHeroesList.get(i).getName());
        }

        mButtons[rng.nextInt(mButtons.length)].setText(mCorrectHero.getName());
    }

    public void makeGuess(View v) {
        Button clickButton = (Button) v;
        String guess = clickButton.getText().toString();
        mTotalGuesses++;

        if (guess.equals(mCorrectHero.getName())) {
            for (Button b: mButtons)
                b.setEnabled(false);
            mCorrectGuesses++;
            mAnswerTextView.setText(mCorrectHero.getName() + "!");
            mAnswerTextView.setTextColor(ContextCompat.getColor(this, R.color.correct_answer));

            if (mCorrectGuesses < HEROES_IN_QUIZ) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextHero();
                    }
                }, 2000);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.results, mTotalGuesses, ((double) mCorrectGuesses / mTotalGuesses)*100 ));
                builder.setPositiveButton(getString(R.string.reset_quiz), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resetQuiz();
                    }
                });

                builder.setCancelable(false);
                builder.create();
                builder.show();
            }
        } else {
            mAnswerTextView.setText(getString(R.string.incorrect_answer));
            mAnswerTextView.setTextColor(ContextCompat.getColor(this, R.color.incorrect_answer));
            clickButton.setEnabled(false);
        }
    }
}
