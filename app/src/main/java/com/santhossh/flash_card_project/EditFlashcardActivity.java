package com.santhossh.flash_card_project;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class EditFlashcardActivity extends AppCompatActivity {

    private EditText titleField, questionField, answerField;
    private FirebaseFirestore firestore;
    private String flashcardId;  // To hold the passed document ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flashcard);

        titleField = findViewById(R.id.titleField);
        questionField = findViewById(R.id.questionField);
        answerField = findViewById(R.id.answerField);
        firestore = FirebaseFirestore.getInstance();

        // Get the document ID passed from the previous activity
        flashcardId = getIntent().getStringExtra("flashcard_id");

        // Load the existing data for editing
        if (flashcardId != null) {
            loadFlashcardData();
        }

        // Set the click listener for the save button
        findViewById(R.id.saveButton).setOnClickListener(v -> saveFlashcard());
    }

    private void loadFlashcardData() {
        // Retrieve the flashcard data from Firestore using the document ID
        firestore.collection("flashcards").document(flashcardId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Set the current data in the edit fields
                        titleField.setText(documentSnapshot.getString("title"));
                        questionField.setText(documentSnapshot.getString("question"));
                        answerField.setText(documentSnapshot.getString("answer"));
                    } else {
                        Toast.makeText(this, "Flashcard not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading flashcard data", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveFlashcard() {
        String title = titleField.getText().toString().trim();
        String question = questionField.getText().toString().trim();
        String answer = answerField.getText().toString().trim();

        if (!title.isEmpty() && !question.isEmpty() && !answer.isEmpty()) {
            Map<String, Object> updates = new HashMap<>();
            updates.put("title", title);
            updates.put("question", question);
            updates.put("answer", answer);

            // Update the flashcard in Firestore using the document ID
            firestore.collection("flashcards")
                    .document(flashcardId)  // Use the passed flashcard ID
                    .update(updates)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Flashcard updated!", Toast.LENGTH_SHORT).show();
                        finish();  // Close the activity and return to the previous screen
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error updating flashcard", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
        }
    }
}
