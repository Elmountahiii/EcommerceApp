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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.my.ecommerce.R;
import com.my.ecommerce.viewmodel.AppViewModel;


public class ChangePasswordFragment extends Fragment {


    TextInputEditText oldPassword, newPassword;
    Button confirmButton;
    ProgressBar progressBar;

    ImageView backImage;
    AppViewModel viewModel;



    public ChangePasswordFragment() {
        // Required empty public constructor
    }
    @Override
    public void onStart() {
        super.onStart();
        initViewModel();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        oldPassword = view.findViewById(R.id.old_password);
        newPassword = view.findViewById(R.id.new_password);
        confirmButton = view.findViewById(R.id.button3);
        progressBar =view.findViewById(R.id.progressBar4);
        backImage = view.findViewById(R.id.imageView2);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPassword()){
                    progressBar.setVisibility(View.VISIBLE);

                    viewModel.changePassword(oldPassword.getText().toString(),newPassword.getText().toString());

                }
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ChangePasswordFragment.this).popBackStack();
            }
        });
    }

    private boolean checkPassword(){
        if (!oldPassword.getText().toString().isEmpty()&&!newPassword.getText().toString().isEmpty()){
            return true;
        }else {
            Toast.makeText(getContext(), "The Password Is empty", Toast.LENGTH_SHORT).show();
            return  false;
        }
    }

    // start using that viewModel
    void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

    }
}