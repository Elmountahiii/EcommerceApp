package com.my.ecommerce.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.my.ecommerce.R;
import com.my.ecommerce.adapters.ProductsListAdapter;
import com.my.ecommerce.interfaces.OnProductClickListener;
import com.my.ecommerce.viewmodel.AppViewModel;


public class CurrentSellsProductsFragment extends Fragment implements OnProductClickListener {
    AppViewModel viewModel;


    ImageView backImage;

    RecyclerView CurrentSellsProductsList;

    public CurrentSellsProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        initViewModel();
        viewModel.getAllUserSellsProduct();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backImage = view.findViewById(R.id.imageView298t);
        CurrentSellsProductsList = view.findViewById(R.id.past_Current_Sells_List);


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onResume() {
        super.onResume();


        //listOfUserProduct


        viewModel.listOfUserProduct.observe(getViewLifecycleOwner(),productArrayList -> {

            ProductsListAdapter adapter = new ProductsListAdapter(productArrayList, CurrentSellsProductsFragment.this);

            CurrentSellsProductsList.setAdapter(adapter);


        });
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(CurrentSellsProductsFragment.this).popBackStack();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_sells_products, container, false);
    }

    // start using that viewModel
    void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

    }

    @Override
    public void productClick(int ProductId) {

    }
}