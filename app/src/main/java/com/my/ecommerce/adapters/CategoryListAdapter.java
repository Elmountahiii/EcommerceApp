package com.my.ecommerce.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.my.ecommerce.R;
import com.my.ecommerce.models.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CategoryListAdapter  extends  RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder>{
    ArrayList<String> listOfColor= new ArrayList<>();
    List<Category> categoryList;



    public CategoryListAdapter(List<Category> categoryList){
        listOfColor.add("#D9D7F1");
        listOfColor.add("#4FBDBA");
        listOfColor.add("#FFC900");
        listOfColor.add("#2F3A8F");
        listOfColor.add("#8843F2");
        listOfColor.add("#93FFD8");
        listOfColor.add("#EA5C2B");
        listOfColor.add("#A3DA8D");
        listOfColor.add("#CDDEFF");


        this.categoryList=categoryList;

    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.singel_category_item, parent, false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.Bind(listOfColor.get(ThreadLocalRandom.current().nextInt(listOfColor.size())),categoryList.get(position).CategoryName);

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


    class CategoryViewHolder extends RecyclerView.ViewHolder{





        public MaterialCardView cardView ;
        public TextView categoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.itemCard);
            categoryName =itemView.findViewById(R.id.categoryName);
        }



        public void  Bind(String color,String name){

            this.categoryName.setText(name);
            this.cardView.setStrokeColor(Color.parseColor(color));

        }


    }


}
