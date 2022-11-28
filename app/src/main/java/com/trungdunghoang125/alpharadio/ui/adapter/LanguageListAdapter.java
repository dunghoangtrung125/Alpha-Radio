package com.trungdunghoang125.alpharadio.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trungdunghoang125.alpharadio.data.domain.Language;
import com.trungdunghoang125.alpharadio.databinding.ItemLanguageBinding;

import java.util.List;

/**
 * Created by trungdunghoang125 on 11/28/2022.
 */
public class LanguageListAdapter extends RecyclerView.Adapter<LanguageListAdapter.LanguageViewHolder> {

    private List<Language> mLanguageList;

    private ItemClick itemClick;

    public LanguageListAdapter(List<Language> mLanguageList, ItemClick itemClick) {
        this.mLanguageList = mLanguageList;
        this.itemClick = itemClick;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemLanguageBinding binding = ItemLanguageBinding.inflate(inflater, parent, false);

        return new LanguageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        Language language = mLanguageList.get(position);
        holder.bind(language, holder);
    }

    @Override
    public int getItemCount() {
        return mLanguageList.size();
    }

    public class LanguageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mLanguageName;

        private TextView mStationCount;

        public LanguageViewHolder(@NonNull ItemLanguageBinding binding) {
            super(binding.getRoot());
            mLanguageName = binding.tvLanguageName;
            mStationCount = binding.tvLanguageStationCount;
        }

        void bind(Language language, LanguageListAdapter.LanguageViewHolder holder) {

            if (language.getName().length() >= 10) {
                mLanguageName.setText(language.getName().substring(0, 10) + "..");
            } else {
                mLanguageName.setText(language.getName());
            }
            mStationCount.setText(language.getStationCount());
            setClickListener(language);
        }

        private void setClickListener(Language language) {
            itemView.setTag(language);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClick.onItemClick((Language) view.getTag());
        }
    }

    public interface ItemClick {
        void onItemClick(Language language);
    }
}
