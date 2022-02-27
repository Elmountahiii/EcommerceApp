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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.auth.User;
import com.my.ecommerce.R;
import com.my.ecommerce.models.UserInfo;
import com.my.ecommerce.models.UserType;
import com.my.ecommerce.viewmodel.AppViewModel;

import java.util.ArrayList;

public class UserInformationFragment extends Fragment {

    AppViewModel viewModel;

    TextInputEditText fullName, email, city, country, addressOne, addressTwo, zip;
    Button continueButton;
    AutoCompleteTextView autoCompleteUserType;
    ArrayList<String> usersType=new ArrayList<>();



    public UserInformationFragment() {
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
        usersType.add("Buyer");
        usersType.add("Seller");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fullName = view.findViewById(R.id.FullNameInput);
        email = view.findViewById(R.id.EmailInput);
        country = view.findViewById(R.id.countryInput);
        city = view.findViewById(R.id.cityInput);
        addressOne = view.findViewById(R.id.addressOneInput);
        addressTwo = view.findViewById(R.id.addressTowInput);
        zip = view.findViewById(R.id.zipInput);
        continueButton = view.findViewById(R.id.buttonContinue);
        autoCompleteUserType = view.findViewById(R.id.autoCompleteTextView);

        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.drop_down_item, usersType);

        autoCompleteUserType.setAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_information, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkUserInformation()){
                    autoCompleteUserType.getText().toString();
                  int index =  usersType.indexOf(autoCompleteUserType.getText().toString());
                    UserInfo user;
                    if (index==0){
                        user = new UserInfo(fullName.getText().toString(), email.getText().toString(), city.getText().toString(), country.getText().toString(), addressOne.getText().toString(), addressTwo.getText().toString(), zip.getText().toString(), UserType.Buyer);


                    }else {
                        user = new UserInfo(fullName.getText().toString(), email.getText().toString(), city.getText().toString(), country.getText().toString(), addressOne.getText().toString(), addressTwo.getText().toString(), zip.getText().toString(), UserType.Seller);

                    }
                    viewModel.saveUserInformation(user);


                }
            }
        });

        viewModel.saveUserDataSuccess.observe(getViewLifecycleOwner(),aBoolean -> {
            if (aBoolean){
                NavHostFragment.findNavController(this).navigate(R.id.action_userInformationFragment_to_accountFragment);
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