package com.mobi.efficacious.esmartresatarant.adaptors;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobi.efficacious.esmartresatarant.R;
import com.mobi.efficacious.esmartresatarant.entity.Item;


public class TakeOrderAdapter extends ArrayAdapter<Item> {
	Context context;
	static int layoutResourceId;
	static ArrayList<Item> data = new ArrayList<Item>();
	
	public TakeOrderAdapter(Context context, int layoutResourceId,
		ArrayList<Item> data) {
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
			holder.txtTitle = (TextView) row.findViewById(R.id.item_text_takeorder);
			holder.imageItem = (ImageView) row.findViewById(R.id.item_image_takeorder);
			row.setTag(holder);
		}
		else 
		{
			holder = (RecordHolder) row.getTag();
		}

		Item item = data.get(position);
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