package com.my.ecommerce.models;

import java.util.Collections;
import java.util.List;

public class Cart {
    public List<Integer> productId;


    public Cart(List<Integer>ids){
        this.productId=ids;
    }
    public Cart(){
     this.productId= Collections.emptyList();
    }
}
