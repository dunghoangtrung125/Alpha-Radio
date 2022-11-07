package com.trungdunghoang125.alpharadio.utils;

import com.trungdunghoang125.alpharadio.R;
import com.trungdunghoang125.alpharadio.model.RadioFilterTypeModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by trungdunghoang125 on 11/4/2022.
 */

public final class Constants {
    public static final List<RadioFilterTypeModel> sFilterList = Collections.unmodifiableList(
            new ArrayList<RadioFilterTypeModel>() {{
                add(new RadioFilterTypeModel("By country", R.drawable.filter_item_background));
                add(new RadioFilterTypeModel("By language", R.drawable.filter_item_background));
            }}
    );
}
