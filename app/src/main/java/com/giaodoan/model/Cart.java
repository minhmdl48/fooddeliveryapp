package com.giaodoan.model;

public class Cart {
    private String uid;
    private String pid;
    private String name;
    private int quantity;
    private String price;
    private String img_url;

    public Cart() {
    }

    public Cart(String uid, String pid, String name, int quantity, String price, String img_url) {
        this.uid = uid;
        this.pid = pid;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.img_url = img_url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}

