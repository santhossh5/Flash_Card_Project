package com.santhossh.flash_card_project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardAdapter.FlashcardViewHolder> {

    private List<Flashcard> flashcardList;
    private Context context;
    private FirebaseFirestore firestore;

    public FlashcardAdapter(Context context, List<Flashcard> flashcardList) {
        this.context = context;
        this.flashcardList = flashcardList;
        this.firestore = FirebaseFirestore.getInstance();  // Initialize Firestore instance
    }

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.flashcard_item, parent, false);
        return new FlashcardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        Flashcard flashcard = flashcardList.get(position);
        holder.titleTextView.setText(flashcard.getTitle());

        // Set the click listener for navigating to the detail activity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FlashcardDetailActivity.class);
            intent.putExtra("question", flashcard.getQuestion());
            intent.putExtra("answer", flashcard.getAnswer());
            intent.putExtra("title", flashcard.getTitle());
            context.startActivity(intent);
        });

        // Set Edit button functionality
        holder.itemView.findViewById(R.id.editButton).setOnClickListener(v -> {
            // Pass the Firestore document ID to Edit activity
            Intent intent = new Intent(context, EditFlashcardActivity.class);
            intent.putExtra("flashcard_id", flashcard.getTitle()); // Pass the document ID for editing
            context.startActivity(intent);
        });

        // Set Delete button functionality
        holder.itemView.findViewById(R.id.deleteButton).setOnClickListener(v -> {
            String title = flashcard.getTitle(); // Get the title of the flashcard

            // Query Firestore to find the document with the matching title
            firestore.collection("flashcards")
                    .whereEqualTo("title", title)  // Find the document by title
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // There should be only one document with this title, so we get the first result
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                String documentId = document.getId();  // Get the document ID

                                // Now delete the document
                                firestore.collection("flashcards").document(documentId)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> {
                                            flashcardList.remove(position);  // Remove the flashcard from the list
                                            notifyItemRemoved(position);  // Notify the adapter about the change
                                            Toast.makeText(context, "Flashcard deleted", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(context, "Error deleting flashcard", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        } else {
                            Toast.makeText(context, "Flashcard not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error searching for flashcard", Toast.LENGTH_SHORT).show();
                    });
        });

    }

    @Override
    public int getItemCount() {
        return flashcardList.size();
    }

    public static class FlashcardViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        public FlashcardViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }
    }
}
