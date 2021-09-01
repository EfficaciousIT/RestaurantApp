package com.mobi.efficacious.esmartresatarant.restaurentapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.mobi.efficacious.esmartresatarant.R;
import com.mobi.efficacious.esmartresatarant.adaptors.HomeAdapter;
import com.mobi.efficacious.esmartresatarant.common.AlertDialogManager;
import com.mobi.efficacious.esmartresatarant.common.ConnectionDetector;
import com.mobi.efficacious.esmartresatarant.entity.Home;


public class ExistingOrderHomeActivity extends Activity {
	GridView gridview;
	String RESTAURANTID;
	HomeAdapter adapter;
	ArrayList<Home> gridArray = new ArrayList<Home>();
	SharedPreferences sharedpreferences;
	ConnectionDetector cd;
	AlertDialogManager alert;
	RelativeLayout relative;
	private static final String PREFRENCES_NAME = "myprefrences";
	SharedPreferences settings;
	private static  String SOAP_ACTION = "";	 
	private static  String OPERATION_NAME = "GetAboutADSXML";	 
	public static String strURL;
	Context mContext;
	String TableName;
	String OrderId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.existingorderhome_layout);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title_order);
		mContext=ExistingOrderHomeActivity.this;
		TableName = getIntent().getStringExtra("TableName");
		OrderId = getIntent().getStringExtra("OrderId");
		settings = getSharedPreferences(PREFRENCES_NAME,Context.MODE_PRIVATE);
		RESTAURANTID = settings.getString("TAG_RESTAURANTID", "");
		alert = new AlertDialogManager();
		cd = new ConnectionDetector(getApplicationContext());
		gridview = (GridView)findViewById(R.id.gridView_existingorderhome);
		
		gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
            int position, long id) 
        	{
            	 if(position==0)
		         {
            		 Intent MainScreenIntent=new Intent(mContext, CategoryActivity.class);
            		 MainScreenIntent.putExtra("TableName",TableName);
            		 MainScreenIntent.putExtra("OrderId",OrderId);
					 startActivity(MainScreenIntent);	
					 //finish();
		         }
            	 else
            	 {
            		 Intent intent=new Intent(mContext, OrderDetailsActivity.class);
            		 intent.putExtra("TableName",TableName);
     				 intent.putExtra("OrderId",OrderId);
					 startActivity(intent);	
					 //finish();
            	 }
        	}
        });
		
		 Bitmap attendance = BitmapFactory.decodeResource(this.getResources(), R.drawable.new_order);
		// Bitmap leave = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
		 Bitmap calendar = BitmapFactory.decodeResource(this.getResources(), R.drawable.ex_oder);
		 
		 gridArray.add(new Home(attendance,"Add to Order"));
		// gridArray.add(new Item(leave,"Existing Order"));
		 gridArray.add(new Home(calendar,"View Order"));
		 
		 adapter =  new HomeAdapter(this,R.layout.homeadapter_row, gridArray);
		 gridview.setAdapter(adapter);
	}
}
