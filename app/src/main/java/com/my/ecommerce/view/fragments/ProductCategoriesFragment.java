package com.my.ecommerce.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputLayout;
import com.my.ecommerce.R;
import com.my.ecommerce.adapters.CategoryListAdapter;
import com.my.ecommerce.models.Category;
import com.my.ecommerce.viewmodel.AppViewModel;

import java.util.List;


public class ProductCategoriesFragment extends Fragment {

    AppViewModel viewModel;

    RecyclerView categoryRecyclerList;

    TextInputLayout searchInput;


    public ProductCategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryRecyclerList = view.findViewById(R.id.categoryList);
        searchInput=view.findViewById(R.id.textField2);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_categories, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initViewModel();

    }

    @Override
    public void onResume() {
        super.onResume();

        viewModel.listOfCategories.observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categoryList) {
                CategoryListAdapter adapter = new CategoryListAdapter(categoryList);
                categoryRecyclerList.setAdapter(adapter);

            }
        });


        searchInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.filterCategory(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    // start using that viewModel
    void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

    }

}