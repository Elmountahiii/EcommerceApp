package com.my.ecommerce.models;

import java.util.ArrayList;
import java.util.List;

public class PastPurchase {

   public List<Product> productList;


    public  PastPurchase(){

    }

    public PastPurchase(ArrayList<Product> productList) {
        this.productList = productList;
    }


}
