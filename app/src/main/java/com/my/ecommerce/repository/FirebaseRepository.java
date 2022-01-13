package com.my.ecommerce.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.my.ecommerce.models.Category;
import com.my.ecommerce.utils.SingleLiveEvent;

import java.util.List;



public class FirebaseRepository {


    public FirebaseRepository() {

    }

    // Firebase DataBase Instance
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    // categories data location
    private CollectionReference categoriesCollectionsPath = database.collection("categories");


    // The Category Data
    public SingleLiveEvent<List<Category>> listOfCategories = new SingleLiveEvent<List<Category>>();


    // This method is used to call firebase and get the data then put them in listOfCategories
    public void getCategoriesFromDataBase() {


        categoriesCollectionsPath
                .orderBy("CategoryId", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            listOfCategories.setValue(queryDocumentSnapshots.toObjects(Category.class));
                            Log.d("DatabaseMASTER", "Category call onSuccess: Done ");

                            for (int i = 0; i < listOfCategories.getValue().size(); i++) {

                                Log.d("DatabaseMASTER", listOfCategories.getValue().get(i).CategoryName);

                            }
                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DatabaseMASTER", "Category call onFailure: " + e.getMessage());

                    }
                });

    }


}
