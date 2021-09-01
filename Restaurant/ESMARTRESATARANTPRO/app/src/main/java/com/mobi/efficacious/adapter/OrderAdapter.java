package com.mobi.efficacious.adapter;

import java.util.ArrayList;
import com.mobi.efficacious.model.OrderDetail;
import com.mobi.efficacious.restaurant.EditOrderDetailsActivity;
import com.mobi.efficacious.restaurant.R;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrderAdapter extends ArrayAdapter<OrderDetail>{
	Context context;
	static int layoutResourceId;
	ArrayList<OrderDetail> order = new ArrayList<OrderDetail>();
	TextView txtTitle;
	TextView Qty;
	TextView price;
	LinearLayout top;
	
	public OrderAdapter(Context context, ArrayList<OrderDetail> ord) {
		super(context, layoutResourceId, ord);
		this.context = context;
		this.order = ord;
	}
    
    public View getView(final int position, View convertView, ViewGroup parent) {
   		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   		View row = inflater.inflate(R.layout.order_row, parent, false);
   	 	txtTitle = (TextView) row.findViewById(R.id.item_Name_order);
   		Qty = (TextView) row.findViewById(R.id.qty_order);
   		price = (TextView) row.findViewById(R.id.price_order);
   		top = (LinearLayout) row.findViewById(R.id.top_Order);
   		top.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getContext(), EditOrderDetailsActivity.class);
				intent.putExtra("Id", order.get(position).getId()); 
				intent.putExtra("MenuName", order.get(position).getMenu_Name()); 
				intent.putExtra("Qty", order.get(position).getQty()); 
				intent.putExtra("Price", order.get(position).getPrice()); 
				intent.putExtra("OrderId", order.get(position).getOrder_Id()); 
				getContext().startActivity(intent);
			}
		});
   		
   	 	txtTitle.setText(order.get(position).getMenu_Name());
   	 	Qty.setText(order.get(position).getQty());
   	 	price.setText(order.get(position).getPrice());
   	 	return row;
   }
}