package com.sogeti.andreajessup.anfpromocards;

/**
 * Created by andreajessup on 10/5/15.
 */
public class Button {
    private String targetURLString;
    private String title;

    public Button() {}

    public String getTargetURLString() {
        return  this.targetURLString;
    }

    public void  setTargetURLString(String targetURLString) {
        this.targetURLString = targetURLString;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
