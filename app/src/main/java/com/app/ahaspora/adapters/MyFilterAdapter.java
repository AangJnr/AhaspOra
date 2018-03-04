package com.app.ahaspora.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.app.ahaspora.R;
import com.app.ahaspora.models.Tag;
import com.yalantis.filter.adapter.FilterAdapter;
import com.yalantis.filter.widget.FilterItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by aangjnr on 08/06/2017.
 */

public class MyFilterAdapter extends com.yalantis.filter.adapter.FilterAdapter<Tag> {

    Context context;

    public MyFilterAdapter(Context context, @NotNull List<Tag> items) {
        super(items);
        this.context = context;
    }

    @NotNull
    @Override
    public FilterItem createView(int position, Tag item) {
        FilterItem filterItem = new FilterItem(context);

        filterItem.setStrokeColor(item.getColor());
        filterItem.setTextColor(ContextCompat.getColor(context, android.R.color.secondary_text_light));
        filterItem.setColor(ContextCompat.getColor(context, android.R.color.white));
        filterItem.setCheckedColor(item.getColor());
        filterItem.setCheckedTextColor(ContextCompat.getColor(context, R.color.white));
        filterItem.setText(item.getText());
        filterItem.deselect();

        return filterItem;
    }









}