// Tolman Benjamin
// JAV2 - C202101 02
// Book.java

package com.example.googlebooksapiandfileiodemo.objects;

import java.io.Serializable;

public class Book implements Serializable {

    private final String title;
    private final String publisher;
    private final String subject;

    public Book(String title, String author, String subject) {
        this.title = title;
        this.publisher = author;
        this.subject = subject;
    }

    @Override
    public String toString() {
        return title + "\n" + publisher;
    }

    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getSubject() {
        return subject;
    }
}
