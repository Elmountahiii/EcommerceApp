package com.my.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.ecommerce.R;
import com.my.ecommerce.interfaces.OnCartProductRemovedListener;
import com.my.ecommerce.models.Product;

import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.CartAdapterViewHolder> {
    OnCartProductRemovedListener onCartProductRemovedListener;
    private final List<Product> productList;


     public CartListAdapter(List<Product>products,OnCartProductRemovedListener onCartProductRemovedListener){

         this.onCartProductRemovedListener=onCartProductRemovedListener;
         this.productList=products;

    }

    @NonNull
    @Override
    public CartAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_cart_product_item, parent, false);
        return new CartAdapterViewHolder(view,parent.getContext(),onCartProductRemovedListener);

    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapterViewHolder holder, int position) {
         holder.bind(productList.get(position));

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class CartAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage, removeProduct, addProductCount, minusProductCount;
        TextView productTitle, productPrice, productCount;
        Context context;
        OnCartProductRemovedListener onCartProductRemovedListener;

        public CartAdapterViewHolder(@NonNull View itemView, Context context, OnCartProductRemovedListener onCartProductRemovedListener) {
            super(itemView);
            this.productCount = itemView.findViewById(R.id.CartProductCount);
            this.addProductCount = itemView.findViewById(R.id.CartProductAddButton);
            this.minusProductCount = itemView.findViewById(R.id.CartProductMinusButton);
            this.productPrice = itemView.findViewById(R.id.CartProductPrice);
            this.productTitle = itemView.findViewById(R.id.CartProductTitle);
            this.productImage = itemView.findViewById(R.id.CartProductImage);
            this.removeProduct = itemView.findViewById(R.id.CartProductRemove);
            this.context=context;
            this.onCartProductRemovedListener=onCartProductRemovedListener;


        }


        void bind(Product product) {

            Glide.with(context)
                    .load(product.mainProductImage)
                    .into(productImage);
            productCount.setText("1");
            productPrice.setText(String.valueOf(product.price)+"€");
            productTitle.setText(product.title);
            removeProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCartProductRemovedListener.onProductRemoved(getAdapterPosition());
                }
            });


            addProductCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int localProductCount=Integer.parseInt(productCount.getText().toString());
                    localProductCount++;
                    productCount.setText(String.valueOf(localProductCount));
                    onCartProductRemovedListener.addToPrice(product.price);
                }
            });
            minusProductCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int localProductCount=Integer.parseInt(productCount.getText().toString());

                    if (localProductCount==1){
                        productCount.setText("1");
                    }else {
                        localProductCount--;
                        productCount.setText(String.valueOf(localProductCount));
                        onCartProductRemovedListener.minusFromPrice(product.price);
                    }
                }
            });



        }
    }

}
