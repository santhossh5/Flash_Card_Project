package com.santhossh.flash_card_project;

public class Flashcard {
    private String question;
    private String answer;

    public Flashcard() {
        // Default constructor required for calls to DataSnapshot.getValue(Flashcard.class)
    }

    public Flashcard(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
