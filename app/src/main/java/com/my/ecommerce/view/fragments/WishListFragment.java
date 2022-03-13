package com.my.ecommerce.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.my.ecommerce.R;
import com.my.ecommerce.adapters.ProductsListAdapter;
import com.my.ecommerce.interfaces.OnProductClickListener;
import com.my.ecommerce.viewmodel.AppViewModel;


public class WishListFragment extends Fragment implements OnProductClickListener {

    ImageView backImage;
    AppViewModel viewModel;


    RecyclerView wishListList;




    public WishListFragment() {
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
        viewModel.getWishList();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backImage = view.findViewById(R.id.imageView98);
        wishListList = view.findViewById(R.id.wish_List);
    }

    @Override
    public void onResume() {
        super.onResume();


        viewModel.wishListItems.observe(getViewLifecycleOwner(), productArrayList -> {

            Log.d("blani", "onResume: "+productArrayList.size());

            ProductsListAdapter adapter = new ProductsListAdapter(productArrayList, WishListFragment.this);
            wishListList.setAdapter(adapter);


        });
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(WishListFragment.this).popBackStack();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wish_list, container, false);
    }

    // start using that viewModel
    void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

    }

    @Override
    public void productClick(int ProductId) {
        viewModel.getProductById(ProductId);
        NavHostFragment.findNavController(this).navigate(R.id.action_wishListFragment_to_wishProductDetailsFragment);


    }
}