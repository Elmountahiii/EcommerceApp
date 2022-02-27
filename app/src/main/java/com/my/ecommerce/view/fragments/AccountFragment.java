package com.my.ecommerce.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.my.ecommerce.R;
import com.my.ecommerce.viewmodel.AppViewModel;


public class AccountFragment extends Fragment {

    AppViewModel viewModel;

    TextView userName, userType;


    public AccountFragment() {
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
        userName=view.findViewById(R.id.textView16);
        userType=view.findViewById(R.id.textView17);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    // start using that viewModel
    void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);


    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.userInformation.observe(getViewLifecycleOwner(),userInfo -> {
            userName.setText(userInfo.fullName);
            userType.setText(userInfo.userType.name());
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }
}