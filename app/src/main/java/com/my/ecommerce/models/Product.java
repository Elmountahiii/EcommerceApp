package com.my.ecommerce.models;

public class Product {
    
    public String title,features,mainProductImage,productInformation,category;
    public  float price ;
    public  int id ;

    public Product(int Id,Float Price,String Title,String Features,String MainProductImage,String ProductInformation,String Category){

        this.id=Id;
        this.price=Price;
        this.title=Title;
        this.mainProductImage=MainProductImage;
        this.features=Features;
        this.productInformation=ProductInformation;
        this.category=Category;

    }

    public Product(){

    }




}
