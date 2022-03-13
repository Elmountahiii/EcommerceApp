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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.my.ecommerce.R;
import com.my.ecommerce.viewmodel.AppViewModel;

import de.hdodenhof.circleimageview.CircleImageView;


public class BuyerFragment extends Fragment {

    CircleImageView userImage;
    TextView userName, pastPurchase,wishList,changePassword,logout;
    AppViewModel viewModel;
    ProgressBar progressBar;



    public BuyerFragment() {
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userImage= view.findViewById(R.id.buyer_profile_image);
        userName =view.findViewById(R.id.buyer_user_full_name);
        pastPurchase = view.findViewById(R.id.Buyer_past_purchase);
        wishList =view.findViewById(R.id.buyer_wish_list);
        changePassword=view.findViewById(R.id.buyer_change_password);
        logout=view.findViewById(R.id.buyer_logout);
        progressBar=view.findViewById(R.id.progressBar5);



    }


    @Override
    public void onResume() {
        super.onResume();



        viewModel.userInformation.observe(getViewLifecycleOwner(),userInfo -> {

            userName.setText(userInfo.fullName);
            Glide.with(requireContext())
                    .load(userInfo.profileImage)
                    .into(userImage);
        });

        pastPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(BuyerFragment.this).navigate(R.id.action_buyerFragment_to_pastPurchaseFragment);
            }
        });

        wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(BuyerFragment.this).navigate(R.id.action_buyerFragment_to_wishListFragment);

            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavHostFragment.findNavController(BuyerFragment.this).navigate(R.id.action_buyerFragment_to_changePasswordFragment);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                  viewModel.logout();
                NavHostFragment.findNavController(BuyerFragment.this).navigate(R.id.action_buyerFragment_to_signInFragment);

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buyer, container, false);
    }

    // start using that viewModel
    void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

    }
}