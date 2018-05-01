package com.nutrihealth.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nutrihealth.R;
import com.nutrihealth.api.Movie;
import com.squareup.picasso.Picasso;


import java.util.List;
import java.util.Random;

/**
 * Created by Teo on 7/12/2017.
 */

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {

    private List<Movie> dataModelList;
    private Context context;


    public TestAdapter(List<Movie> dataModelList) {
        this.dataModelList = dataModelList;
    }

    @Override
    public int getItemViewType(int position) {
        return (position % 3);
    }

    @Override
    public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        switch (viewType){
        case 0:
            return new TestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false));
        case 1:
            return new TestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_small, parent, false));
        case 2:
            return new TestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_small, parent, false));
            default: return null;
        }

    }

    @Override
    public void onBindViewHolder(TestViewHolder holder, int position) {

        final Movie customObject = dataModelList.get(position);
        holder.title1.setText(customObject.title);
        holder.title2.setText(customObject.title);
        holder.description.setText(customObject.description);
        holder.nrLikes.setText(customObject.nrLikes.toString());

        Picasso.with(context)
                .load(customObject.getImageUrl())
                .fit()
                .centerInside()
                .into(holder.image);




    }

    @Override
    public int getItemCount() {
        return dataModelList != null ? dataModelList.size() : 0;
    }

    public class TestViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title1,title2, description, nrLikes;
        public RelativeLayout mapButton;


        public TestViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image);
            title1 = (TextView) itemView.findViewById(R.id.title);
            title2 = (TextView) itemView.findViewById(R.id.textView);
            description = (TextView) itemView.findViewById(R.id.info);
            nrLikes = (TextView) itemView.findViewById(R.id.nrLikes);
            mapButton = (RelativeLayout) itemView.findViewById(R.id.mapButton);

        }
    }
}
