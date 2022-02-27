package com.my.ecommerce.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.my.ecommerce.R;
import com.my.ecommerce.models.UserType;
import com.my.ecommerce.viewmodel.AppViewModel;


public class ProfileFragment extends Fragment {

    AppViewModel viewModel;



    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        initViewModel();


        if (viewModel.getUsertype()== UserType.Anonymous){
            NavHostFragment.findNavController(this).navigate(R.id.action_destination_account_to_signUpFragment);

        }else if (viewModel.getUsertype()== UserType.Buyer){

            NavHostFragment.findNavController(this).navigate(R.id.action_destination_account_to_accountFragment);


        }else if (viewModel.getUsertype()== UserType.Seller){
            NavHostFragment.findNavController(this).navigate(R.id.action_destination_account_to_accountFragment);


        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    // start using that viewModel
    void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

    }

}