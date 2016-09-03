package com.cybermoosemoosemedia.honeydo.db;

public class HoneyDoDataModel {
    private int mId;
    private String mContent;
    private int mImportant;
    private Integer mDay;
    private Integer mMonth;
    private Integer mYear;

    //Data model for HoneyDo DB

    public HoneyDoDataModel(int id, String content, int important, Integer day, Integer month, Integer year) {
        mId = id;
        mImportant = important;
        mContent = content;
        mDay = day;
        mMonth = month;
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
    public Integer getDay() {
        return mDay;
    }
    public void setDay(Integer day) {
        mDay = day;
    }
    public Integer getMonth() {
        return mMonth;
    }
    public void setMonth(Integer month) {
        mYear = month;
    }
    public Integer getYear() {
        return mYear;
    }
    public void setYear(Integer year) {
        mYear = year;
    }
}
