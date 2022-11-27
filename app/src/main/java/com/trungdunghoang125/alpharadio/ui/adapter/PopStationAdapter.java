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
import com.trungdunghoang125.alpharadio.databinding.ItemPopStationBinding;

import java.util.List;

/**
 * Created by trungdunghoang125 on 11/26/2022.
 */
public class PopStationAdapter extends RecyclerView.Adapter<PopStationAdapter.StationViewHolder> {

    private List<RadioStation> mStationList;

    private final ItemClick itemClick;

    public PopStationAdapter(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPopStationBinding binding = ItemPopStationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StationViewHolder holder, int position) {
        RadioStation station = mStationList.get(position);
        holder.bind(station, holder);
        holder.itemView.setOnClickListener(view -> {
            itemClick.onItemClick(holder.getAbsoluteAdapterPosition());
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

        private ImageView mPopStationFavicon;

        private TextView mPopStationTitle;

        public StationViewHolder(@NonNull ItemPopStationBinding binding) {
            super(binding.getRoot());
            mPopStationFavicon = binding.popStationFavicon;
            mPopStationTitle = binding.textPopStationName;
        }

        public void bind(RadioStation station, PopStationAdapter.StationViewHolder holder) {
            Glide.with(holder.itemView.getContext())
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.ic_radio))
                    .load(station.getFavicon())
                    .into(mPopStationFavicon);

            if (station.getName().length() > 20) {
                mPopStationTitle.setText(station.getName().substring(0, 20) + "..");
            } else {
                mPopStationTitle.setText(station.getName());
            }
        }
    }

    public interface ItemClick {
        void onItemClick(int position);
    }
}