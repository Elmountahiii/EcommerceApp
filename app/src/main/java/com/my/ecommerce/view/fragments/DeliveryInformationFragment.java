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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.my.ecommerce.R;
import com.my.ecommerce.viewmodel.AppViewModel;


public class DeliveryInformationFragment extends Fragment {


    AppViewModel viewModel;

    TextInputEditText fullName,email,city, country, addressOne,addressTwo,zip;
    ImageView backButton;
    Button confirmButton;

    public DeliveryInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        initViewModel();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backButton=view.findViewById(R.id.backimageView);
        fullName=view.findViewById(R.id.FullName);
        email=view.findViewById(R.id.Email);
        country=view.findViewById(R.id.country);
        city=view.findViewById(R.id.city);
        addressOne=view.findViewById(R.id.addressOne);
        addressTwo =view.findViewById(R.id.addressTow);
        zip=view.findViewById(R.id.zip);
        confirmButton=view.findViewById(R.id.buttonConfirm);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delivery_information, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();


        viewModel.userInformation.observe(getViewLifecycleOwner(),userInfo -> {
            if (userInfo!=null){
                fullName.setText(userInfo.fullName);
                email.setText(userInfo.email);
                country.setText(userInfo.country);
                city.setText(userInfo.city);
                addressOne.setText(userInfo.addressOne);
                addressTwo.setText(userInfo.addressTwo);
                zip.setText(userInfo.zip);
            }
        });


        backButton.setOnClickListener(new View.OnClickListener()    {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(DeliveryInformationFragment.this).popBackStack();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkUserInformation()){
                    NavHostFragment.findNavController(DeliveryInformationFragment.this).navigate(R.id.action_deliveryInformationFragment_to_productsSummeryFragment);

                }else {
                    Toast.makeText(getContext(),"Please Fill All Of Your Information",Toast.LENGTH_LONG).show();
                }


            }
        });
    }



    public Boolean checkUserInformation(){
        return !fullName.getText().toString().isEmpty() && !city.getText().toString().isEmpty() && !country.getText().toString().isEmpty() &&
                !addressOne.getText().toString().isEmpty() && !zip.getText().toString().isEmpty() && !email.getText().toString().isEmpty();
    }



    // start using that viewModel
    void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

    }
}