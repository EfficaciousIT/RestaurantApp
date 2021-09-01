package com.mobi.efficacious.esmartrestaurant.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.activity.MainActivity;
import com.mobi.efficacious.esmartrestaurant.entity.ExistingTable;
import com.mobi.efficacious.esmartrestaurant.fragment.Bill_fragment;
import com.mobi.efficacious.esmartrestaurant.fragment.ExistingOrderHomeActivity;
import com.mobi.efficacious.esmartrestaurant.fragment.OrderDetailsActivity;
import com.mobi.efficacious.esmartrestaurant.tab.ExistionOrder_Tab;

import java.util.ArrayList;


public class ExistingTableAdpter extends ArrayAdapter<ExistingTable> {
	Context context;
	static int layoutResourceId;
	ArrayList<ExistingTable> table = new ArrayList<ExistingTable>();
	TextView txtTitle;
	ImageView imageItem;
	LinearLayout top;
	String TableName;
	String OrderId;
	String pagenamee;
	public ExistingTableAdpter(Context context, ArrayList<ExistingTable> tables,String pagename) {
		super(context, layoutResourceId, tables);
		this.context = context;
		this.table = tables;
		this.pagenamee=pagename;
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
				if(pagenamee.contentEquals("ExistingOrder"))
                {
                    ExistionOrder_Tab existingOrderHomeActivity = new ExistionOrder_Tab();
                    Bundle args = new Bundle();
                    args.putString("TableName",TableName);
                    args.putString("OrderId",OrderId);
                    existingOrderHomeActivity.setArguments(args);
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, existingOrderHomeActivity).commitAllowingStateLoss();

                }else if(pagenamee.contentEquals("BillTable"))
				{
					Bill_fragment bill_fragment = new Bill_fragment();
					Bundle args = new Bundle();
					args.putString("TableName",TableName);
					args.putString("OrderId",OrderId);
					bill_fragment.setArguments(args);
					MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, bill_fragment).commitAllowingStateLoss();

				}
                else
                {
                    OrderDetailsActivity orderDetailsActivity = new OrderDetailsActivity();
                    Bundle args = new Bundle();
                    args.putString("TableName",TableName);
                    args.putString("OrderId",OrderId);
                    orderDetailsActivity.setArguments(args);
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, orderDetailsActivity).commitAllowingStateLoss();

                }

			}
		});
		
        txtTitle.setText(table.get(position).getTable_Name());
        TableName = table.get(position).getTable_Name();
		imageItem.setImageResource(R.mipmap.table);
        return rowView;
    }
}