package com.nutrihealth.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nutrihealth.R;
import com.nutrihealth.model.HistoryDay;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Teo on 4/18/2018.
 */

public class HistoryPlannerAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<HistoryDay> historyDayList;
    private String idealCal;
    private HistoryListener listener;

    public interface HistoryListener{
        void onHistoryItemPressed(int position);
    }

    public HistoryPlannerAdapter(Context context, List<HistoryDay> historyDayList, String idealCal) {
        this.context = context;
        this.historyDayList = historyDayList;
        this.idealCal = idealCal;
    }

    public void setHistoryListener(HistoryListener listener){
        this.listener = listener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View historyView = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);

        return new HistoryViewHolder(historyView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final HistoryViewHolder viewHolder = (HistoryViewHolder) holder;



        viewHolder.bottomView.setVisibility(View.VISIBLE);
        if (position == 0) {
            viewHolder.historyContainer.setBackground(ContextCompat.getDrawable(context, R.drawable.white_background_with_top_rounded_corners));
        } else {
            if (position == historyDayList.size() - 1) {
                viewHolder.bottomView.setVisibility(View.GONE);
                viewHolder.historyContainer.setBackground(ContextCompat.getDrawable(context, R.drawable.white_background_with_bottom_rounded_corners));
            } else {
                viewHolder.historyContainer.setBackground(ContextCompat.getDrawable(context, R.drawable.white_background));
            }
        }

        if (historyDayList.size() == 1) {
            viewHolder.historyContainer.setBackground(ContextCompat.getDrawable(context, R.drawable.white_bk_small_rounded_corners));
        }

        if (getTodayInFOrmat().equals(historyDayList.get(position).getDate())) {
            viewHolder.dateTv.setText(context.getResources().getString(R.string.today));
            viewHolder.historyContainer.setOnClickListener(null);
        } else {
            viewHolder.historyContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        listener.onHistoryItemPressed(position);
                    }
                }
            });
            if (getYesterdayInFormat().equals(historyDayList.get(position).getDate())) {
                viewHolder.dateTv.setText(context.getResources().getString(R.string.yesterday));
            } else {
                DateFormat format = new SimpleDateFormat("dd-MMM-yy", new Locale("ro"));
                Date date = null;
                try {
                    date = format.parse(historyDayList.get(position).getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy", new Locale("ro"));
                String formattedDate = df.format(date);
                viewHolder.dateTv.setText(formattedDate);
            }

        }


        viewHolder.calTv.setText(historyDayList.get(position).getKcal() + " kcal");

        int dayCal = Integer.parseInt(historyDayList.get(position).getKcal());
        int idealCalInt = Integer.parseInt(idealCal);

        if (dayCal > idealCalInt) {
            viewHolder.warningIv.setVisibility(View.VISIBLE);
            viewHolder.calTv.setTextColor(ContextCompat.getColor(context, R.color.red_dark));
        } else {
            viewHolder.warningIv.setVisibility(View.GONE);
            viewHolder.calTv.setTextColor(ContextCompat.getColor(context, R.color.light_blue));
        }

    }

    @Override
    public int getItemCount() {
        return historyDayList != null ? historyDayList.size() : null;
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView dateTv;
        ImageView warningIv;
        TextView calTv;
        View bottomView;
        RelativeLayout historyContainer;

        public HistoryViewHolder(View itemView) {
            super(itemView);

            dateTv = itemView.findViewById(R.id.date_tv);
            warningIv = itemView.findViewById(R.id.warning_iv);
            calTv = itemView.findViewById(R.id.cal_tv);
            bottomView = itemView.findViewById(R.id.bottom_view);
            historyContainer = itemView.findViewById(R.id.parent_container);

        }
    }

    private String getTodayInFOrmat() {
        Format formatter = new SimpleDateFormat("dd-MMM-yy");
        String today = formatter.format(new Date());
        return today;
    }

    private String getYesterdayInFormat() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }
}
