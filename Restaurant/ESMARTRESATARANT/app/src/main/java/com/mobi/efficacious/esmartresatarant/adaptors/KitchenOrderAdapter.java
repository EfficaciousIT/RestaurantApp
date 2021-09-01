package com.mobi.efficacious.esmartresatarant.adaptors;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobi.efficacious.esmartresatarant.R;
import com.mobi.efficacious.esmartresatarant.entity.KichenOrder;
import com.mobi.efficacious.esmartresatarant.restaurentapp.KichenOrderDetailsActivity;


public class KitchenOrderAdapter extends BaseAdapter { //implements Filterable{
	Context context;
	static int layoutResourceId;
	ArrayList<KichenOrder> order = new ArrayList<KichenOrder>();
	String TableName;
	String OrderId;
	public ArrayList<KichenOrder> ord;
	public ArrayList<KichenOrder> orig;
	TextView txtTitle;
	TextView txtId;
	LinearLayout linear;
	public KitchenOrderAdapter(Context context, ArrayList<KichenOrder> orderdetails) {
		super();
		this.context = context;
		this.order = orderdetails;
	}
    
    public Filter getFilter() 
    {
        return new Filter() 
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<KichenOrder> results = new ArrayList<KichenOrder>();
                if (order == null)
                	order = ord;
                if (constraint != null) {
                    if (order != null && order.size() > 0) {
                        for (final KichenOrder g : order) {
                            if (g.getTable_Name().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
              FilterResults results) {
            	order = (ArrayList<KichenOrder>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
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
		View row = inflater.inflate(R.layout.kitchenorderadopter_row, parent, false);
          
   	 	txtId = (TextView) row.findViewById(R.id.OrderId_kitchenorder);
   	 	txtTitle = (TextView) row.findViewById(R.id.Name_kitchenorder);
   	 	linear = (LinearLayout) row.findViewById(R.id.linear_kitchenorder);
   	 	txtId.setText("Order Id:"+" "+ order.get(position).getOrder_Id());
   	 	txtTitle.setText("Table Name:"+" "+ order.get(position).getTable_Name());
   	 	
   	 	linear.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(context, KichenOrderDetailsActivity.class);
			intent.putExtra("OrderId", order.get(position).getOrder_Id()); 
			context.startActivity(intent);
		}
	});
   	 
       return row;
   }

}