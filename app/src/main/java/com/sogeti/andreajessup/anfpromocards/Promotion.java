package com.sogeti.andreajessup.anfpromocards;

/**
 * Created by andreajessup on 10/5/15.
 */
public class Promotion {
    private Button button;
    private String description;
    private String footer;
    private String imageURLString;
    private String title;

    public Promotion() {}

    public Button getButton() {
        return this.button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFooter() {
        return  footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getImageURLString() {
        return  this.imageURLString;
    }

    public void setImageURLString(String imageURLString) {
        this.imageURLString = imageURLString;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
