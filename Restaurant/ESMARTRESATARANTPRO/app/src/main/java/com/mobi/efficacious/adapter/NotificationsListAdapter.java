package com.mobi.efficacious.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mobi.efficacious.database.DatabaseHandler;
import com.mobi.efficacious.database.NotificationList;
import com.mobi.efficacious.restaurant.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
public class NotificationsListAdapter extends ArrayAdapter<NotificationList>{
	 	Context mcontext;
	    int layoutResourceId;  
	    String name;
	    String id;
		Boolean isInternetPresent = false;
	    DatabaseHandler db;
	    List<String> objectIds; 
	  
	    ArrayList<NotificationList> data=new ArrayList<NotificationList>();
	    
	    public NotificationsListAdapter(Context context, int layoutResourceId, ArrayList<NotificationList> data) {
	        super(context, layoutResourceId, data);
	        this.layoutResourceId = layoutResourceId;
	        this.mcontext = context;
	        this.data = data;
	    }
	    
	    public static class ImageHolder
	    {
	        TextView txtname;
	        TextView txtdate;
	    }
	    
	    @Override
	    public View getView(final int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        ImageHolder holder = null;
	        final NotificationList fav = data.get(position);
	        db  = new DatabaseHandler(mcontext);
	        if(row == null)
	        {
	            LayoutInflater inflater = ((Activity)mcontext).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	            row=inflater.inflate(R.layout.screen_list, null);
	            notifyDataSetChanged();
	            holder = new ImageHolder();
	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (ImageHolder)row.getTag();
	        }
	    
	       final ImageHolder hold = new ImageHolder();
	      
	       hold.txtname = (TextView)row.findViewById(R.id.nameText);
	       hold.txtname.setText(fav.getName());
	       hold.txtdate = (TextView)row.findViewById(R.id.dateText);
	       hold.txtdate.setText(fav.getDate());
	       
	      return row;
	    }
	    
	}
