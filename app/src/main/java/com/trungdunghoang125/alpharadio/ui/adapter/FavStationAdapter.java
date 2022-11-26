package com.trungdunghoang125.alpharadio.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.trungdunghoang125.alpharadio.R;
import com.trungdunghoang125.alpharadio.data.model.RadioStation;
import com.trungdunghoang125.alpharadio.databinding.ItemFavStationBinding;

import java.util.List;

/**
 * Created by trungdunghoang125 on 11/26/2022.
 */
public class FavStationAdapter extends RecyclerView.Adapter<FavStationAdapter.StationViewHolder> {
    private List<RadioStation> mStationList;

    private final StationItemClick stationItemClick;

    public FavStationAdapter(StationItemClick stationItemClick) {
        this.stationItemClick = stationItemClick;
    }

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFavStationBinding binding = ItemFavStationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StationViewHolder holder, int position) {
        RadioStation station = mStationList.get(position);
        holder.bind(station, holder);
        holder.itemView.setOnClickListener(view -> {
            stationItemClick.onItemClick(holder.getAbsoluteAdapterPosition());
        });

        holder.mBtnAddFav.setOnClickListener(view -> {
            stationItemClick.onCheckBoxImportance(station);
        });
    }

    @Override
    public int getItemCount() {
        if (mStationList != null) {
            return mStationList.size();
        }
        return 0;
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

        private ImageButton mBtnAddFav;

        public StationViewHolder(@NonNull ItemFavStationBinding binding) {
            super(binding.getRoot());
            mImageFavicon = binding.favImageFavicon;
            mTextStationName = binding.textFavStationName;
            mTextStationState = binding.textFavStationState;
            mTextStationCountry = binding.textFavStationCountry;
            mTextStationBitrate = binding.textFavStationBitrate;
            mBtnAddFav = binding.btnRemoveFav;
        }

        public void bind(RadioStation station, StationViewHolder holder) {
            Glide.with(holder.itemView.getContext())
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.ic_radio))
                    .load(station.getFavicon())
                    .into(mImageFavicon);

            if (station.getName().length() >= 20) {
                mTextStationName.setText(station.getName().substring(0, 20) + "...");
            } else {
                mTextStationName.setText(station.getName());
            }

            if (station.getState().length() >= 10) {
                mTextStationState.setText(station.getState().substring(0, 10) + "...");
            } else {
                mTextStationState.setText(station.getState());
            }

            if (station.getCountry().length() >= 10) {
                mTextStationCountry.setText(station.getCountry().substring(0, 10) + "...");
            } else {
                mTextStationCountry.setText(station.getCountry());
            }

            mTextStationBitrate.setText(String.valueOf(station.getBitrate()) + " kbps");
        }
    }

    public interface StationItemClick {
        void onItemClick(int position);

        void onCheckBoxImportance(RadioStation station);
    }
}