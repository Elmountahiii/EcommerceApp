package com.my.ecommerce.repository;

import android.net.Uri;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.my.ecommerce.models.Cart;
import com.my.ecommerce.models.Category;
import com.my.ecommerce.models.PastPurchase;
import com.my.ecommerce.models.Product;
import com.my.ecommerce.models.UserInfo;
import com.my.ecommerce.models.UserType;
import com.my.ecommerce.models.WishList;
import com.my.ecommerce.utils.SingleLiveEvent;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class FirebaseRepository {


    public FirebaseRepository() {

    }

    // Firebase Authentication Instance
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public UserType userIs = UserType.Anonymous;


    // Firebase DataBase Instance
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();

    // categories data location
    private final CollectionReference categoriesCollectionsPath = database.collection("categories");

    private FirebaseStorage storage = FirebaseStorage.getInstance();


    // Products data location
    private final CollectionReference productsCollectionsPath = database.collection("products");

    // cart data location
    private final CollectionReference cartCollectionsPath = database.collection("cart");


    //userInformation  data location
    private final CollectionReference userInformationCollectionsPath = database.collection("userInfo");


    //Past Purchase data location
    private final CollectionReference pastPurchaseCollectionsPath = database.collection("PastPurchase");

    //Wish Products data location
    private final CollectionReference wishProductsCollectionsPath = database.collection("WishProducts");


    public MutableLiveData<UserInfo> userInformation = new MutableLiveData<>();

    public MutableLiveData<Product> selectedProduct = new MutableLiveData<Product>();

    public MutableLiveData<Float> totalCartPrice = new MutableLiveData<>(0.0f);

    public MutableLiveData<ArrayList<Product>> pastPurchaseList = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Product>> wishListItems = new MutableLiveData<>();


    private Cart listOfCartIds = new Cart(new ArrayList<>());

    public SingleLiveEvent<Boolean> singUpSuccess = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> singInSuccess = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> saveUserDataSuccess = new SingleLiveEvent<>();

    public SingleLiveEvent<String> singUpError = new SingleLiveEvent<>();
    public SingleLiveEvent<String> singInError = new SingleLiveEvent<>();

    private String userProfileLink;


    // The Category Data
    public MutableLiveData<List<Category>> listOfCategories = new MutableLiveData<List<Category>>() {
    };

    // The Products Data
    public MutableLiveData<List<Product>> listOfProducts = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Product>> listOfCartProduct = new MutableLiveData<>();


    public SingleLiveEvent<String> addingProductToCartState = new SingleLiveEvent<String>();


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

        if (auth.getCurrentUser().getUid() != null) {

            cartCollectionsPath.document(auth.getCurrentUser().getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                listOfCartIds = documentSnapshot.toObject(Cart.class);
                                getCardProducts();

                            } else {
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

    }

    public void getCardProducts() {

        ArrayList<Product> productList = new ArrayList<>();

        if (listOfCartIds.productId.size() == 0) {
            listOfCartProduct.setValue(productList);
        }

        for (int i = 0; i < listOfCartIds.productId.size(); i++) {

            productsCollectionsPath
                    .whereEqualTo("id", listOfCartIds.productId.get(i))
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            if (queryDocumentSnapshots.isEmpty()) {

                            } else {
                            }

                            try {
                                productList.add(queryDocumentSnapshots.toObjects(Product.class).get(0));
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


    }

    public void removeALlCardProducts() {
        Map<String, Object> cartData = new HashMap<>();
        cartData.put("productId", new ArrayList<Integer>());
        cartCollectionsPath.document(auth.getCurrentUser().getUid())
                .set(cartData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        getSavedCardIds();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void saveProductsIdsToDatabase(List<Integer> productIds) {

        Map<String, Object> cartData = new HashMap<>();
        cartData.put("productId", productIds);

        cartCollectionsPath.document(auth.getCurrentUser().getUid())
                .set(cartData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

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

        Product product = listOfCartProduct.getValue().get(position);
        listOfCartIds.productId.remove(Integer.valueOf(product.id));


        saveProductsIdsToDatabase(listOfCartIds.productId);

    }


    public void savedToPastPurchase(ArrayList<Product> productList) {

        pastPurchaseCollectionsPath.document(auth.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {

                    PastPurchase purchase = documentSnapshot.toObject(PastPurchase.class);
                    if (purchase != null) {
                        purchase.productList.addAll(productList);

                        pastPurchaseCollectionsPath
                                .document(auth.getCurrentUser().getUid())
                                .set(purchase)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                    }


                } else {
                    pastPurchaseCollectionsPath
                            .document(auth.getCurrentUser().getUid())
                            .set(new PastPurchase(productList))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                }

            }
        });


    }


    public void getPastPurchase() {

        pastPurchaseCollectionsPath.document(auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        PastPurchase purchase = documentSnapshot.toObject(PastPurchase.class);
                        if (purchase != null) {
                            pastPurchaseList.setValue(purchase.productList);
                        } else {

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    public void savedToWishList(Product wishProduct) {

        wishProductsCollectionsPath.document(auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists())
                        {
                            WishList wishList = documentSnapshot.toObject(WishList.class);

                            if (wishList != null) {
                                wishList.productList.add(wishProduct);



                                wishProductsCollectionsPath.document(auth.getCurrentUser().getUid())
                                        .set(wishList)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });

                            }

                        }

                        else {

                            WishList wishList =new WishList();
                            ArrayList<Product> products =new ArrayList<>();
                            products.add(wishProduct);
                            wishList.productList=products;
                            wishProductsCollectionsPath.document(auth.getCurrentUser().getUid())
                                    .set(wishList)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }


    public void getSavedWishList(){


        wishProductsCollectionsPath.document(auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d("doxa", "onSuccess: ");

                        if (documentSnapshot.exists()){

                            Log.d("doxa", "doc l9inah: ");

                            WishList wishList = documentSnapshot.toObject(WishList.class);

                            if (wishList!=null){
                                Log.d("doxa", "object mchi null: ");

                                wishListItems.setValue(wishList.productList);

                            }else {
                                Log.d("doxa", "object null: ");

                            }
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    //Authentication

    public void checkUserAuthentication() {
        if (auth.getCurrentUser() == null) {
            signUserAnonymously();
        } else {
            checkUserType();
        }
    }

    private void checkUserType() {


        userInformationCollectionsPath.document(auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d("khobi", "onSuccess: " + documentSnapshot.getData());
                        UserInfo info = documentSnapshot.toObject(UserInfo.class);
                        if (info != null) {
                            userIs = info.userType;


                            userInformation.setValue(info);

                            singInSuccess.setValue(true);
                            saveUserDataSuccess.setValue(true);


                        }
                        getSavedCardIds();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void signUserAnonymously() {

        auth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                getSavedCardIds();

            }
        });
    }

    public void singUp(String email, String password) {
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        auth.getCurrentUser().linkWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        singUpSuccess.setValue(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                singUpSuccess.setValue(false);
                singUpError.setValue(e.getMessage());

            }
        });

    }

    public void singIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        checkUserType();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        singInSuccess.setValue(false);
                        singInError.setValue(e.getMessage());

                    }
                });
    }


    public void sendOrderConfirmation(String emailAddress) {


    }


    public void SaveUserInfo(UserInfo userInfo) {

        userInfo.profileImage = userProfileLink;
        userInformationCollectionsPath.document(auth.getCurrentUser().getUid()).set(userInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        userIs = userInfo.userType;

                        checkUserType();
                        Log.d("lilhada", "onSuccess: done ashbi");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("lilhada", "Failure: no ashbi");

                    }
                });

    }

    public void saveUserProfileImage(Uri profileImage) {
        StorageReference ref
                = storage.getReference().child(
                "images/"
                        + UUID.randomUUID().toString());


        UploadTask uploadTask = ref.putFile(profileImage);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    userProfileLink = downloadUri.toString();
                    Log.d("Imagaaa", "onComplete: this is the Uri " + downloadUri.toString());
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }


    public void logout() {
        auth.signOut();
        userIs = UserType.Anonymous;
    }

    public void changePassword(String oldPassword, String newPassword) {
        String email = auth.getCurrentUser().getEmail();


        AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);

        auth.getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("bimno", "onComplete: the first");
                            auth.getCurrentUser().updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("bimno", "onComplete: the second");

                                    } else {
                                        Log.d("bimno", "secend la");

                                    }
                                }
                            });
                        } else {
                            Log.d("bimno", "first  la");

                        }
                    }
                });

    }

}
