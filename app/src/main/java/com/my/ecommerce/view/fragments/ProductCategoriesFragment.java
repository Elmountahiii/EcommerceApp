package com.my.ecommerce.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.my.ecommerce.R;
import com.my.ecommerce.adapters.CategoryListAdapter;
import com.my.ecommerce.models.Category;
import com.my.ecommerce.viewmodel.AppViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProductCategoriesFragment extends Fragment {

    AppViewModel viewModel;

    RecyclerView categoryRecyclerList;
     ProgressBar progressBar;

    public ProductCategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryRecyclerList= view.findViewById(R.id.categoryList);
        progressBar=view.findViewById(R.id.progressBar);
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

        viewModel.getCategories();
    }

    @Override
    public void onResume() {
        super.onResume();

        viewModel.listOfCategories.observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categoryList) {
                progressBar.setVisibility(View.INVISIBLE);
                CategoryListAdapter adapter = new CategoryListAdapter(categoryList);
                categoryRecyclerList.setAdapter(adapter);

            }
        });

    }

    // start using that viewModel
    void initViewModel(){
        viewModel =new ViewModelProvider(requireActivity()).get(AppViewModel.class);

    }

}