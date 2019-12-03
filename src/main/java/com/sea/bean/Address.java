package com.sea.bean;

import javax.persistence.Column;
import javax.persistence.Id;

public class Address {

    @Column(name = "userName")
    private String userName;

    @Column(name = "tel")
    private String tel;

    private String address;

    @Id
    @Column(name = "addressId")
    private Integer addressId;

    @Column(name = "defaultAddress")
    private String defaultAddress;

    @Column(name = "fullName")
    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(String defaultAddress) {
        this.defaultAddress = defaultAddress;
    }
}
