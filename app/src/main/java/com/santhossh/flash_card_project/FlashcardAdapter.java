package com.santhossh.flash_card_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardAdapter.ViewHolder> {
    private List<Flashcard> flashcards;
    private Context context;

    public FlashcardAdapter(List<Flashcard> flashcards, Context context) {
        this.flashcards = flashcards;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flashcard_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Flashcard flashcard = flashcards.get(position);
        holder.questionText.setText(flashcard.getQuestion());

        holder.itemView.setOnClickListener(v -> {
            holder.flipView();
        });
    }

    @Override
    public int getItemCount() {
        return flashcards.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionText, answerText;
        boolean isFront = true;

        ViewHolder(View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.questionText);
            answerText = itemView.findViewById(R.id.answerText);
        }

        void flipView() {
            if (isFront) {
                questionText.setVisibility(View.GONE);
                answerText.setVisibility(View.VISIBLE);
            } else {
                questionText.setVisibility(View.VISIBLE);
                answerText.setVisibility(View.GONE);
            }
            isFront = !isFront;
        }
    }
}
