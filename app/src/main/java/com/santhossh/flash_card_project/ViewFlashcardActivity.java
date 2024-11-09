package com.santhossh.flash_card_project;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewFlashcardActivity extends AppCompatActivity {

    private MaterialCardView cardView;
    private TextView questionTextView, answerTextView;
    private Button markKnownButton;
    private String question, answer, title;
    private boolean isFront = true;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_flashcard);

        cardView = findViewById(R.id.cardView);
        questionTextView = findViewById(R.id.questionTextView);
        answerTextView = findViewById(R.id.answerTextView);
        markKnownButton = findViewById(R.id.markKnownButton);

        firestore = FirebaseFirestore.getInstance();

        question = getIntent().getStringExtra("question");
        answer = getIntent().getStringExtra("answer");
        title = getIntent().getStringExtra("title");

        questionTextView.setText(question);
        answerTextView.setText(answer);
        answerTextView.setVisibility(View.GONE);

        cardView.setOnClickListener(v -> flipCard());

        markKnownButton.setOnClickListener(v -> markAsKnown());
    }

    private void flipCard() {
        if (isFront) {
            ObjectAnimator.ofFloat(cardView, "rotationY", 0f, 180f).setDuration(500).start();
            answerTextView.setVisibility(View.VISIBLE);
            questionTextView.setVisibility(View.INVISIBLE);
            answerTextView.setScaleX(-1f);
        } else {
            ObjectAnimator.ofFloat(cardView, "rotationY", 180f, 360f).setDuration(500).start();
            answerTextView.setVisibility(View.INVISIBLE);
            questionTextView.setVisibility(View.VISIBLE);
            answerTextView.setScaleX(1f);
        }
        isFront = !isFront;
    }

    private void markAsKnown() {
        firestore.collection("flashcards").whereEqualTo("title", title).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                        firestore.collection("flashcards").document(documentId)
                                .update("isMarked", true)
                                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Marked as known", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(this, "Error marking as known", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Flashcard not found", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
