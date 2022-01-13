package com.my.ecommerce.viewmodel;

import androidx.lifecycle.ViewModel;

import com.my.ecommerce.models.Category;
import com.my.ecommerce.repository.FirebaseRepository;
import com.my.ecommerce.utils.SingleLiveEvent;

import java.util.List;


public class AppViewModel extends ViewModel {

    FirebaseRepository repository= new FirebaseRepository();
    public SingleLiveEvent<List<Category>> listOfCategories;



    public AppViewModel() {
        listOfCategories = repository.listOfCategories;

    }

    public void getCategories(){

        repository.getCategoriesFromDataBase();
    }


}
