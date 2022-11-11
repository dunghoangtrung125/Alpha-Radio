package com.trungdunghoang125.alpharadio.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.trungdunghoang125.alpharadio.R;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.databinding.ItemRadioStationBinding;

import java.util.List;

/**
 * Created by trungdunghoang125 on 11/10/2022.
 */
public class RadioStationAdapter extends RecyclerView.Adapter<RadioStationAdapter.StationViewHolder> {

    private List<RadioStation> mStationList;

    private final StationItemClick stationItemClick;

    public RadioStationAdapter(StationItemClick stationItemClick) {
        this.stationItemClick = stationItemClick;
    }

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRadioStationBinding binding = ItemRadioStationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StationViewHolder holder, int position) {
        RadioStation station = mStationList.get(position);
        holder.bind(station, holder);
    }

    @Override
    public int getItemCount() {
        return mStationList.size();
    }

    public void setStationList(List<RadioStation> mStationList) {
        this.mStationList = mStationList;
        notifyDataSetChanged();
    }

    public static class StationViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageFavicon;

        private TextView mTextStationName;

        private TextView mTextStationState;

        private TextView mTextStationCountry;

        private TextView mTextStationBitrate;

        public StationViewHolder(@NonNull ItemRadioStationBinding binding) {
            super(binding.getRoot());
            mImageFavicon = binding.imageFavicon;
            mTextStationName = binding.textStationName;
            mTextStationState = binding.textStationState;
            mTextStationCountry = binding.textStationCountry;
            mTextStationBitrate = binding.textStationBitrate;
        }

        public void bind(RadioStation station, StationViewHolder holder) {
            Glide.with(holder.itemView.getContext())
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.radio_icon))
                    .load(station.getFavicon())
                    .into(mImageFavicon);

            mTextStationName.setText(station.getName());
            mTextStationState.setText(station.getState());
            mTextStationCountry.setText(station.getCountry());
            mTextStationBitrate.setText(String.valueOf(station.getBitrate()) + "kbps");
        }
    }

    public interface StationItemClick {
        void onItemClick(RadioStation station);
    }
}
