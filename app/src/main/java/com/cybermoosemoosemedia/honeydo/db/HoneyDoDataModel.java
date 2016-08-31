package com.cybermoosemoosemedia.honeydo.db;

public class HoneyDoDataModel {
    private int mId;
    private String mContent;
    private int mImportant;
    private String mYear;

    //Data model for HoneyDo DB

    public HoneyDoDataModel(int id, String content, int important, String year) {
        mId = id;
        mImportant = important;
        mContent = content;
        mYear = year;
    }
    public int getId() {
        return mId;
    }
    public void setId(int id) {
        mId = id;
    }
    public int getImportant() {
        return mImportant;
    }
    public void setImportant(int important) {
        mImportant = important;
    }
    public String getContent() {
        return mContent;
    }
    public void setContent(String content) {
        mContent = content;
    }
    public String getYear() {
        return mYear;
    }
    public void setYear(String year) {
        mYear = year;
    }
}
