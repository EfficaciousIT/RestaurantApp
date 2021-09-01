package com.mobi.efficacious.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobi.efficacious.adapter.CategoryAdopter.ImageHolder;
import com.mobi.efficacious.model.Categories;
import com.mobi.efficacious.model.CustomerWiseOrderDetail;
import com.mobi.efficacious.restaurant.MenuActivity;
import com.mobi.efficacious.restaurant.R;

public class CustomerWiseOrderDetailAdopter extends BaseAdapter{
		Context context;
		static int layoutResourceId;
		ArrayList<CustomerWiseOrderDetail> order = new ArrayList<CustomerWiseOrderDetail>();
		String OrderId;
		TextView txtTitle;
    	TextView txtQty;
    	TextView txtRate;
    	
		public CustomerWiseOrderDetailAdopter(Context context, ArrayList<CustomerWiseOrderDetail> orderdetails) {
			super();
			this.context = context;
			this.order = orderdetails;
		}

	    @Override
	    public int getCount() {
	        return order.size();
	    }

	    @Override
	    public Object getItem(int position) {
	        return order.get(position);
	    }

	    @Override
	    public long getItemId(int position) {
	        return position;
	    }

	    public View getView(final int position, View convertView, ViewGroup parent) {
	   	 
   		 	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   		 	View row = inflater.inflate(R.layout.customerwiseorderdetails_row, parent, false);
	         
	   	 	txtTitle = (TextView) row.findViewById(R.id.item_customerwiseorderdetails);
	   	 	txtQty = (TextView) row.findViewById(R.id.Qty_customerwiseorderdetails);
	   	 	txtRate = (TextView) row.findViewById(R.id.price_customerwiseorderdetails);
	   	 	
	   	 	txtTitle.setText(order.get(position).getMenu_Name());
	   	 	txtQty.setText(order.get(position).getQty());
	   	 	txtRate.setText(order.get(position).getPrice());
	       return row;
	   }
}
