package com.giaodoan.model;

import java.util.Date;

public class Order {
    private String oid;
    private String userId;
    private int status;
    private float total;
    private ItemOrder[] items;

    private String timerTime;

    private String note;

    public Order(String oid, String userId, int status, float total, ItemOrder[] items, String timerTime, String note) {
        this.oid = oid;
        this.userId = userId;
        this.status = status;
        this.total = total;
        this.items = items;
        this.timerTime = timerTime;
        this.note = note;
    }


    public String getOid() {
        return this.oid;
    }

    public String getUserId() {
        return this.userId;
    }
    public int getStatus() {
        return this.status;
    }
    public float getTotal() {
        return this.total;
    }
    public ItemOrder[] getItems() {
        return this.items;
    }
    public String getTimertime() {
        return this.timerTime;
    }
    public String getNote() {
        return this.note;
    }



    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setOid(String oid) {
        this.oid = oid;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public void setTotal(float total) {
        this.total = total;
    }
    public void setItems(ItemOrder[] items) {
        this.items = items;
    }
    public void setTimerTime(String date) {
        this.timerTime= date;
    }
    public void setNote(String note) {
        this.note = note;
    }
}
