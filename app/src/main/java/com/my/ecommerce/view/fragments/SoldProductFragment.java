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
import com.my.ecommerce.adapters.SoldProductListAdapter;
import com.my.ecommerce.viewmodel.AppViewModel;


public class SoldProductFragment extends Fragment {

    AppViewModel viewModel;


    ImageView backImage;

    RecyclerView soldProductsList;

    public SoldProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        initViewModel();
        viewModel.getAllSellsProduct();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backImage = view.findViewById(R.id.imageView298th);
        soldProductsList = view.findViewById(R.id.soldProductList);


    }


    @Override
    public void onResume() {
        super.onResume();

        viewModel.soldProductList.observe(getViewLifecycleOwner(),productArrayList -> {

            SoldProductListAdapter adapter = new SoldProductListAdapter(productArrayList);

            soldProductsList.setAdapter(adapter);


        });
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(SoldProductFragment.this).popBackStack();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sold_product, container, false);
    }

    // start using that viewModel
    void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

    }
}