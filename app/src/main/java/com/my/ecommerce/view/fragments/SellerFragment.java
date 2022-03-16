package com.my.ecommerce.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.my.ecommerce.R;
import com.my.ecommerce.viewmodel.AppViewModel;


public class SellerFragment extends Fragment {


    AppViewModel viewModel;

    TextView uploadProduct, currentProducts, pastProducts, amount, changePassword, logoOut, userFullName;
    ImageView profileImage;

    ProgressBar progressBar6;

    public SellerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        initViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uploadProduct = view.findViewById(R.id.add_item);
        currentProducts = view.findViewById(R.id.view_current_sells);
        pastProducts = view.findViewById(R.id.view_past_sells);
        amount = view.findViewById(R.id.view_amount);
        changePassword = view.findViewById(R.id.seller_change_password);
        logoOut = view.findViewById(R.id.seller_logoout);
        userFullName=view.findViewById(R.id.seller_user_full_name);
        profileImage=view.findViewById(R.id.seller_profile_image);
        progressBar6=view.findViewById(R.id.progressBar6);
    }


    @Override
    public void onResume() {
        super.onResume();


        Glide.with(getContext())
                .load(viewModel.userInformation.getValue().profileImage)
                .into(profileImage);


        currentProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(SellerFragment.this).navigate(R.id.action_sellerFragment_to_currentSellsProductsFragment);
            }
        });
        userFullName.setText(viewModel.userInformation.getValue().fullName);

        uploadProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(SellerFragment.this).navigate(R.id.action_sellerFragment_to_uploadProductFragment);
            }
        });


        logoOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar6.setVisibility(View.VISIBLE);
                viewModel.logout();
                NavHostFragment.findNavController(SellerFragment.this).navigate(R.id.action_buyerFragment_to_signInFragment);
            }
        });


        amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(SellerFragment.this).navigate(R.id.action_sellerFragment_to_amountFragment);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavHostFragment.findNavController(SellerFragment.this).navigate(R.id.action_sellerFragment_to_changePasswordFragment);

            }
        });
    }

    // start using that viewModel
    void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

    }

}