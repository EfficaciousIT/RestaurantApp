package com.mobi.efficacious.esmartresatarant.adaptors;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mobi.efficacious.esmartresatarant.R;
import com.mobi.efficacious.esmartresatarant.entity.Menus;


public class ViewMenuAdapter extends BaseAdapter implements Filterable {
	Context context;
	static int layoutResourceId;
	ArrayList<Menus> menus = new ArrayList<Menus>();
	public ArrayList<Menus> categories;
	public ArrayList<Menus> orig;

	String qty;
	String price;
	String menu_name;
	
	public ViewMenuAdapter(Context context, ArrayList<Menus> Menus){
		super();
		this.context = context;
		this.menus = Menus;
	}
	
	 static class ImageHolder
	 {
    	TextView txtTitle;
    	TextView price;
	 }
	 
	 public Filter getFilter() 
	    {
	        return new Filter() 
	        {
	            @Override
	            protected FilterResults performFiltering(CharSequence constraint) {
	                final FilterResults oReturn = new FilterResults();
	                final ArrayList<Menus> results = new ArrayList<Menus>();
	                if (orig == null)
	                    orig = menus;
	                if (constraint != null) {
	                    if (orig != null && orig.size() > 0) {
	                        for (final Menus g : orig) {
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
	            	menus = (ArrayList<Menus>) results.values;
	                notifyDataSetChanged();
	            }
	        };
	    }

	    public void notifyDataSetChanged() {
	        super.notifyDataSetChanged();
	    }

	    @Override
	    public int getCount() {
	        return menus.size();
	    }

	    @Override
	    public Object getItem(int position) {
	        return menus.get(position);
	    }

	    @Override
	    public long getItemId(int position) {
	        return position;
	    }

	  public View getView(final int position, View convertView, ViewGroup parent) {
	   	 View row = convertView;
	   	 if(row == null)
	        {
	   		 	final ImageHolder holder = new ImageHolder();
	   		 	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	   		    row = inflater.inflate(R.layout.viewmenuadopter_row, parent, false);
	   		    holder.txtTitle = (TextView) row.findViewById(R.id.item_Name_viewmenu);
		   		holder.price = (TextView) row.findViewById(R.id.price_viewmenu);
		   
	            row.setTag(holder);
	        }
	        else
	        {
	        	row = convertView;
	        }
	  
	   	 	ImageHolder holder = (ImageHolder) row.getTag();
		   	holder.txtTitle.setText(menus.get(position).getMenu_Name());
		   	holder.price.setText(menus.get(position).getPrice());
		   	price = menus.get(position).getPrice();
		   	menu_name = menus.get(position).getMenu_Name();
		   
		   return row;
	  }
}