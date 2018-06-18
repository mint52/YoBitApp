package com.example.kolyannow.myapplication;

public class Pair {

    int id;
    String name;
    String price;
    String date;

    public Pair() {
    }

    public Pair(String name) {
        this.name = name;
    }

    public Pair(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public Pair(int id, String name) {

        this.id = id;
        this.name = name;
    }

    public Pair(int id, String name, String price, String date) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
