package com.my.ecommerce.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.ecommerce.R;
import com.my.ecommerce.repository.FirebaseRepository;
import com.my.ecommerce.viewmodel.AppViewModel;


public class HomeFragment extends Fragment {
    AppViewModel viewModel;


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
    public void onStart() {
        super.onStart();
        initViewModel();
    }


    @Override
    public void onResume() {
        super.onResume();

        viewModel.getProducts();
    }

    // start using that viewModel
    void initViewModel(){
        viewModel =new ViewModelProvider(requireActivity()).get(AppViewModel.class);

    }
}