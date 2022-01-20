package com.my.ecommerce.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.my.ecommerce.R;
import com.my.ecommerce.adapters.CartListAdapter;
import com.my.ecommerce.interfaces.OnCartProductRemovedListener;
import com.my.ecommerce.models.Product;
import com.my.ecommerce.viewmodel.AppViewModel;

import java.text.DecimalFormat;
import java.util.List;


public class ShoppingCartFragment extends Fragment implements OnCartProductRemovedListener {

    AppViewModel viewModel;

    RecyclerView cartRecyclerView;

    TextView totalPrice;


    public ShoppingCartFragment() {
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
        initViews(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_cart, container, false);
    }

    void initViews(View view) {

        cartRecyclerView = view.findViewById(R.id.ShoppingCartProductsList);
        totalPrice = view.findViewById(R.id.ShoppingCartTotalPrice);


    }


    @Override
    public void onResume() {
        super.onResume();
        viewModel.listOfCartProduct.observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {


                CartListAdapter adapter=new CartListAdapter(products,ShoppingCartFragment.this);
                cartRecyclerView.setAdapter(adapter);

              }
        });
        viewModel.totalCartPrice.observe(getViewLifecycleOwner(), new Observer<Float>() {
            @Override
            public void onChanged(Float aFloat) {

                totalPrice.setText(String.valueOf(new DecimalFormat("##.##").format(aFloat)));


            }
        });

    }

    // start using that viewModel
    void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

    }


    @Override
    public void onProductRemoved(int productPosition) {

      viewModel.removeProductFromCart(productPosition);
    }


    @Override
    public void addToPrice(float priceToBeAdd) {


        viewModel.addToPrice(priceToBeAdd);


    }

    @Override
    public void minusFromPrice(float priceToBeMinus) {

        viewModel.minusFromPrice(priceToBeMinus);

    }

}