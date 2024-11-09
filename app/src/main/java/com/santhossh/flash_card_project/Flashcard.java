package com.santhossh.flash_card_project;

public class Flashcard {
    private String title;
    private String question;
    private String answer;
    private boolean isMarked; // New field to indicate if the flashcard is marked

    public Flashcard() {
        // Default constructor required for Firestore document conversion
    }

    public Flashcard(String title, String question, String answer, boolean isMarked) {
        this.title = title;
        this.question = question;
        this.answer = answer;
        this.isMarked = isMarked;
    }

    public String getTitle() {
        return title;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }
}
