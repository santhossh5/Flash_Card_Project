package com.santhossh.flash_card_project;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class FlashcardDetailActivity extends AppCompatActivity {

    private CardView cardView;
    private TextView questionTextView, answerTextView;
    private boolean isFront = true;  // To track if the card is showing the question or answer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_detail);

        cardView = findViewById(R.id.cardView);
        questionTextView = findViewById(R.id.questionTextView);
        answerTextView = findViewById(R.id.answerTextView);

        // Get the question and answer passed from the previous activity
        String question = getIntent().getStringExtra("question");
        String answer = getIntent().getStringExtra("answer");

        questionTextView.setText(question);
        answerTextView.setText(answer);

        // Initially, show the question and hide the answer
        answerTextView.setVisibility(View.INVISIBLE);

        // Set the onClickListener to trigger the flip animation
        cardView.setOnClickListener(v -> flipCard());
    }

    private void flipCard() {
        if (isFront) {
            // Show the answer, hide the question
            ObjectAnimator.ofFloat(cardView, "rotationY", 0f, 180f).setDuration(500).start();
            answerTextView.setVisibility(View.VISIBLE);
            questionTextView.setVisibility(View.INVISIBLE);

            // Apply negative scale on the answer to counteract the mirrored effect
            answerTextView.setScaleX(-1f);  // Flip horizontally
        } else {
            // Show the question, hide the answer
            ObjectAnimator.ofFloat(cardView, "rotationY", 180f, 360f).setDuration(500).start();
            answerTextView.setVisibility(View.INVISIBLE);
            questionTextView.setVisibility(View.VISIBLE);

            // Reset the scale for the question text
            answerTextView.setScaleX(1f);  // Reset flip effect
        }
        isFront = !isFront;  // Toggle the state between showing question or answer
    }

}
