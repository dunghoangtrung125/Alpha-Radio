package com.trungdunghoang125.alpharadio.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.trungdunghoang125.alpharadio.R;
import com.trungdunghoang125.alpharadio.data.model.Country;
import com.trungdunghoang125.alpharadio.utils.Constants;

import java.util.List;

/**
 * Created by trungdunghoang125 on 11/7/2022.
 */
public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder> {

    private List<Country> mCountryList;

    private final CountryItemClick itemClick;

    public CountryListAdapter(List<Country> mCountryList, CountryItemClick itemClick) {
        this.mCountryList = mCountryList;
        this.itemClick = itemClick;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_country, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Country country = mCountryList.get(position);
        holder.bind(country, holder);
    }

    @Override
    public int getItemCount() {
        return mCountryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mCountryFlag;

        private TextView mCountryName;

        private TextView mStationCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCountryFlag = itemView.findViewById(R.id.image_country_flag);
            mCountryName = itemView.findViewById(R.id.text_country_name);
            mStationCount = itemView.findViewById(R.id.text_station_count);
        }

        void bind(Country country, ViewHolder holder) {
            Glide.with(holder.itemView.getContext())
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.radio_icon))
                    .load(Constants.BASE_COUNTRY_FLAG_URL + country.getName() + ".png")
                    .into(mCountryFlag);
            mCountryName.setText(country.getName());
            mStationCount.setText(country.getStationCount());
            setClickListener(country);
        }

        private void setClickListener(Country country) {
            itemView.setTag(country);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClick.onItemClick((Country) view.getTag());
        }
    }

    public interface CountryItemClick {
        void onItemClick(Country country);
    }
}
