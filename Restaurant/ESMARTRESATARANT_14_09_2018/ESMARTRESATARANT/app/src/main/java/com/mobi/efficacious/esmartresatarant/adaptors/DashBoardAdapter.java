package com.mobi.efficacious.esmartresatarant.adaptors;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.esmartresatarant.R;
import com.mobi.efficacious.esmartresatarant.entity.Tables;


public class DashBoardAdapter extends ArrayAdapter<Tables> {
	Context context;
	static int layoutResourceId;
	ArrayList<Tables> table = new ArrayList<Tables>();
	TextView txtTitle;
	ImageView imageItem;
	String TableName;

	public DashBoardAdapter(Context context, ArrayList<Tables> tables) {
		super(context, layoutResourceId, tables);
		this.context = context;
		this.table = tables;
	}

	public View getView(int position, View convertView, ViewGroup parent) 
	{
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View rowView = inflater.inflate(R.layout.new_dashboardrow_layout, parent, false);
  
    	txtTitle = (TextView) rowView.findViewById(R.id.item_text);
		imageItem = (ImageView) rowView.findViewById(R.id.item_image);
        txtTitle.setText(table.get(position).getTable_Name());
        TableName = table.get(position).getTable_Name();
		imageItem.setImageResource(R.drawable.table);
        return rowView;
    }
}