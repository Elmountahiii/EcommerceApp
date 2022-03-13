package com.my.ecommerce.view.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.my.ecommerce.R;
import com.my.ecommerce.viewmodel.AppViewModel;

import java.text.DecimalFormat;


public class ProductsSummeryFragment extends Fragment {

    AppViewModel viewModel;


    ImageView checkImage,backImage;

    TextView productSummery,totalPrice;




    public ProductsSummeryFragment() {
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
        return inflater.inflate(R.layout.fragment_products_summery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkImage=view.findViewById(R.id.chckImage);
        productSummery=view.findViewById(R.id.ProductSummeryTxt);
        totalPrice=view.findViewById(R.id.ProductSummeryTotal);
        backImage=view.findViewById(R.id.imageViewSummery);
        Glide.with(this)
                .load("https://cdn.dribbble.com/users/1751799/screenshots/5512482/media/1cbd3594bb5e8d90924a105d4aae924c.gif")
                .into(new DrawableImageViewTarget(checkImage) {
                    @Override public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        if (resource instanceof GifDrawable) {
                            ((GifDrawable)resource).setLoopCount(1);
                        }
                        super.onResourceReady(resource, transition);
                    }
                });
    }


    @Override
    public void onResume() {
        super.onResume();

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ProductsSummeryFragment.this).popBackStack();
            }
        });

        for (int i = 0; i < viewModel.listOfCartProduct.getValue().size(); i++) {

            String title="\n"+"- "+viewModel.listOfCartProduct.getValue().get(i).title+".";

            productSummery.setText(title+"\n"+productSummery.getText());


        }

        totalPrice.setText(String.valueOf(new DecimalFormat("##.##").format(viewModel.totalCartPrice.getValue()))+"€");


         viewModel.removeCardItems();
         viewModel.savedToPastPurchase(viewModel.listOfCartProduct.getValue());

    }
    // start using that viewModel
    void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

    }

}