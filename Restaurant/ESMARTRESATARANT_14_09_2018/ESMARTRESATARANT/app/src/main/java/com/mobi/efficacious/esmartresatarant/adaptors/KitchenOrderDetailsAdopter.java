package com.mobi.efficacious.esmartresatarant.adaptors;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobi.efficacious.esmartresatarant.R;
import com.mobi.efficacious.esmartresatarant.entity.KichenOrderDetails;


public class KitchenOrderDetailsAdopter extends BaseAdapter implements Filterable{
	Context context;
	static int layoutResourceId;
	ArrayList<KichenOrderDetails> order = new ArrayList<KichenOrderDetails>();
	String TableName;
	String OrderId;
	public ArrayList<KichenOrderDetails> ord;
	public ArrayList<KichenOrderDetails> orig;
	TextView txtQty;
	TextView txtName;
	LinearLayout linear;
	public KitchenOrderDetailsAdopter(Context context, ArrayList<KichenOrderDetails> orderdetails) {
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
                final ArrayList<KichenOrderDetails> results = new ArrayList<KichenOrderDetails>();
                if (order == null)
                	order = ord;
                if (constraint != null) {
                    if (order != null && order.size() > 0) {
                        for (final KichenOrderDetails g : order) {
                            if (g.getMenu_Name().toLowerCase()
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
            	order = (ArrayList<KichenOrderDetails>) results.values;
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
		View row = inflater.inflate(R.layout.kitchenorderdetailsadopter_row, parent, false);
          
   	 	txtName = (TextView) row.findViewById(R.id.name_kitchenorderdetails);
   	 	txtQty = (TextView) row.findViewById(R.id.qty_kitchenorderdetails);
   	 	linear = (LinearLayout) row.findViewById(R.id.linear_kitchenorderdetails);
   	 	txtName.setText(order.get(position).getMenu_Name());
   	 	txtQty.setText(order.get(position).getQty());
   	 
       return row;
   }

}