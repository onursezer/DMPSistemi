package com.bitirme2.onursezer.dmpsistemi;

/**
 * Created by OnurSezer on 23.12.2017.
 */

public class Homework {

    private String nameOfHW;
    private String deliveryDate;
    private String deliveryTime;

    public Homework() {
    }

    public Homework(String nameOfHW, String deliveryDate, String deliveryTime) {
        this.nameOfHW = nameOfHW;
        this.deliveryDate = deliveryDate;
        this.deliveryTime = deliveryTime;
    }

    public String getNameOfHW() {
        return nameOfHW;
    }

    public void setNameOfHW(String nameOfHW) {
        this.nameOfHW = nameOfHW;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
