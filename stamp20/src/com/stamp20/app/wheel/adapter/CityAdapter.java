package com.stamp20.app.wheel.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.stamp20.app.R;

/**
 * Adapter for countries
 */
public class CityAdapter extends AbstractWheelTextAdapter {

    String cities[];

    /**
     * Constructor
     */
    public CityAdapter(Context context, String[] cities) {
        super(context, R.layout.city_layout, NO_RESOURCE);
        // FontManager.changeFonts(this, );
        setItemTextResource(R.id.city_name);
        this.cities = cities;
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        View view = super.getItem(index, cachedView, parent);
        return view;
    }

    @Override
    public int getItemsCount() {
        return cities.length;
    }

    @Override
    protected CharSequence getItemText(int index) {
        return cities[index];
    }
}
