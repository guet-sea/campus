package com.sea.bean;

import javax.persistence.Column;

public class Chat {

    private Integer id;

    @Column(name = "fromname")
    private String from;

    @Column(name = "toname")
    private String to;

    private String content;

    @Column(name = "msgTime")
    private String msgTime;

    @Column(name = "statusname")
    private  String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", content='" + content + '\'' +
                ", msgTime='" + msgTime + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
