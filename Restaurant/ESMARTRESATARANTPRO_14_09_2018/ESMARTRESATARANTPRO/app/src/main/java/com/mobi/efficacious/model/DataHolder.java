package com.mobi.efficacious.model;

import com.mobi.efficacious.restaurant.R;

import android.content.Context;
import android.widget.ArrayAdapter;

public class DataHolder {

    private int selected;
    private ArrayAdapter<CharSequence> adapter;

    public DataHolder(Context parent) 
    {
        adapter = ArrayAdapter.createFromResource(parent, R.array.choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public ArrayAdapter<CharSequence> getAdapter() 
    {
        return adapter;
    }

    public String getText() 
    {
        return (String) adapter.getItem(selected);
    }

    public int getSelected() 
    {
        return selected;
    }

    public void setSelected(int selected) 
    {
        this.selected = selected;
    }

}