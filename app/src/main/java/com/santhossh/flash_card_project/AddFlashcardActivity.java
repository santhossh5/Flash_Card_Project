package com.santhossh.flash_card_project;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AddFlashcardActivity extends AppCompatActivity {

    private EditText titleField, questionField, answerField;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flashcard);

        titleField = findViewById(R.id.titleField);
        questionField = findViewById(R.id.questionField);
        answerField = findViewById(R.id.answerField);
        firestore = FirebaseFirestore.getInstance();

        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFlashcard();
            }
        });
    }

    private void saveFlashcard() {
        String title = titleField.getText().toString().trim();
        String question = questionField.getText().toString().trim();
        String answer = answerField.getText().toString().trim();

        if (!title.isEmpty() && !question.isEmpty() && !answer.isEmpty()) {
            Map<String, Object> flashcard = new HashMap<>();
            flashcard.put("title", title);
            flashcard.put("question", question);
            flashcard.put("answer", answer);
            flashcard.put("isMarked", false); // Set isMarked to false by default

            firestore.collection("flashcards").add(flashcard)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Flashcard saved!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error saving flashcard", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
        }
    }
}
