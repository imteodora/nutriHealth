package com.nutrihealth.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.nutrihealth.R;
import com.nutrihealth.databinding.ItemNavigationDrawerBinding;
import com.nutrihealth.listeners.DrawerItemListener;


/**
 * Created by erkut on 10/20/17.
 */

public class NavigationDrawerAdapter extends ArrayAdapter {

    public enum DrawerItem {
        ALARMS(R.string.alarms, R.drawable.ic_account_circle_black_48dp),
        PROFILE(R.string.profile, R.drawable.ic_account_circle_black_48dp),
        HISTORY(R.string.calories_planner, R.drawable.ic_account_circle_black_48dp),
        MEAL(R.string.meal_planning, R.drawable.ic_account_circle_black_48dp);

        private final int stringResId;
        private final int imageResId;

        DrawerItem(int stringResId, int imageResId) {
            this.stringResId = stringResId;
            this.imageResId = imageResId;
        }

        public int getStringResId() {
            return stringResId;
        }

        public int getImageResId() {
            return imageResId;
        }
    }

    public enum DrawerItemPressed {
        ALARMS(R.string.alarms, R.drawable.ic_account_circle_black_48dp),
        PROFILE(R.string.profile, R.drawable.ic_account_circle_black_48dp),
        HISTORY(R.string.calories_planner, R.drawable.ic_account_circle_black_48dp),
        MEAL(R.string.meal_planning, R.drawable.ic_account_circle_black_48dp);

        private final int stringResId;
        private final int imageResId;

        DrawerItemPressed(int stringResId, int imageResId) {
            this.stringResId = stringResId;
            this.imageResId = imageResId;
        }

        public int getStringResId() {
            return stringResId;
        }

        public int getImageResId() {
            return imageResId;
        }
    }


    private DrawerItemListener drawerItemListener;
    private int selectedItemIndex;
    private LayoutInflater layoutInflater;


    public NavigationDrawerAdapter(@NonNull Context context, final int resource, @NonNull final DrawerItemListener drawerItemListener) {
        super(context, resource);
        this.drawerItemListener = drawerItemListener;
        this.selectedItemIndex = -1;
        this.layoutInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NavigationDrawerItemViewHolder viewHolder = null;

        if (convertView == null) {
            ItemNavigationDrawerBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_navigation_drawer, parent, false);
            convertView = binding.getRoot();
            viewHolder = new NavigationDrawerItemViewHolder(binding);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (NavigationDrawerItemViewHolder) convertView.getTag();
        }

        viewHolder.populateItem(position);

        return convertView;
    }

    @Override
    public int getCount() {
        return DrawerItem.values().length;
    }

    public void setSelectedItemIndex(final int position) {
        this.selectedItemIndex = position;
        notifyDataSetChanged();
    }

    private class NavigationDrawerItemViewHolder {

        private ItemNavigationDrawerBinding binding;

        public NavigationDrawerItemViewHolder(ItemNavigationDrawerBinding binding) {
            this.binding = binding;
        }

        public void populateItem(final int position) {

            final DrawerItem drawerItem = DrawerItem.values()[position];

            final DrawerItemPressed drawerItem2 = DrawerItemPressed.values()[position];

            binding.draweritemSeparator.setVisibility(View.VISIBLE);


            if (selectedItemIndex == position) {
                binding.draweritemImage.setImageResource(drawerItem2.getImageResId());
                binding.draweritemName.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            } else {
                binding.draweritemImage.setImageResource(drawerItem.getImageResId());
                binding.draweritemName.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            }


            binding.setDrawerItem(drawerItem);
            binding.setListener(drawerItemListener);

        }
    }

}
