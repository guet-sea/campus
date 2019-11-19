package com.sea.bean;

import javax.persistence.Column;
import javax.persistence.Id;

public class AuthInfo {

    @Id
    @Column(name = "userId")
    private Integer userId;

    @Column(name = "fIdCard")
    private String fIdCard;

    @Column(name = "rIdCard")
    private  String rIdCard;

    @Column(name = "studentId")
    private String studentId;

    @Column(name = "studentIdPhoto")
    private String studentIdPhoto;




}
