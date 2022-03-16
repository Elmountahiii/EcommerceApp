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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    public MutableLiveData<List<Product>> pastPurchaseList = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Product>> wishListItems = new MutableLiveData<>();


    private Cart listOfCartIds = new Cart(new ArrayList<>());

    public SingleLiveEvent<Boolean> singUpSuccess = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> singInSuccess = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> saveUserDataSuccess = new SingleLiveEvent<>();

    public SingleLiveEvent<String> singUpError = new SingleLiveEvent<>();
    public SingleLiveEvent<String> singInError = new SingleLiveEvent<>();

    private String userProfileLink;

    public ArrayList<String> listOfCategoriesString = new ArrayList<>();


    // The Category Data
    public MutableLiveData<List<Category>> listOfCategories = new MutableLiveData<List<Category>>() {
    };

    // The Products Data
    public MutableLiveData<List<Product>> listOfProducts = new MutableLiveData<>();

    public MutableLiveData<List<Product>> listOfUserProduct= new MutableLiveData<>();

    public MutableLiveData<ArrayList<Product>> listOfCartProduct = new MutableLiveData<>();


    public SingleLiveEvent<String> addingProductToCartState = new SingleLiveEvent<String>();

    public SingleLiveEvent<Boolean> UploadingProductState = new SingleLiveEvent<Boolean>();



    // categories

    public void getCategoriesFromDataBase() {


        categoriesCollectionsPath
                .orderBy("CategoryId", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {

                        List<Category> categories = queryDocumentSnapshots.toObjects(Category.class);

                        for (int i = 0; i < categories.size(); i++) {
                            listOfCategoriesString.add(categories.get(i).CategoryName);

                        }
                        listOfCategories.setValue(categories);


                    }


                })
                .addOnFailureListener(error -> {

                });

    }


    // products

    public void getProductsFromDataBase() {
        productsCollectionsPath
                .orderBy("time", Query.Direction.DESCENDING)
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


    public void uploadProduct(Uri productImage, String title, String information, String features, String Category, String price) {


        StorageReference ref
                = storage.getReference().child(
                "images/"
                        + UUID.randomUUID().toString());


        UploadTask uploadTask = ref.putFile(productImage);

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


                    int productId=(int) System.currentTimeMillis();
                    String productImage = downloadUri.toString();
                    String productTile = title;
                    String productCategory = Category;
                    String productFeatures = features;
                    String ProductInformation = information;
                    Float productPrice =Float.valueOf(price);
                    int productSellsCount=0;
                    String productOwner= auth.getCurrentUser().getUid();

                    Product product = new Product(productId,productPrice,productTile,productFeatures,productImage,ProductInformation,productCategory,productOwner,productSellsCount);

                    productsCollectionsPath.add(product)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    UploadingProductState.setValue(true);
                                    getProductsFromDataBase();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            UploadingProductState.setValue(false);
                            Log.d("kikch", "onFailure: " + e.getMessage());
                        }
                    });

                }
            }
        });


    }

    public void getAllUserSellsProduct(){

        productsCollectionsPath.orderBy("ownerId", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            listOfUserProduct.setValue(queryDocumentSnapshots.toObjects(Product.class));
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    public void filterProduct(String query){


        if (query.isEmpty()){
            getProductsFromDataBase();
        }else {
            ArrayList<Product> filteredList= new ArrayList<>();

            for (int i = 0; i < listOfProducts.getValue().size(); i++) {

                Product theProduct=listOfProducts.getValue().get(i);
                if (theProduct.title.contains(query)){
                    filteredList.add(theProduct);
                }

            }

            listOfProducts.setValue(filteredList);

        }






    }

    public void filterCategory(String query){
        if (query.isEmpty()){
            getCategoriesFromDataBase();
        }else {
            ArrayList<Category> theCategoryList=new ArrayList<>();
            for (int i = 0; i < listOfCategories.getValue().size(); i++) {

                Category category= listOfCategories.getValue().get(i);

                if (category.CategoryName.contains(query)){
                    theCategoryList.add(listOfCategories.getValue().get(i));
                }

            }

            listOfCategories.setValue(theCategoryList);
        }
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

        addBalanceToUser(productList);

        pastPurchaseCollectionsPath.document(auth.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {

                    PastPurchase purchase = documentSnapshot.toObject(PastPurchase.class);
                    if (purchase != null) {
                       try {
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

                       }catch (Exception e){
                           Log.e("thezbi", "onSuccess: ", e);
                       }

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


    private void addBalanceToUser(ArrayList<Product> productList){


        for (int i = 0; i < productList.size(); i++) {
            String ownerId=productList.get(i).ownerId;
            doThJob(ownerId,productList.get(i).price);
            }

    }


    private void  doThJob(String owner,float price){
        userInformationCollectionsPath.document(owner)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            UserInfo user=documentSnapshot.toObject(UserInfo.class);
                            user .balance=  user.balance+price;
                            userInformationCollectionsPath.document(owner).set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    }) .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            })  ;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

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

                        if (documentSnapshot.exists()) {
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

                        } else {

                            WishList wishList = new WishList();
                            ArrayList<Product> products = new ArrayList<>();
                            products.add(wishProduct);
                            wishList.productList = products;
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


    public void getSavedWishList() {


        wishProductsCollectionsPath.document(auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d("doxa", "onSuccess: ");

                        if (documentSnapshot.exists()) {

                            Log.d("doxa", "doc l9inah: ");

                            WishList wishList = documentSnapshot.toObject(WishList.class);

                            if (wishList != null) {
                                Log.d("doxa", "object mchi null: ");

                                wishListItems.setValue(wishList.productList);

                            } else {
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
