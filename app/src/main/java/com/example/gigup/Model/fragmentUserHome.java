package com.example.gigup.Model;

public class fragmentUserHome{

    String title, desc, tag, price,location;

    public fragmentUserHome(String title, String desc, String tag, String price,String location) {
        this.title = title;
        this.desc = desc;
        this.tag = tag;
        this.price = price;
        this.location = location;
    }

    public fragmentUserHome(){

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
