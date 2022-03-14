package com.my.ecommerce.view.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.my.ecommerce.R;
import com.my.ecommerce.viewmodel.AppViewModel;

import java.util.ArrayList;

public class UploadProductFragment extends Fragment {


    AppViewModel viewModel;
    
    ProgressBar progressBar7;


    ImageView backButton, mainProductImage, selectImageButton;
    Button uploadButton;
    TextInputEditText productTitleInput, productInformationInput, productFeaturesInput, productPriceInput;
    AutoCompleteTextView categoryAutoComplete;

    ArrayList<String> categoriesList = new ArrayList<>();

    Uri productImageURI;


    public UploadProductFragment() {
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
        if (categoriesList.isEmpty()) {
            categoriesList = viewModel.listOfCategoriesString;
        }
        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.drop_down_item, categoriesList);
        categoryAutoComplete.setAdapter(adapter);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_product, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uploadButton = view.findViewById(R.id.UploadProductButton);
        productTitleInput = view.findViewById(R.id.tileProduct);
        productFeaturesInput = view.findViewById(R.id.PrdFeatures);
        productInformationInput = view.findViewById(R.id.PrdInformation);
        mainProductImage = view.findViewById(R.id.productImageUpload);
        selectImageButton = view.findViewById(R.id.SelectImageForUpload);
        backButton = view.findViewById(R.id.imageView98b);
        categoryAutoComplete = view.findViewById(R.id.categorySelect);
        productPriceInput = view.findViewById(R.id.PrdPric);
        progressBar7=view.findViewById(R.id.progressBar7);


    }


    @Override
    public void onResume() {
        super.onResume();
        
        
        viewModel.uploadingProductState.observe(getViewLifecycleOwner(),aBoolean -> {
            if (aBoolean){
                Toast.makeText(getContext(), "the product uploaded successfully", Toast.LENGTH_SHORT).show();
                uploadButton.setActivated(true);
                NavHostFragment.findNavController(this).popBackStack();
            }else {
                Toast.makeText(getContext(), "Error Please try again", Toast.LENGTH_SHORT).show();
                uploadButton.setActivated(true);

            }
        });
        mainProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotoPicker();
            }
        });
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotoPicker();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAllInputs()) {
                    progressBar7.setVisibility(View.VISIBLE);
                    
                    v.setActivated(false);
                    Uri productUri = productImageURI;
                    String productTile = productTitleInput.getText().toString();
                    String category = categoryAutoComplete.getText().toString();
                    String features = productFeaturesInput.getText().toString();
                    String information = productInformationInput.getText().toString();
                    String price = productPriceInput.getText().toString();
                    viewModel.uploadProduct(productUri, productTile, information, features, category, price);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(UploadProductFragment.this).popBackStack();
            }
        });
    }

    // start using that viewModel
    void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

    }


    private void openPhotoPicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);


    }

    private boolean checkAllInputs() {
        if (productPriceInput.getText().toString().isEmpty() && productTitleInput.getText().toString().isEmpty() && productFeaturesInput.getText().toString().isEmpty()
                && productInformationInput.getText().toString().isEmpty() && categoryAutoComplete.getText().toString().isEmpty() && productImageURI == null) {
            Toast.makeText(getContext(), "please fill all fields correctly", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK && requestCode == 10) {


            productImageURI = data.getData();
            mainProductImage.setImageURI(productImageURI);

        }
    }

}