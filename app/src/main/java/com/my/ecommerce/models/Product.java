package com.my.ecommerce.models;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Product {
    
    public String title,features,mainProductImage,productInformation,category,ownerId;
    public  float price ;
    public  int id ,selleCount;

    @ServerTimestamp
    public  Date time;
    public Product(int Id,Float Price,String Title,String Features,String MainProductImage,String ProductInformation,String Category,String owner,int selleCount){

        this.id=Id;
        this.price=Price;
        this.title=Title;
        this.mainProductImage=MainProductImage;
        this.features=Features;
        this.productInformation=ProductInformation;
        this.category=Category;
        this.selleCount=selleCount;
        this.ownerId=owner;

    }

    public Product(){

    }



}
