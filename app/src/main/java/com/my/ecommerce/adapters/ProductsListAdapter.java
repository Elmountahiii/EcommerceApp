package com.my.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.my.ecommerce.R;
import com.my.ecommerce.interfaces.OnProductClickListener;
import com.my.ecommerce.models.Product;

import java.util.List;

public class ProductsListAdapter extends RecyclerView.Adapter<ProductsListAdapter.ProductViewHolder> {

    List<Product> productList;
    OnProductClickListener onProductClickListener;



    public ProductsListAdapter(List<Product>products,OnProductClickListener onProductClickListener){
        this.productList=products;
        this.onProductClickListener=onProductClickListener;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_product_item_layout, parent, false);
        return new ProductViewHolder(view,parent.getContext(),onProductClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(productList.get(position));


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{


        private TextView title,price,category;

        private ImageView productImage;
        private Context context;
        OnProductClickListener onProductClickListener;


        public ProductViewHolder(@NonNull View itemView, Context context,OnProductClickListener onProductClickListener) {
            super(itemView);

            this.title=itemView.findViewById(R.id.productTitle);
            this.price=itemView.findViewById(R.id.productPrice);
            this.category=itemView.findViewById(R.id.productCategory);
            this.productImage=itemView.findViewById(R.id.productImage);
            this.context=context;
            this.onProductClickListener=onProductClickListener;
        }


        public void bind(Product product){
            title.setText(product.title);
            price.setText(String.valueOf(product.price)+"€");
            Glide.with(context)
                    .load(product.mainProductImage)
                    .into(productImage);

            category.setText(product.category);
            title.setOnClickListener(View->{
                onProductClickListener.productClick(productList.get(getAdapterPosition()).id);

            });

            productImage.setOnClickListener(view -> {

              onProductClickListener.productClick(productList.get(getAdapterPosition()).id);
            });

        }
    }
}
