package com.my.ecommerce.viewmodel;

import androidx.lifecycle.ViewModel;

import com.my.ecommerce.models.Category;
import com.my.ecommerce.repository.FirebaseRepository;
import com.my.ecommerce.utils.SingleLiveEvent;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AppViewModel extends ViewModel {

    FirebaseRepository repository= new FirebaseRepository();
    public SingleLiveEvent<List<Category>> listOfCategories;



    @Inject
    public AppViewModel() {
        listOfCategories = repository.listOfCategories;

    }

    public void getCategories(){

        repository.getCategoriesFromDataBase();
    }


}
