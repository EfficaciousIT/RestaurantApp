package com.mobi.efficacious.esmartrestaurant.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.activity.MainActivity;
import com.mobi.efficacious.esmartrestaurant.entity.Categories;
import com.mobi.efficacious.esmartrestaurant.fragment.ViewMenuActivity;

import java.util.ArrayList;


public class ViewCategoryAdapter extends BaseAdapter implements Filterable{
	Context context;
	static int layoutResourceId;
	ArrayList<Categories> cate = new ArrayList<Categories>();
	String TableName;
	String OrderId;
	public ArrayList<Categories> categories;
	public ArrayList<Categories> orig;
	
	public ViewCategoryAdapter(Context context, ArrayList<Categories> categories) {
		super();
		this.context = context;
		this.cate = categories;
//		this.TableName = tableName;
//		this.OrderId = orderId;
	}

    static class ImageHolder
    {
    	TextView txtTitle;
    	ImageView imageItem;
        RelativeLayout linear;
     }
    
    public Filter getFilter() 
    {
        return new Filter() 
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Categories> results = new ArrayList<Categories>();
                if (orig == null)
                    orig = cate;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Categories g : orig) {
                            if (g.getCat_Name().toLowerCase()
                                    .contains(constraint.toString().toLowerCase()))
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
            	cate = (ArrayList<Categories>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return cate.size();
    }

    @Override
    public Object getItem(int position) {
        return cate.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
   	 View row = convertView;
     ImageHolder holder = null;
   	 if(row == null)
        {
   		 	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   		    row = inflater.inflate(R.layout.viewcategoryadapter_row, parent, false);
            holder = new ImageHolder();
            row.setTag(holder);
        }
        else
        {
            holder = (ImageHolder)row.getTag();
        }
  
   	 	holder.txtTitle = (TextView) row.findViewById(R.id.item_text_category);
   	 	holder.imageItem = (ImageView) row.findViewById(R.id.item_image_category);
   	 	holder.linear = (RelativeLayout) row.findViewById(R.id.linear_category);
   	 	holder.txtTitle.setText(cate.get(position).getCat_Name());
        if (cate.get(position).getCat_Name().equalsIgnoreCase("Pizzas"))
        {
            holder.imageItem.setImageResource(R.drawable.pizza_sm);
        }
        else if(cate.get(position).getCat_Name().equalsIgnoreCase("South Indian"))
        {
            holder.imageItem.setImageResource(R.drawable.si_sm);
        }
        else if(cate.get(position).getCat_Name().equalsIgnoreCase("Maharashtrian"))
        {
            holder.imageItem.setImageResource(R.drawable.maharashtrian_sm);
        }
        else if(cate.get(position).getCat_Name().equalsIgnoreCase("Sandwich"))
        {
            holder.imageItem.setImageResource(R.drawable.sandwich_sm);
        }
        else if(cate.get(position).getCat_Name().equalsIgnoreCase("Beverages"))
        {
            holder.imageItem.setImageResource(R.drawable.juice_beverage_sm);
        }
        else if(cate.get(position).getCat_Name().equalsIgnoreCase("Refreshers"))
        {
            holder.imageItem.setImageResource(R.drawable.juice_beverage_sm);
        }
        else if(cate.get(position).getCat_Name().equalsIgnoreCase("Faloodas"))
        {
            holder.imageItem.setImageResource(R.drawable.falooda1_sm);
        }
        else if(cate.get(position).getCat_Name().equalsIgnoreCase("Fresh-N-Juicy"))
        {
            holder.imageItem.setImageResource(R.drawable.juice_beverage_sm);
        }
        else if(cate.get(position).getCat_Name().equalsIgnoreCase("Shaken Up"))
        {
            holder.imageItem.setImageResource(R.drawable.juice_beverage_sm);
        }
        else if(cate.get(position).getCat_Name().equalsIgnoreCase("Salad/Raita/Papad"))
        {
            holder.imageItem.setImageResource(R.drawable.salad_sm);
        }
        else if(cate.get(position).getCat_Name().equalsIgnoreCase("Soups"))
        {
            holder.imageItem.setImageResource(R.drawable.soup_sm);
        }
        else if(cate.get(position).getCat_Name().equalsIgnoreCase("starter"))
        {
            holder.imageItem.setImageResource(R.drawable.starters_sm);
        }
        else if(cate.get(position).getCat_Name().equalsIgnoreCase("Main Course"))
        {
            holder.imageItem.setImageResource(R.drawable.maincourse1_sm);
        }
        else if(cate.get(position).getCat_Name().equalsIgnoreCase("Chinese"))
        {
            holder.imageItem.setImageResource(R.drawable.chinese_sm);
        }
        else if(cate.get(position).getCat_Name().equalsIgnoreCase("Rice"))
        {
            holder.imageItem.setImageResource(R.drawable.rice_sm);
        }
        else if(cate.get(position).getCat_Name().equalsIgnoreCase("Continental"))
        {
            holder.imageItem.setImageResource(R.drawable.continental_sm);
        }
        else if(cate.get(position).getCat_Name().equalsIgnoreCase("Ice Creams"))
        {
            holder.imageItem.setImageResource(R.drawable.icecreme_sm);
        }
        else if(cate.get(position).getCat_Name().equalsIgnoreCase("Desserts"))
        {
            holder.imageItem.setImageResource(R.drawable.dessert_sm);
        }
        else
        {
            holder.imageItem.setImageResource(R.drawable.soup_sm);
        }
   	 
   	 	holder.linear.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			ViewMenuActivity viewMenuActivity = new ViewMenuActivity();
			Bundle args = new Bundle();
			args.putString("Cat_Id", cate.get(position).getCat_Id());
			viewMenuActivity.setArguments(args);
			MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, viewMenuActivity).commitAllowingStateLoss();

		}
	});
   	 
       return row;
   }

}