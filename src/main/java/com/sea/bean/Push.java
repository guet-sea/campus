package com.sea.bean;

public class Push {

    private  Integer id;

    private String picture;

    private String title;

    private String content;

    private String school;

    private String time;

    private String release;

    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Push{" +
                "id=" + id +
                ", picture='" + picture + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", school='" + school + '\'' +
                ", time='" + time + '\'' +
                ", release='" + release + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
