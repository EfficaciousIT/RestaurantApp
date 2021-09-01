package com.mobi.efficacious.adapter;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.model.Categories;
import com.mobi.efficacious.model.CustomerWiseOrder;
import com.mobi.efficacious.restaurant.CategoryActivity;
import com.mobi.efficacious.restaurant.MenuActivity;
import com.mobi.efficacious.restaurant.R;
import com.mobi.efficacious.restaurant.TrasactionDetailActivity;
import com.mobi.efficacious.webservice.Constants;
import com.mobi.efficacious.webservice.Responce;

public class MyTrasactionAdopter extends BaseAdapter {
	Context context;
	static int layoutResourceId;
	static ArrayList<CustomerWiseOrder> data = new ArrayList<CustomerWiseOrder>();
	TextView txtAmt;
	TextView txtDate;
	TextView txtId;
	LinearLayout top;
	LinearLayout bottom;
	private static final String PREFRENCES_NAME = "myprefrences";
	SharedPreferences settings;
	private static  String SOAP_ACTION = "";	 
	private static  String OPERATION_NAME = "GetAboutADSXML";	 
	public static String strURL;
	String OrderId;
	public MyTrasactionAdopter(Context context,ArrayList<CustomerWiseOrder> gridArray) {
		super();
		this.context = context;
		this.data = gridArray;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			View row = inflater.inflate(R.layout.mytrasactionadopter_row, parent, false);
			top = (LinearLayout) row.findViewById(R.id.top_mytrasaction);
			bottom = (LinearLayout) row.findViewById(R.id.bottom_mytrasaction);
			txtId = (TextView) row.findViewById(R.id.Id_mytrasaction);
			txtDate = (TextView) row.findViewById(R.id.Date_mytrasaction);
			txtAmt = (TextView) row.findViewById(R.id.TotalAmt_mytrasaction);
			top.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context, TrasactionDetailActivity.class);
					intent.putExtra("OrderId", OrderId); 
					context.startActivity(intent);
				}
			});
			
			bottom.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context, TrasactionDetailActivity.class);
					intent.putExtra("OrderId",  OrderId); 
					context.startActivity(intent);
				}
			});
			
			OrderId = data.get(position).getOrder_Id();
			txtId.setText("Bill No: "+" " + data.get(position).getOrder_Id());
			txtDate.setText("Date: "+" " + data.get(position).getCreated_Date());
			txtAmt.setText("Total Amount: "+" "+data.get(position).getTotal());
			
		return row;
	}

	  @Override
	    public int getCount() {
	        return data.size();
	    }

	    @Override
	    public Object getItem(int position) {
	        return data.get(position);
	    }

	    @Override
	    public long getItemId(int position) {
	        return position;
	    }
	    
	    
}
