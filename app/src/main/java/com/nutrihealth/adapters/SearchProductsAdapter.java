package com.nutrihealth.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nutrihealth.R;
import com.nutrihealth.api.response.ProductItem;

import java.util.List;

/**
 * Created by Teo on 5/12/2018.
 */

public class SearchProductsAdapter extends RecyclerView.Adapter  {

    private List<ProductItem> productItemList;
    private Context context;
    private ProductsListener listener;

    public SearchProductsAdapter(Context context, List<ProductItem> productItemList){
        this.context = context;
        this.productItemList = productItemList;
    }

    public interface ProductsListener{
        void onProductPressed(int position);
    }

    public void setListener(ProductsListener listener){
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
        productItemViewHolder.productNameTv.setText(productItemList.get(position).name);
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
        return productItemList != null ? productItemList.size() : 0;
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
