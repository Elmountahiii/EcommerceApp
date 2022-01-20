package com.my.ecommerce.repository;

import android.util.Log;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.my.ecommerce.models.Category;
import com.my.ecommerce.models.Product;
import com.my.ecommerce.utils.SingleLiveEvent;

import java.text.DecimalFormat;
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

    public MutableLiveData<Product> selectedProduct=new MutableLiveData<Product>();


    public MutableLiveData<Float> totalCartPrice=new MutableLiveData<>(0.0f);



    // The Category Data
    public MutableLiveData<List<Category>> listOfCategories = new MutableLiveData<List<Category>>() {
    };

    // The Products Data
    public MutableLiveData<List<Product>> listOfProducts = new MutableLiveData<>();

    public MutableLiveData<List<Product>> listOfCartProduct= new MutableLiveData<>();



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
                        listOfCartProduct.setValue(queryDocumentSnapshots.toObjects(Product.class));

                        getCartTotalPrice(listOfProducts.getValue());

                    }


                })
                .addOnFailureListener(error -> Log.d("DatabaseMASTER", "Products call onFailure: " + error.getMessage()));


    }

    private void getCartTotalPrice(List<Product> products) {
        totalCartPrice.setValue(0.0f);

        for (int i = 0; i < products.size(); i++) {


            totalCartPrice.setValue(totalCartPrice.getValue()+products.get(i).price);



        }
    }


    public void getProductById(int id){
        selectedProduct.setValue(new Product());
        productsCollectionsPath
                .whereEqualTo("id",id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                       selectedProduct.setValue(queryDocumentSnapshots.toObjects(Product.class).get(0));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }


    public void removeProductFromCart(int position) {

        listOfCartProduct.getValue().remove(position);
        getCartTotalPrice(listOfCartProduct.getValue());
        listOfCartProduct.postValue(listOfCartProduct.getValue());


    }



    public void addToPrice(float priceToBeAdd) {

        totalCartPrice.setValue(totalCartPrice.getValue()+priceToBeAdd);


    }

    public void minusFromPrice(float priceToBeMinus) {
        totalCartPrice.setValue(totalCartPrice.getValue()-priceToBeMinus);

    }
}
