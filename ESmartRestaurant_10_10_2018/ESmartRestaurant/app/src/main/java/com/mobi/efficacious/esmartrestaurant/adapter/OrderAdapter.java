package com.mobi.efficacious.esmartrestaurant.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.activity.MainActivity;
import com.mobi.efficacious.esmartrestaurant.entity.OrderDetail;
import com.mobi.efficacious.esmartrestaurant.fragment.EditOrderDetailsActivity;

import java.util.ArrayList;


public class OrderAdapter extends ArrayAdapter<OrderDetail>{
	Context context;
	static int layoutResourceId;
	ArrayList<OrderDetail> order = new ArrayList<OrderDetail>();
	TextView txtTitle;
	TextView Qty;
	TextView price;
	RelativeLayout top;
	String pagenamee;
	public OrderAdapter(Context context, ArrayList<OrderDetail> ord,String pagename) {
		super(context, layoutResourceId, ord);
		this.context = context;
		this.order = ord;
		this.pagenamee=pagename;
	}
    
    public View getView(final int position, View convertView, ViewGroup parent) {
   		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   		View row = inflater.inflate(R.layout.order_row, parent, false);
   	 	txtTitle = (TextView) row.findViewById(R.id.item_Name_order);
   		Qty = (TextView) row.findViewById(R.id.qty_order);
   		price = (TextView) row.findViewById(R.id.price_order);
   		top = (RelativeLayout) row.findViewById(R.id.top_Order);
   		top.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Completed
				if(!order.get(position).getKitchenStaus().contentEquals("Completed"))
				{
					EditOrderDetailsActivity editOrderDetailsActivity = new EditOrderDetailsActivity();
					Bundle args = new Bundle();
					args.putString("Id", order.get(position).getId());
					args.putString("MenuName", order.get(position).getMenu_Name());
					args.putString("Qty", order.get(position).getQty());
					args.putString("Price", order.get(position).getPrice());
					args.putString("OrderId", order.get(position).getOrder_Id());
					args.putString("TableName", order.get(position).getTable_Name());
					args.putString("pagename",pagenamee);
					editOrderDetailsActivity.setArguments(args);
					MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, editOrderDetailsActivity).commitAllowingStateLoss();

				}else
				{
					Toast.makeText(context,"You Cannot  Edited Or Delete this Order",Toast.LENGTH_SHORT);
				}

			}
		});
   		
   	 	txtTitle.setText(order.get(position).getMenu_Name());
   	 	Qty.setText(order.get(position).getQty());
   	 	price.setText(order.get(position).getPrice());
   	 	return row;
   }
    
}