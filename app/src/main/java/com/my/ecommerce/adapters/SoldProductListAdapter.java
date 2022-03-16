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
import com.my.ecommerce.models.Product;

import java.util.List;

public class SoldProductListAdapter  extends RecyclerView.Adapter<SoldProductListAdapter.SoldProductViewHolder>{

    List<Product> productList;

    public SoldProductListAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public SoldProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sold_product_item_layout, parent, false);
        return new SoldProductViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull SoldProductViewHolder holder, int position) {
        holder.bind(productList.get(position));

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class SoldProductViewHolder extends RecyclerView.ViewHolder {

        private TextView title, times, forTotal;
        private ImageView productImage;
        private Context context;

        public SoldProductViewHolder(@NonNull View itemView,Context context) {
            super(itemView);

            title=itemView.findViewById(R.id.soldProductTitle);
            times=itemView.findViewById(R.id.soldProductHowManyTime);
            forTotal=itemView.findViewById(R.id.soldProductForTotal);
            productImage=itemView.findViewById(R.id.soldProductImage);
            this.context=context;

        }


        public void bind(Product product){
            title.setText(product.title);
            times.setText(product.selleCount+" Time");
            forTotal.setText("for total of : "+product.price*product.selleCount);

            Glide.with(context)
                    .load(product.mainProductImage).into(productImage);
        }
    }
}
