package com.example.nadro.astroweather.Fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nadro.astroweather.Fragment.ForecastFragment.OnListFragmentInteractionListener;

import com.example.nadro.astroweather.MainActivity;
import com.example.nadro.astroweather.Model.Weather;
import com.example.nadro.astroweather.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a dummy and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyForecastRecyclerViewAdapter extends RecyclerView.Adapter<MyForecastRecyclerViewAdapter.ViewHolder> {

    private final List<Weather.NextDays> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyForecastRecyclerViewAdapter(List<Weather.NextDays> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_forecastitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mDayView.setText(mValues.get(position).day);
        holder.mDateView.setText(mValues.get(position).date);
        holder.mTempView.setText((mValues.get(position).low + mValues.get(position).low)/2);
        holder.mDescView.setText(mValues.get(position).description);
//        holder.mImageView.setImageBitmap();


//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDayView;
        public final TextView mDateView;
        public final TextView mTempView;
        public final TextView mDescView;
//        public final ImageView mImageView;
//        public Weather.NextDays mItem;

//        public int code;
//        public String date;
//        public String day;
//        public Integer low;
//        public Integer high;
//        public String description;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDayView = (TextView) view.findViewById(R.id.forecast_day);
            mDateView = (TextView) view.findViewById(R.id.forecast_date);
            mTempView = (TextView) view.findViewById(R.id.foreccast_avg_temp_val);
            mDescView = (TextView) view.findViewById(R.id.forecast_description);
//            mImageView = (ImageView) view.findViewById(R.id.forecast_image);
        }

//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
    }
}
