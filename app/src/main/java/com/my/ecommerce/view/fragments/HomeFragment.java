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
import android.widget.Toast;

import com.my.ecommerce.R;
import com.my.ecommerce.adapters.ProductsListAdapter;
import com.my.ecommerce.interfaces.OnProductClickListener;
import com.my.ecommerce.models.Product;
import com.my.ecommerce.repository.FirebaseRepository;
import com.my.ecommerce.viewmodel.AppViewModel;

import java.util.List;


public class HomeFragment extends Fragment implements OnProductClickListener {

    AppViewModel viewModel;

    RecyclerView productsRecyclerList;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productsRecyclerList=view.findViewById(R.id.recentlyProductsList);
    }

    @Override
    public void onStart() {
        super.onStart();
        initViewModel();
    }


    @Override
    public void onResume() {
        super.onResume();

        viewModel.listOfProducts.observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                ProductsListAdapter adapter= new ProductsListAdapter(products,HomeFragment.this);
                productsRecyclerList.setAdapter(adapter);
            }
        });
    }

    // start using that viewModel
    void initViewModel(){
        viewModel =new ViewModelProvider(requireActivity()).get(AppViewModel.class);

    }

    @Override
    public void productClick(int ProductId) {

        Toast.makeText(this.getContext(),String.valueOf(ProductId),Toast.LENGTH_LONG).show();

    }
}