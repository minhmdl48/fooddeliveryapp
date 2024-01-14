package com.giaodoan.model;

public class Order {
    private String uid;
    private String oid;
    private String status;
    private String price;
    private String quantity;
    private String ordertime;

    public Order() {
    }
    public Order(String uid, String oid, String status, String price, String quantity, String ordertime) {
        this.uid = uid;
        this.oid = oid;
        this.status = status;
        this.price = price;
        this.quantity = quantity;
        this.ordertime = ordertime;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }
}
