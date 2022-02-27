package com.my.ecommerce.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.my.ecommerce.R;
import com.my.ecommerce.viewmodel.AppViewModel;


public class SignInFragment extends Fragment {

    AppViewModel viewModel;

    TextView signUpLink;
    TextInputEditText emailFiled, passwordFiled;
    TextInputLayout passwordInputLayout, emailInputLayout;
    Button signInButton;
    ProgressBar progressBar;


    public SignInFragment() {
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
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signUpLink = view.findViewById(R.id.signup);
        signInButton = view.findViewById(R.id.continue_button);
        emailFiled = view.findViewById(R.id.email);
        passwordFiled = view.findViewById(R.id.password);
        emailInputLayout = view.findViewById(R.id.email_textinputlayout);
        passwordInputLayout = view.findViewById(R.id.password_textinputlayout);
        progressBar=view.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);


    }

    @Override
    public void onResume() {
        super.onResume();

        viewModel.singInError.observe(getViewLifecycleOwner(),s -> {
            Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
        });


        viewModel.singInSuccess.observe(getViewLifecycleOwner(),aBoolean -> {
            if (aBoolean){
                progressBar.setVisibility(View.INVISIBLE);
                NavHostFragment.findNavController(SignInFragment.this).navigate(R.id.action_signInFragment_to_accountFragment);
            }else {
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                NavHostFragment.findNavController(SignInFragment.this).popBackStack();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkFiled()) {

                    viewModel.singIn(emailFiled.getText().toString(),passwordFiled.getText().toString());
                } else {
                    Log.d("botona", "onClick: la la azbi");

                }
            }
        });
    }

    private boolean checkFiled() {
        if (
                emailFiled.getText().toString().isEmpty() || passwordFiled.getText().toString().isEmpty()

        ) {
            Toast.makeText(getContext(), "please fill all information", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (emailFiled.getText().toString().contains("@")) {

                emailInputLayout.setErrorEnabled(false);

                return true;
            } else {
                emailInputLayout.setErrorEnabled(true);

                emailInputLayout.setError("Please inter a valid email");
                return false;
            }
        }
    }

    void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

    }

}