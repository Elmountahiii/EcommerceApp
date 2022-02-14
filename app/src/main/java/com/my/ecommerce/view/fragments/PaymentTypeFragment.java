package com.my.ecommerce.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.my.ecommerce.R;


public class PaymentTypeFragment extends Fragment {


    ImageView backButton;
    Button payOnDelivery, payWithCard;


    public PaymentTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_type, container, false);


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backButton=view.findViewById(R.id.imageView);
        payWithCard=view.findViewById(R.id.button);
        payOnDelivery= view.findViewById(R.id.button2);

    }

    @Override
    public void onResume() {
        super.onResume();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(PaymentTypeFragment.this).popBackStack();
            }
        });
        payOnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(PaymentTypeFragment.this).navigate(R.id.action_paymentTypeFragment_to_deliveryInformationFragment);
            }
        });
    }
}