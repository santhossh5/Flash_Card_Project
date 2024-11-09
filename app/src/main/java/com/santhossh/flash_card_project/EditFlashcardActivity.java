package com.santhossh.flash_card_project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditFlashcardActivity extends AppCompatActivity {
    private EditText questionField, answerField;
    private Button saveButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flashcard);

        questionField = findViewById(R.id.questionField);
        answerField = findViewById(R.id.answerField);
        saveButton = findViewById(R.id.saveButton);
        databaseReference = FirebaseDatabase.getInstance().getReference("flashcards");

        saveButton.setOnClickListener(v -> {
            String question = questionField.getText().toString().trim();
            String answer = answerField.getText().toString().trim();
            if (!question.isEmpty() && !answer.isEmpty()) {
                saveFlashcard(new Flashcard(question, answer));
            }
        });
    }

    private void saveFlashcard(Flashcard flashcard) {
        databaseReference.push().setValue(flashcard).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Flashcard saved!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error saving flashcard", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

