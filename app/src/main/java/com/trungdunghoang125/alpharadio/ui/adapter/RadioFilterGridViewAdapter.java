package com.trungdunghoang125.alpharadio.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.trungdunghoang125.alpharadio.R;
import com.trungdunghoang125.alpharadio.data.model.RadioFilterTypeModel;

import java.util.List;

/**
 * Created by trungdunghoang125 on 11/3/2022.
 */
public class RadioFilterGridViewAdapter extends ArrayAdapter<RadioFilterTypeModel> {

    public RadioFilterGridViewAdapter(@NonNull Context context, List<RadioFilterTypeModel> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_radio_filter, parent, false);

            RadioFilterTypeModel filterType = getItem(position);
            TextView filterTitle = listItemView.findViewById(R.id.text_filter_title);
            ImageView filterBackground = listItemView.findViewById(R.id.image_filter);

            filterTitle.setText(filterType.getRadioFilterTitle());

            Drawable d = listItemView.getResources().getDrawable(filterType.getRadioFilterBackground());
            Log.d("tranle1811", "getView: " + filterType.getRadioFilterTitle());
            filterBackground.setBackground(d);
        }

        return listItemView;
    }
}
