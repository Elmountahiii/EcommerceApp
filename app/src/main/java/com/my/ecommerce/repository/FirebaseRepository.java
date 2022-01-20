package com.my.ecommerce.repository;

import android.util.Log;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.my.ecommerce.models.Cart;
import com.my.ecommerce.models.Category;
import com.my.ecommerce.models.Product;
import com.my.ecommerce.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FirebaseRepository {


    public FirebaseRepository() {

    }

    // Firebase Authentication Instance
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    // Firebase DataBase Instance
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();

    // categories data location
    private final CollectionReference categoriesCollectionsPath = database.collection("categories");

    // Products data location
    private final CollectionReference productsCollectionsPath = database.collection("products");

    // cart data location
    private final CollectionReference cartCollectionsPath = database.collection("cart");


    public MutableLiveData<Product> selectedProduct = new MutableLiveData<Product>();

    public MutableLiveData<Float> totalCartPrice = new MutableLiveData<>(0.0f);

    private Cart listOfCartIds = new Cart(new ArrayList<>());


    // The Category Data
    public MutableLiveData<List<Category>> listOfCategories = new MutableLiveData<List<Category>>() {
    };

    // The Products Data
    public MutableLiveData<List<Product>> listOfProducts = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Product>> listOfCartProduct = new MutableLiveData<>();


    public SingleLiveEvent<String> addingProductToCartState=new SingleLiveEvent<String>();


    // categories

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



    // products

    public void getProductsFromDataBase() {
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

    public void getProductById(int id) {
        selectedProduct.setValue(new Product());
        productsCollectionsPath
                .whereEqualTo("id", id)
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




    // Logic methods

    public void addToPrice(float priceToBeAdd) {

        totalCartPrice.setValue(totalCartPrice.getValue() + priceToBeAdd);


    }


    public void minusFromPrice(float priceToBeMinus) {
        totalCartPrice.setValue(totalCartPrice.getValue() - priceToBeMinus);

    }


    public void countTotalPrice(List<Product> products) {
        totalCartPrice.setValue(0.0f);

        for (int i = 0; i < products.size(); i++) {

            totalCartPrice.setValue(totalCartPrice.getValue() + products.get(i).price);


        }
    }


    //Cart

    public void checkIfProductAlreadySaved(int productId) {
        if (listOfCartIds.productId.contains(productId)) {
            addingProductToCartState.setValue("The Product Already Saved");

        } else {
            listOfCartIds.productId.add(productId);
            saveProductsIdsToDatabase(listOfCartIds.productId);
        }



    }


    public void getSavedCardIds() {
        Log.d("CartShit", "Start Getting list Of Id ");
        cartCollectionsPath.document(auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            listOfCartIds = documentSnapshot.toObject(Cart.class);
                            Log.d("CartShit", "Success size of ids is :"+listOfCartIds.productId.size());
                            getCardProducts();

                        } else {
                            Log.d("CartShit", "the User never Saved Any Product");
                            listOfCartIds = new Cart(new ArrayList<>());

                        }

                    }
                }).
                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void getCardProducts() {
        Log.d("CartShit", "Start getting Product By his Id from database ");

        ArrayList<Product> productList = new ArrayList<>();

        Log.d("CartShit", "the list of ids is now : "+listOfCartIds.productId.size());
        if (listOfCartIds.productId.size()==0){
            listOfCartProduct.setValue(productList);
        }

        for (int i = 0; i < listOfCartIds.productId.size(); i++) {

            productsCollectionsPath
                    .whereEqualTo("id", listOfCartIds.productId.get(i))
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            if (queryDocumentSnapshots.isEmpty()){
                                Log.d("CartShit", "empty");
                            }else {
                                Log.d("CartShit", "not empty");
                            }

                            try {
                                productList.add(queryDocumentSnapshots.toObjects(Product.class).get(0));
                                Log.d("CartShit", queryDocumentSnapshots.toObjects(Product.class).get(0).title);
                                listOfCartProduct.setValue(productList);

                            } catch (Exception e) {

                            }


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

        }

//        Log.d("CartShit", "end getting product the size of products : "+listOfCartProduct.getValue().size());


    }



    private void saveProductsIdsToDatabase(List<Integer> productIds) {

        Log.d("CartShit", "start Saving the new Product Ids to Database");
        Map<String, Object> cartData = new HashMap<>();
        cartData.put("productId", productIds);

        cartCollectionsPath.document(auth.getCurrentUser().getUid())
                .set(cartData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("CartShit", "Success saving now get fresh cart product");

                        getCardProducts();



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    public void removeProductFromCart(int position) {
        Log.d("CartShit", "Start the process of Deleting a product from database");

        Product product = listOfCartProduct.getValue().get(position);
        Log.d("CartShit", "this product: "+ product.title);
        Log.d("CartShit", "id is :"+product.id);
        listOfCartIds.productId.remove(Integer.valueOf(product.id));
        if (listOfCartIds.productId.contains(Integer.valueOf(product.id))){
            Log.d("CartShit", "yes the Id is still there ");
        }else {
            Log.d("CartShit", "the id is gone the size of the list is : "+listOfCartIds.productId.size());
        }


        saveProductsIdsToDatabase(listOfCartIds.productId);

    }



    //Authentication

    public void checkUserAuthentication() {
        if (auth.getCurrentUser() == null) {
            signUserAnonymously();
        }
    }

    private void signUserAnonymously() {

        auth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Log.d("AuthStuuf", "onSuccess: " + authResult.getUser().getUid());
            }
        });
    }

}
