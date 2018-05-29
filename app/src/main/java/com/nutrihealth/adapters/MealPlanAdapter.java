package com.nutrihealth.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nutrihealth.R;
import com.nutrihealth.api.response.MealPlanFood;
import com.nutrihealth.api.response.ProductItem;

import java.util.List;

/**
 * Created by Teo on 5/29/2018.
 */

public class MealPlanAdapter extends RecyclerView.Adapter {

    private List<MealPlanFood> foodItemList;
    private Context context;
    private FoodListener listener;

    public MealPlanAdapter(Context context, List<MealPlanFood> foodList){
        this.context = context;
        this.foodItemList = foodList;
    }

    public interface FoodListener{
        void onProductPressed(int position);
    }

    public void setListener(FoodListener listener){
        this.listener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View productView = LayoutInflater.from(context).inflate(R.layout.item_products, parent, false);

        return new ProductItemViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ProductItemViewHolder productItemViewHolder = (ProductItemViewHolder) holder;
        productItemViewHolder.productNameTv.setText(foodItemList.get(position).foodName);
        productItemViewHolder.parentRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onProductPressed(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodItemList != null ? foodItemList.size() : 0;
    }



    public class ProductItemViewHolder extends RecyclerView.ViewHolder {

        public TextView productNameTv;
        public RelativeLayout parentRl;


        public ProductItemViewHolder(View itemView) {
            super(itemView);
            productNameTv = itemView.findViewById(R.id.product_name_tv);
            parentRl = itemView.findViewById(R.id.parent_container);


        }
    }
}
