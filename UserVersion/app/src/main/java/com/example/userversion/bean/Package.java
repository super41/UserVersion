package com.example.userversion.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by XJP on 2017/12/3.
 */
public class Package extends DataSupport implements Serializable {

    String name;
    String email;
    String wifiId;
    String wifiPsw;
    String distance;
    String emailTitle;



    String emailContent;
    String qrCode;
    long time;
    boolean isDelivery=false;
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isDelivery() {
        return isDelivery;
    }

    public void setDelivery(boolean delivery) {
        isDelivery = delivery;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWifiId() {
        return wifiId;
    }

    public void setWifiId(String wifiId) {
        this.wifiId = wifiId;
    }

    public String getWifiPsw() {
        return wifiPsw;
    }

    public void setWifiPsw(String wifiPsw) {
        this.wifiPsw = wifiPsw;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getEmailTitle() {
        return emailTitle;
    }

    public void setEmailTitle(String emailTitle) {
        this.emailTitle = emailTitle;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }
}
