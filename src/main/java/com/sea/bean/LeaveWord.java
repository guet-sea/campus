package com.sea.bean;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Table(name = "leaveWord")
public class LeaveWord {

    @Id
    private Integer id;

    @NotBlank(message = "{leaveWord.username.NotBlank}")
    @NotNull(message = "{leaveWord.username.notnull}")
    @Column(name = "userName")
    private String userName;

    @NotNull(message = "{leaveWord.goodsId.notnull}")
    @Column(name = "goodsId")
    private Integer goodsId;

    @NotBlank(message = "{leaveWord.content.NotBlank}")
    @NotNull(message = "{leaveWord.content.notnull}")
    private String content;

    @Column(name = "messageTime")
    private String messageTime;

    @Transient
    private String headPortrait;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "LeaveWord{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", goodsId=" + goodsId +
                ", content='" + content + '\'' +
                ", messageTime='" + messageTime + '\'' +
                ", headPortrait='" + headPortrait + '\'' +
                '}';
    }
}
