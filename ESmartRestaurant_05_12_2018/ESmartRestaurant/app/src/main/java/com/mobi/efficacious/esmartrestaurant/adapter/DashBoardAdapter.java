package com.mobi.efficacious.esmartrestaurant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.entity.Tables;

import java.util.ArrayList;


public class DashBoardAdapter extends ArrayAdapter<Tables> {
	Context context;
	static int layoutResourceId;
	ArrayList<Tables> table = new ArrayList<Tables>();
	TextView txtTitle;
	ImageView imageItem;
	String TableName;

	public DashBoardAdapter(Context context, ArrayList<Tables> tables) {
		super(context, layoutResourceId, tables);
		this.context = context;
		this.table = tables;
	}

	public View getView(int position, View convertView, ViewGroup parent) 
	{
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View rowView = inflater.inflate(R.layout.new_dashboardrow_layout, parent, false);
  
    	txtTitle = (TextView) rowView.findViewById(R.id.item_text);
		imageItem = (ImageView) rowView.findViewById(R.id.item_image);
        txtTitle.setText(table.get(position).getTable_Name());
        TableName = table.get(position).getTable_Name();
		imageItem.setImageResource(R.mipmap.table);
        return rowView;
    }
}