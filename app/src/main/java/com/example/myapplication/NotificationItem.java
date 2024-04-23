package com.example.myapplication;

public class NotificationItem {

    private String title;
    private String detail;

    public NotificationItem( String title, String detail)
    {
        this.title = title;
        this.detail = detail;
    }

    public void setTitle( String title){this.title = title;}

    public void setDetail(String detail){this.detail = detail;}

    public String getTitle(){return this.title;}

    public String getDetail(){return this.detail;}
}
