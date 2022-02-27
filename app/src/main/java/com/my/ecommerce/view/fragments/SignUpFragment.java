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


public class SignUpFragment extends Fragment {

    TextView signInLink;
    AppViewModel viewModel;


    TextInputEditText emailFiled, nameFiled, passwordFiled, secondPasswordFiled;
    Button singUpButton;
    TextInputLayout secondPasswordInputLayout,emailInputLayout;
    ProgressBar progressBar;



    public SignUpFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signInLink = view.findViewById(R.id.signin);
        nameFiled = view.findViewById(R.id.name);
        emailFiled = view.findViewById(R.id.email2);
        passwordFiled = view.findViewById(R.id.password2);
        secondPasswordFiled = view.findViewById(R.id.reenter_password);
        singUpButton = view.findViewById(R.id.signup_button);
        secondPasswordInputLayout=view.findViewById(R.id.reenter_password_textinputlayout);
        emailInputLayout=view.findViewById(R.id.email_textinputlayout2);
        progressBar=view.findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.INVISIBLE);




    }

    @Override
    public void onResume() {
        super.onResume();


        viewModel.singUpError.observe(getViewLifecycleOwner(),s -> {
            Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
        });
        
        viewModel.singUpSuccess.observe(getViewLifecycleOwner(),aBoolean -> {
            if (aBoolean){
                progressBar.setVisibility(View.INVISIBLE);
                NavHostFragment.findNavController(this).navigate(R.id.action_signUpFragment_to_userInformationFragment);
            }else {
                progressBar.setVisibility(View.INVISIBLE);

                Toast.makeText(getContext(), "error please try again", Toast.LENGTH_SHORT).show();
            }
        });
        signInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(SignUpFragment.this).navigate(R.id.action_signUpFragment_to_signInFragment);
            }
        });

        singUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkFiled()){
                    progressBar.setVisibility(View.VISIBLE);

                    viewModel.singUp(emailFiled.getText().toString(),passwordFiled.getText().toString());

                }else {
                    Log.d("botona", "onClick: la la azbi");

                }

            }
        });
    }


    private boolean checkFiled() {
        if (nameFiled.getText().toString().isEmpty() ||
                emailFiled.getText().toString().isEmpty() || passwordFiled.getText().toString().isEmpty()
                || secondPasswordFiled.getText().toString().isEmpty()
        ) {
            Toast.makeText(getContext(), "please fill all information", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            if (passwordFiled.getText().toString().equals(secondPasswordFiled.getText().toString())){
                secondPasswordInputLayout.setErrorEnabled(false);
                if(emailFiled.getText().toString().contains("@")){

                    emailInputLayout.setErrorEnabled(false);

                    return true;
                }else {
                    emailInputLayout.setErrorEnabled(true);

                    emailInputLayout.setError("Please inter a valid email");
                    return false;
                }

            }else {
                secondPasswordInputLayout.setErrorEnabled(true);
                secondPasswordInputLayout.setError("password not match");
                Toast.makeText(getContext(), "password not match", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }
    void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

    }
}