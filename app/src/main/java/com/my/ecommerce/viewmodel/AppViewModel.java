package com.my.ecommerce.viewmodel;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.my.ecommerce.models.Category;
import com.my.ecommerce.models.Product;
import com.my.ecommerce.models.UserInfo;
import com.my.ecommerce.models.UserType;
import com.my.ecommerce.repository.FirebaseRepository;
import com.my.ecommerce.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;


public class AppViewModel extends ViewModel {

    FirebaseRepository repository = new FirebaseRepository();
    public MutableLiveData<List<Category>> listOfCategories;
    public MutableLiveData<List<Product>> listOfProducts;
    public MutableLiveData<ArrayList<Product>> listOfCartProduct;
    public MutableLiveData<Product> product;

    public MutableLiveData<List<Product>> pastPurchaseList;
    public MutableLiveData<ArrayList<Product>> wishListItems;


    public SingleLiveEvent<Boolean> singUpSuccess;
    public SingleLiveEvent<Boolean> singInSuccess;

    public SingleLiveEvent<Boolean> saveUserDataSuccess;

    public MutableLiveData<Float> totalCartPrice;
    public SingleLiveEvent<String> addingProductToCartState;
    public MutableLiveData<UserInfo> userInformation;
    public ArrayList<String> listOfCategoriesString;
    public ArrayList<String> usersType = new ArrayList<>();
    public SingleLiveEvent<Boolean> uploadingProductState;
    public MutableLiveData<List<Product>> listOfUserProduct;

    public SingleLiveEvent<String> singUpError;
    public SingleLiveEvent<String> singInError;


    public AppViewModel() {
        repository.checkUserAuthentication();
        listOfCategories = repository.listOfCategories;
        listOfProducts = repository.listOfProducts;
        repository.getCategoriesFromDataBase();
        repository.getProductsFromDataBase();
        product = repository.selectedProduct;
        listOfCartProduct = repository.listOfCartProduct;
        totalCartPrice = repository.totalCartPrice;
        addingProductToCartState = repository.addingProductToCartState;
        singInSuccess = repository.singInSuccess;
        singUpSuccess = repository.singUpSuccess;
        saveUserDataSuccess = repository.saveUserDataSuccess;
        userInformation = repository.userInformation;
        singInError = repository.singInError;
        singUpError = repository.singUpError;
        pastPurchaseList= repository.pastPurchaseList;
        wishListItems=repository.wishListItems;
        listOfCategoriesString=repository.listOfCategoriesString;
        usersType.add("Buyer");
        usersType.add("Seller");
        uploadingProductState=repository.UploadingProductState;
        listOfUserProduct= repository.listOfUserProduct;


    }

    public void getCategories() {

        repository.getCategoriesFromDataBase();
    }

    public void getProducts() {

        repository.getProductsFromDataBase();
    }

    public void getProductById(int id) {
        repository.getProductById(id);

    }

    public void removeProductFromCart(int position) {

        repository.removeProductFromCart(position);


    }


    public void addToPrice(float priceToBeAdd) {

        repository.addToPrice(priceToBeAdd);


    }


    public void minusFromPrice(float priceToBeMinus) {

        repository.minusFromPrice(priceToBeMinus);
    }

    public void savedProductToCart(int productId) {
        repository.checkIfProductAlreadySaved(productId);
    }

    public void countCartTotalPrice(List<Product> products) {
        repository.countTotalPrice(products);
    }


    public void sendConfirmationEmail(String emailAddress) {
        repository.sendOrderConfirmation(emailAddress);
    }


    public void removeCardItems() {
        repository.removeALlCardProducts();
    }

    public UserType getUsertype() {
        return repository.userIs;
    }


    public void singUp(String email, String password) {
        repository.singUp(email, password);

    }

    public void singIn(String email, String password) {
        repository.singIn(email, password);

    }

    public void saveUserInformation(UserInfo user) {
        repository.SaveUserInfo(user);
    }


    public void uploadUserProfileImage(Uri profileImage) {
        repository.saveUserProfileImage(profileImage);
    }


    public void logout() {
        repository.logout();
    }

    public void changePassword(String oldPassword, String newPassword) {

        repository.changePassword(oldPassword, newPassword);

    }

    public void savedToPastPurchase(ArrayList<Product> productList) {
        repository.savedToPastPurchase(productList);
    }

    public void getPastPurchase(){

        repository.getPastPurchase();
    }

    public void savedToWishList(Product wishProduct) {

       repository.savedToWishList(wishProduct);

    }


    public  void getWishList(){

        repository.getSavedWishList();
    }


   public void getAllUserSellsProduct(){
        repository.getAllUserSellsProduct();
   }


   public void filterProduct(String query){
        repository.filterProduct(query);
   }

   public void filterCategory(String query){
        repository.filterCategory(query);
   }


    public void uploadProduct(Uri productImage,String title,String information,String features ,String Category,String price){

        repository.uploadProduct(productImage, title, information, features, Category, price);
    }
}
