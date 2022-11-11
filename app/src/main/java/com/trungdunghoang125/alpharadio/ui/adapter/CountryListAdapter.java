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
import com.trungdunghoang125.alpharadio.databinding.ItemCountryBinding;
import com.trungdunghoang125.alpharadio.utils.Constants;

import java.util.List;

/**
 * Created by trungdunghoang125 on 11/7/2022.
 */
public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.CountryViewHolder> {

    private List<Country> mCountryList;

    private final CountryItemClick itemClick;

    public CountryListAdapter(List<Country> mCountryList, CountryItemClick itemClick) {
        this.mCountryList = mCountryList;
        this.itemClick = itemClick;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemCountryBinding binding = ItemCountryBinding.inflate(inflater, parent, false);

        return new CountryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        Country country = mCountryList.get(position);
        holder.bind(country, holder);
    }

    @Override
    public int getItemCount() {
        return mCountryList.size();
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mCountryFlag;

        private TextView mCountryName;

        private TextView mStationCount;

        public CountryViewHolder(@NonNull ItemCountryBinding binding) {
            super(binding.getRoot());
            mCountryFlag = binding.imageCountryFlag;
            mCountryName = binding.textCountryName;
            mStationCount = binding.textStationCount;
        }

        void bind(Country country, CountryViewHolder holder) {
            Glide.with(holder.itemView.getContext())
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.ic_radio))
                    .load(Constants.BASE_COUNTRY_FLAG_URL + country.getName())
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
