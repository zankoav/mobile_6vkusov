package com.example.alexandrzanko.mobile_6vkusov.Models;

/**
 * Created by alexandrzanko on 1/24/17.
 */

public class Variant {

    private Double price;
    private String size, weight;
    private int id;

    public Variant(Double price, String size, String weight, int id){
        this.price = price;
        this.size = size;
        this.weight = weight;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getSize() {
        return size;
    }

    public String getWeight() {
        return weight;
    }

    public Double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variant){
            Variant variant = (Variant)obj;
            return  price.equals(variant.getPrice()) &&
                    size.equals(variant.getSize()) &&
                    weight.equals(variant.getWeight());
        }else{
            return false;
        }
    }
}

