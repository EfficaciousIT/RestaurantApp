package com.mobi.efficacious.esmartresatarant.adaptors;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobi.efficacious.esmartresatarant.R;
import com.mobi.efficacious.esmartresatarant.entity.ExistingTable;
import com.mobi.efficacious.esmartresatarant.restaurentapp.ExistingOrderHomeActivity;


public class ExistingTableAdpter extends ArrayAdapter<ExistingTable> {
	Context context;
	static int layoutResourceId;
	ArrayList<ExistingTable> table = new ArrayList<ExistingTable>();
	TextView txtTitle;
	ImageView imageItem;
	LinearLayout top;
	String TableName;
	String OrderId;
	
	public ExistingTableAdpter(Context context, ArrayList<ExistingTable> tables) {
		super(context, layoutResourceId, tables);
		this.context = context;
		this.table = tables;
	}

	public View getView(final int position, View convertView, ViewGroup parent) 
	{
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View rowView = inflater.inflate(R.layout.existingtableadapter_row, parent, false);
    	top = (LinearLayout) rowView.findViewById(R.id.top_existingtable);
    	txtTitle = (TextView) rowView.findViewById(R.id.itemtext_existingtable);
		imageItem = (ImageView) rowView.findViewById(R.id.itemimage_existingtable);
		top.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TableName = table.get(position).getTable_Name();
				OrderId = table.get(position).getOrder_Id();
				Intent intent=new Intent(getContext(), ExistingOrderHomeActivity.class);
				intent.putExtra("TableName",TableName);
				intent.putExtra("OrderId",OrderId);
				getContext().startActivity(intent);
			}
		});
		
        txtTitle.setText(table.get(position).getTable_Name());
        TableName = table.get(position).getTable_Name();
		imageItem.setImageResource(R.drawable.tablewith);
        return rowView;
    }
}