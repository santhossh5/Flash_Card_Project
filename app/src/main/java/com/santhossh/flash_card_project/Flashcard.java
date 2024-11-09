package com.santhossh.flash_card_project;

public class Flashcard {
    private String title;
    private String question;
    private String answer;

    public Flashcard() {
        // Default constructor required for Firestore document conversion
    }

    public Flashcard(String title, String question, String answer) {
        this.title = title;
        this.question = question;
        this.answer = answer;
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
}


