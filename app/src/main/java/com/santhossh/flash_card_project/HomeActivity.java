package com.santhossh.flash_card_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private FlashcardAdapter adapter;
    private ArrayList<Flashcard> flashcards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.addButton);

        flashcards = new ArrayList<>();
        adapter = new FlashcardAdapter(flashcards, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadFlashcards();

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, EditFlashcardActivity.class);
            startActivity(intent);
        });
    }

    private void loadFlashcards() {
        FirebaseDatabase.getInstance().getReference("flashcards")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        flashcards.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Flashcard flashcard = snapshot.getValue(Flashcard.class);
                            flashcards.add(flashcard);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(HomeActivity.this, "Error loading flashcards", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
