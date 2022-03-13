package com.my.ecommerce.models;

import java.util.ArrayList;

public class PastPurchase {

   public ArrayList<Product> productList;


    public  PastPurchase(){

    }

    public PastPurchase(ArrayList<Product> productList) {
        this.productList = productList;
    }


}
