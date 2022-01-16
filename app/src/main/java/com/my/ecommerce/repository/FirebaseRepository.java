package com.my.ecommerce.repository;

import android.util.Log;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.my.ecommerce.models.Category;
import com.my.ecommerce.models.Product;
import com.my.ecommerce.utils.SingleLiveEvent;

import java.util.List;



public class FirebaseRepository {


    public FirebaseRepository() {

    }

    // Firebase DataBase Instance
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();

    // categories data location
    private final CollectionReference categoriesCollectionsPath = database.collection("categories");

    // Products data location
    private final CollectionReference productsCollectionsPath = database.collection("products");



    // The Category Data
    public MutableLiveData<List<Category>> listOfCategories = new MutableLiveData<List<Category>>() {
    };

    // The Products Data
    public MutableLiveData<List<Product>> listOfProducts = new MutableLiveData<>();



    // This method is used to call firebase and get the data then put them in listOfCategories
    public void getCategoriesFromDataBase() {


        categoriesCollectionsPath
                .orderBy("CategoryId", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        listOfCategories.setValue(queryDocumentSnapshots.toObjects(Category.class));

                    }


                })
                .addOnFailureListener(error -> {

                });

    }



    public  void  getProductsFromDataBase(){
        productsCollectionsPath
                .orderBy("id", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        listOfProducts.setValue(queryDocumentSnapshots.toObjects(Product.class));

                    }


                })
                .addOnFailureListener(error -> Log.d("DatabaseMASTER", "Products call onFailure: " + error.getMessage()));


    }


}
