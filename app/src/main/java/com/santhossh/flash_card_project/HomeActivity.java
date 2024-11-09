package com.santhossh.flash_card_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private FlashcardAdapter adapter;
    private ArrayList<Flashcard> flashcards;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.addButton);

        flashcards = new ArrayList<>();
        adapter = new FlashcardAdapter(this,flashcards);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();

        loadFlashcards();

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddFlashcardActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload flashcards every time the activity is resumed
        loadFlashcards();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadFlashcards(); // Reload flashcards when returning from AddFlashcardActivity
        }
    }


    private void loadFlashcards() {
        firestore.collection("flashcards").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    flashcards.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Flashcard flashcard = document.toObject(Flashcard.class);
                        flashcards.add(flashcard);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HomeActivity.this, "Error loading flashcards", Toast.LENGTH_SHORT).show();
                });
    }
}
