package com.mobi.efficacious.esmartrestaurant.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.entity.Home;

import java.util.ArrayList;

public class HomeAdapter extends ArrayAdapter<Home> {
	Context context;
	static int layoutResourceId;
	static ArrayList<Home> data = new ArrayList<Home>();
	
	public HomeAdapter(Context context, int layoutResourceId,
                       ArrayList<Home> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		RecordHolder holder = null;

		if (row == null) 
		{
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new RecordHolder();
			holder.txtTitle = (TextView) row.findViewById(R.id.itemtext_home);
			holder.imageItem = (ImageView) row.findViewById(R.id.itemimage_home);
			row.setTag(holder);
		}
		else 
		{
			holder = (RecordHolder) row.getTag();
		}

		Home item = data.get(position);
		holder.txtTitle.setText(item.getTitle());
		holder.imageItem.setImageBitmap(item.getImage());
		return row;

	}

	static class RecordHolder 
	{
		TextView txtTitle;
		ImageView imageItem;
	}
}