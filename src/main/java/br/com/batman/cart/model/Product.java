package br.com.batman.cart.model;


import lombok.*;

@Data
public class Product {
    private String id;
    private String sku;
    private  String name;
    private String shortDescription;
    private String longDescription;
    private String imageUrl;
    private Price price;
}
