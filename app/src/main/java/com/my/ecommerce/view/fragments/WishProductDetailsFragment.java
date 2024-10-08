package com.my.ecommerce.view.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.my.ecommerce.R;
import com.my.ecommerce.models.Product;
import com.my.ecommerce.viewmodel.AppViewModel;


public class WishProductDetailsFragment extends Fragment {

    private ImageView productImage;
    private TextView productTitle, productCategory, productPrice, productInformation, productFeatures,categoryTextView,reviewTextView,informationTextView,featuresTextView;
    private RatingBar ratingBar;
    private Button addToCartButton;
    private AppViewModel viewModel;
    private ProgressBar progressBar;
    private MaterialToolbar materialToolbar;

    public WishProductDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        initViewModel();

    }
    public void onResume() {
        super.onResume();



        viewModel.product.observe(getViewLifecycleOwner(), new Observer<Product>() {
            @Override
            public void onChanged(Product product) {
                if (product.title!=null){
                    bindProduct(product);
                    setViewsToBeVisible();
                }
            }
        });

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(WishProductDetailsFragment.this).popBackStack();
            }
        });

        addToCartButton.setOnClickListener(View ->{
            viewModel.savedProductToCart(viewModel.product.getValue().id);
            View.setBackgroundColor(Color.parseColor("#91C483"));
            addToCartButton.setText("successfully added to cart");
        });

        viewModel.addingProductToCartState.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(requireContext(),s,Toast.LENGTH_LONG).show();

            }
        });

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wish_product_details, container, false);
    }


    void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

    }

    void setViewsToBeVisible() {
        progressBar.setVisibility(View.INVISIBLE);
        productImage.setVisibility(View.VISIBLE);
        productTitle.setVisibility(View.VISIBLE);
        productCategory.setVisibility(View.VISIBLE);
        productPrice.setVisibility(View.VISIBLE);
        productInformation.setVisibility(View.VISIBLE);
        productFeatures.setVisibility(View.VISIBLE);
        addToCartButton.setVisibility(View.VISIBLE);
        ratingBar.setVisibility(View.VISIBLE);
        categoryTextView.setVisibility(View.VISIBLE);
        reviewTextView.setVisibility(View.VISIBLE);
        informationTextView.setVisibility(View.VISIBLE);
        featuresTextView.setVisibility(View.VISIBLE);

    }

    void bindProduct(Product product){
        productTitle.setText(product.title);
        Glide.with(requireActivity())
                .load(product.mainProductImage)
                .into(productImage);
        productPrice.setText(String.valueOf(product.price)+"€");
        productCategory.setText(product.category);
        productInformation.setText(product.productInformation);
        productFeatures.setText(product.features);

    }


    private void initViews(View view) {
        materialToolbar=view.findViewById(R.id.topAppBar2);
        productImage = view.findViewById(R.id.productDetailsImage2);
        productTitle = view.findViewById(R.id.productDetailsTitle2);
        productCategory = view.findViewById(R.id.productDetailsCategory2);
        productPrice = view.findViewById(R.id.productDetailsPrice2);
        productInformation = view.findViewById(R.id.productDetailsInformation2);
        productFeatures = view.findViewById(R.id.productDetailsFeatures2);
        addToCartButton = view.findViewById(R.id.productDetailsAddToCart2);
        ratingBar=view.findViewById(R.id.ratingBar2);
        categoryTextView=view.findViewById(R.id.textView22);
        reviewTextView=view.findViewById(R.id.textView52);
        informationTextView=view.findViewById(R.id.textView62);
        featuresTextView=view.findViewById(R.id.textView82);
        progressBar=view.findViewById(R.id.progressBar2);



    }

}