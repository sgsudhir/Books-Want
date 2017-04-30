package com.imca2017.bookswant.pojo;

/**
 * Created by sgsudhir on 30/4/17.
 */

public class RecyclerDataModel {
    private String title;
    private String author;
    private String publisher;
    private String imageUrl;

    public RecyclerDataModel(String title, String author, String publisher, String imageUrl) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.imageUrl = imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

}
