package com.nutrihealth.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nutrihealth.R;
import com.nutrihealth.fragments.TodayListFragment;
import com.nutrihealth.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Teo on 5/4/2018.
 */

public class DayDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int FIRST_CELL_VIEW_TYPE = 0;
    public static final int HEADER_VIEW_TYPE = 1;
    public static final int FOOD_VIEW_TYPE = 2;
    public static final int FOOTER_VIEW_TYPE = 3;

    private Context context;
    private int allKcal = 0;
    private int permKcal = 0;
    private boolean isFirstTime = true;

    private List<Product> breakfastList;
    private List<Product> firstSnackList;
    private List<Product> lunchList;
    private List<Product> secondSnackList;
    private List<Product> dinnerList;



    public DayDetailsAdapter(Context context, List<Product> breakfastList, List<Product> firstSnackList, List<Product> lunchList, List<Product> secondSnackList, List<Product> dinnerList, int allKcal, int permKcal) {
        this.context = context;
        this.breakfastList = breakfastList;
        this.firstSnackList = firstSnackList;
        this.lunchList = lunchList;
        this.secondSnackList = secondSnackList;
        this.dinnerList = dinnerList;
        this.allKcal = allKcal;
        this.permKcal = permKcal;
    }


    class FirstCellViewHolder extends RecyclerView.ViewHolder {

        TextView allKcalTv;
        TextView titleTv;
        ProgressBar progressBar;

        public FirstCellViewHolder(View itemView) {
            super(itemView);
            allKcalTv = itemView.findViewById(R.id.nr_kcal_remaining_tv);
            progressBar = itemView.findViewById(R.id.progressBar);
            titleTv = itemView.findViewById(R.id.title_tv);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView titleTv;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.title_tv);
        }
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {

        TextView productNameTv;
        TextView calNrTv;

        public FoodViewHolder(View itemView) {
            super(itemView);
            productNameTv = itemView.findViewById(R.id.product_name_tv);
            calNrTv = itemView.findViewById(R.id.product_cal_tv);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        ImageView addIconIv;
        TextView noFoodTv;

        public FooterViewHolder(View itemView) {
            super(itemView);
            addIconIv = itemView.findViewById(R.id.add_icon_iv);
            noFoodTv = itemView.findViewById(R.id.no_food_tv);
        }
    }

    public void updateLists(List<Product> list1, List<Product> list2, List<Product> list3, List<Product> list4, List<Product> list5, int allKcal, int permKcal) {

        breakfastList = new ArrayList<>();
        breakfastList = list1;

        firstSnackList = new ArrayList<>();
        firstSnackList = list2;

        lunchList = new ArrayList<>();
        lunchList = list3;

        secondSnackList = new ArrayList<>();
        secondSnackList = list4;

        dinnerList = new ArrayList<>();
        dinnerList = list5;

        this.allKcal = allKcal;
        this.permKcal = permKcal;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View firstCellView = LayoutInflater.from(context).inflate(R.layout.item_first_cell, parent, false);
        View headerView = LayoutInflater.from(context).inflate(R.layout.item_header, parent, false);
        View foodView = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        View footerView = LayoutInflater.from(context).inflate(R.layout.item_footer, parent, false);
        switch (viewType) {
            case FIRST_CELL_VIEW_TYPE:
                return new FirstCellViewHolder(firstCellView);
            case HEADER_VIEW_TYPE:
                return new HeaderViewHolder(headerView);
            case FOOD_VIEW_TYPE:
                return new FoodViewHolder(foodView);
            case FOOTER_VIEW_TYPE:
                return new FooterViewHolder(footerView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case FIRST_CELL_VIEW_TYPE:

                final FirstCellViewHolder firstCellViewHolder = (FirstCellViewHolder) holder;
                firstCellViewHolder.progressBar.setVisibility(View.VISIBLE);
                firstCellViewHolder.progressBar.setBackground(ContextCompat.getDrawable(context,R.drawable.circle_progress_background));
                int remaingKcal = permKcal - allKcal;
                if(remaingKcal < 0){
                    firstCellViewHolder.allKcalTv.setText((remaingKcal * -1) + " kcal");
                    firstCellViewHolder.allKcalTv.setTextColor(ContextCompat.getColor(context,R.color.red_dark));
                    firstCellViewHolder.progressBar.setProgressDrawable(ContextCompat.getDrawable(context,R.drawable.circle_progress_foreground_red));
                    ObjectAnimator progressAnimator;
                    progressAnimator = ObjectAnimator.ofInt(firstCellViewHolder.progressBar, "progress", 0,100);
                    progressAnimator.setDuration(500);
                    progressAnimator.setInterpolator(new LinearInterpolator());
                    progressAnimator.start();
                    firstCellViewHolder.titleTv.setText(context.getResources().getString(R.string.you_exceed));
                    firstCellViewHolder.titleTv.setTextColor(ContextCompat.getColor(context,R.color.red_dark));
                }else{
                    firstCellViewHolder.progressBar.setProgressDrawable(ContextCompat.getDrawable(context,R.drawable.circle_progress_foreground));
                    firstCellViewHolder.allKcalTv.setText(remaingKcal + " kcal");
                    firstCellViewHolder.allKcalTv.setTextColor(ContextCompat.getColor(context,R.color.light_blue));
                    firstCellViewHolder.titleTv.setText(context.getResources().getString(R.string.you_had));
                    firstCellViewHolder.titleTv.setTextColor(ContextCompat.getColor(context,R.color.light_blue));
                    double percentage = ( (double) allKcal/ (double) permKcal)*100.0;
                    ObjectAnimator progressAnimator;
                    progressAnimator = ObjectAnimator.ofInt(firstCellViewHolder.progressBar, "progress", 0,(int) percentage);
                    progressAnimator.setDuration(500);
                    progressAnimator.setInterpolator(new LinearInterpolator());
                    progressAnimator.start();
                   /* if (isFirstTime) {
                        ObjectAnimator progressAnimator;
                        progressAnimator = ObjectAnimator.ofInt(firstCellViewHolder.progressBar, "progress", 0,(int) percentage);
                        progressAnimator.setDuration(500);
                        progressAnimator.setInterpolator(new LinearInterpolator());
                        progressAnimator.start();
                        isFirstTime = false;
                    } else {
                        ObjectAnimator progressAnimator;
                        progressAnimator = ObjectAnimator.ofInt(firstCellViewHolder.progressBar, "progress", 0,(int) percentage);
                        progressAnimator.setDuration(500);
                        progressAnimator.setInterpolator(new LinearInterpolator());
                        progressAnimator.start();
                       //
                    } */
                }


                break;

            case HEADER_VIEW_TYPE:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                if (position == 1)
                    headerViewHolder.titleTv.setText(context.getResources().getString(R.string.breakfast));
                if (position == breakfastList.size() + 3)
                    headerViewHolder.titleTv.setText(context.getResources().getString(R.string.first_snack));
                if (position == breakfastList.size() + 2 + firstSnackList.size() + 3)
                    headerViewHolder.titleTv.setText(context.getResources().getString(R.string.lunch));
                if (position == breakfastList.size() + 2 + firstSnackList.size() + 2 + lunchList.size() + 3)
                    headerViewHolder.titleTv.setText(context.getResources().getString(R.string.second_snack));
                if (position == breakfastList.size() + 2 + firstSnackList.size() + 2 + lunchList.size() + 2 + secondSnackList.size() + 3)
                    headerViewHolder.titleTv.setText(context.getResources().getString(R.string.diner));

                break;

            case FOOD_VIEW_TYPE:
                 FoodViewHolder foodViewHolder = (FoodViewHolder) holder;
                Product product = null;
                if (position > 1 && position < breakfastList.size() + 2) {
                    product = breakfastList.get(position - 2);
                }

                if (position > (breakfastList.size() + 3) && position < (breakfastList.size() + 2 + firstSnackList.size() + 2)) {
                    product = firstSnackList.get(position - breakfastList.size() - 4);
                }
                if (position > (breakfastList.size() + 2 + firstSnackList.size() + 3) && position < (breakfastList.size() + 2 + firstSnackList.size() + 2 + lunchList.size() + 2)) {
                    product = lunchList.get(position - breakfastList.size() - firstSnackList.size() - 6);
                }
                if (position > (breakfastList.size() + 2 + firstSnackList.size() + 2 + lunchList.size() + 3) && position < (breakfastList.size() + 2 + firstSnackList.size() + 2 + lunchList.size() + 2 + secondSnackList.size() + 2)) {
                    product = secondSnackList.get(position - breakfastList.size() - firstSnackList.size() - lunchList.size() - 8);
                }
                if (position > (breakfastList.size() + 2 + firstSnackList.size() + 2 + lunchList.size() + 2 + secondSnackList.size() + 3) && position < (breakfastList.size() + 2 + firstSnackList.size() + 2 + lunchList.size() + 2 + secondSnackList.size() + 2 + dinnerList.size() + 2)) {
                    product = dinnerList.get(position - breakfastList.size() - firstSnackList.size() - lunchList.size() - secondSnackList.size() - 10);
                }
                foodViewHolder.productNameTv.setText(product.getName());
                foodViewHolder.calNrTv.setText(product.getKcal() + " kcal");

                break;

            case FOOTER_VIEW_TYPE:
                FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
                footerViewHolder.addIconIv.setVisibility(View.GONE);
                footerViewHolder.noFoodTv.setVisibility(View.GONE);
                if (position == breakfastList.size() + 2)
                    if(breakfastList.size() == 0){
                      footerViewHolder.noFoodTv.setVisibility(View.VISIBLE);
                    }
                if (position == breakfastList.size() + 2 + firstSnackList.size() + 2)
                    if(firstSnackList.size() == 0){
                        footerViewHolder.noFoodTv.setVisibility(View.VISIBLE);
                    }
                if (position == breakfastList.size() + 2 + firstSnackList.size() + 2 + lunchList.size() + 2)
                    if(lunchList.size() == 0){
                        footerViewHolder.noFoodTv.setVisibility(View.VISIBLE);
                    }
                if (position == breakfastList.size() + 2 + firstSnackList.size() + 2 + lunchList.size() + 2 + secondSnackList.size() + 2)
                    if(secondSnackList.size() == 0){
                        footerViewHolder.noFoodTv.setVisibility(View.VISIBLE);
                    }
                if (position == breakfastList.size() + 2 + firstSnackList.size() + 2 + lunchList.size() + 2 + secondSnackList.size() + 2 + dinnerList.size() + 2)
                    if(dinnerList.size() == 0){
                        footerViewHolder.noFoodTv.setVisibility(View.VISIBLE);
                    }
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0)
            return FIRST_CELL_VIEW_TYPE;
        if (position == 1)
            return HEADER_VIEW_TYPE;
        if (position == breakfastList.size() + 3)
            return HEADER_VIEW_TYPE;
        if (position == breakfastList.size() + 2 + firstSnackList.size() + 3)
            return HEADER_VIEW_TYPE;
        if (position == breakfastList.size() + 2 + firstSnackList.size() + 2 + lunchList.size() + 3)
            return HEADER_VIEW_TYPE;
        if (position == breakfastList.size() + 2 + firstSnackList.size() + 2 + lunchList.size() + 2 + secondSnackList.size() + 3)
            return HEADER_VIEW_TYPE;

        if (position == breakfastList.size() + 2)
            return FOOTER_VIEW_TYPE;
        if (position == breakfastList.size() + 2 + firstSnackList.size() + 2)
            return FOOTER_VIEW_TYPE;
        if (position == breakfastList.size() + 2 + firstSnackList.size() + 2 + lunchList.size() + 2)
            return FOOTER_VIEW_TYPE;
        if (position == breakfastList.size() + 2 + firstSnackList.size() + 2 + lunchList.size() + 2 + secondSnackList.size() + 2)
            return FOOTER_VIEW_TYPE;
        if (position == breakfastList.size() + 2 + firstSnackList.size() + 2 + lunchList.size() + 2 + secondSnackList.size() + 2 + dinnerList.size() + 2)
            return FOOTER_VIEW_TYPE;

        if (position > 1 && position < breakfastList.size() + 2) {
            return FOOD_VIEW_TYPE;
        }

        if (position > (breakfastList.size() + 3) && position < (breakfastList.size() + 2 + firstSnackList.size() + 2)) {
            return FOOD_VIEW_TYPE;
        }
        if (position > (breakfastList.size() + 2 + firstSnackList.size() + 3) && position < (breakfastList.size() + 2 + firstSnackList.size() + 2 + lunchList.size() + 2)) {
            return FOOD_VIEW_TYPE;
        }
        if (position > (breakfastList.size() + 2 + firstSnackList.size() + 2 + lunchList.size() + 3) && position < (breakfastList.size() + 2 + firstSnackList.size() + 2 + lunchList.size() + 2 + secondSnackList.size() + 2)) {
            return FOOD_VIEW_TYPE;
        }
        if (position > (breakfastList.size() + 2 + firstSnackList.size() + 2 + lunchList.size() + 2 + secondSnackList.size() + 3) && position < (breakfastList.size() + 2 + firstSnackList.size() + 2 + lunchList.size() + 2 + secondSnackList.size() + 2 + dinnerList.size() + 2)) {
            return FOOD_VIEW_TYPE;
        }

        return HEADER_VIEW_TYPE;


    }

    @Override
    public int getItemCount() {

        return breakfastList.size() + firstSnackList.size() + lunchList.size() + secondSnackList.size() + dinnerList.size() + 2 * 5 + 1;
    }
}
