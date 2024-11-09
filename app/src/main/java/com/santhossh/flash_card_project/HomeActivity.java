package com.santhossh.flash_card_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private Button viewFlashcardsButton;
    private FlashcardAdapter adapter;
    private ArrayList<Flashcard> flashcards;
    private FirebaseFirestore firestore;

    private final ActivityResultLauncher<Intent> addFlashcardLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadFlashcards();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.addButton);
        viewFlashcardsButton = findViewById(R.id.viewFlashcardsButton);

        flashcards = new ArrayList<>();
        adapter = new FlashcardAdapter(this, flashcards);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();

        loadFlashcards();

        viewFlashcardsButton.setOnClickListener(v -> openRandomFlashcard());

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddFlashcardActivity.class);
            addFlashcardLauncher.launch(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFlashcards();
    }

    private void loadFlashcards() {
        firestore.collection("flashcards")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Toast.makeText(HomeActivity.this, "Error loading flashcards", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    flashcards.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Flashcard flashcard = document.toObject(Flashcard.class);
                        flashcards.add(flashcard);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void openRandomFlashcard() {
        if (flashcards.isEmpty()) {
            Toast.makeText(this, "No flashcards available", Toast.LENGTH_SHORT).show();
            return;
        }
        Flashcard randomFlashcard = flashcards.get(new Random().nextInt(flashcards.size()));

        Intent intent = new Intent(HomeActivity.this, ViewFlashcardActivity.class);
        intent.putExtra("title", randomFlashcard.getTitle());
        intent.putExtra("question", randomFlashcard.getQuestion());
        intent.putExtra("answer", randomFlashcard.getAnswer());
        startActivity(intent);
    }
}
