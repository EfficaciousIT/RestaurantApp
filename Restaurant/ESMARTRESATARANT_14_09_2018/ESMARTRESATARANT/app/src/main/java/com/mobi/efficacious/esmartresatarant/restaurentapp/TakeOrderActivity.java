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
import com.mobi.efficacious.esmartresatarant.adaptors.TakeOrderAdapter;
import com.mobi.efficacious.esmartresatarant.common.AlertDialogManager;
import com.mobi.efficacious.esmartresatarant.common.ConnectionDetector;
import com.mobi.efficacious.esmartresatarant.dashboard.New_Dashboard;
import com.mobi.efficacious.esmartresatarant.entity.Item;

public class TakeOrderActivity extends Activity{
	GridView gridview;
	String RESTAURANTID;
	TakeOrderAdapter adapter;
	ArrayList<Item> gridArray = new ArrayList<Item>();
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.takeorder_layout);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title_order);
		mContext=TakeOrderActivity.this;
		settings = getSharedPreferences(PREFRENCES_NAME,Context.MODE_PRIVATE);
		RESTAURANTID = settings.getString("TAG_RESTAURANTID", "");
		alert = new AlertDialogManager();
		cd = new ConnectionDetector(getApplicationContext());
		gridview = (GridView)findViewById(R.id.gridView_takeorder);
		
		gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
            int position, long id) 
        	{
            	 if(position==0)
		         {
            		 Intent MainScreenIntent=new Intent(mContext, New_Dashboard.class);
					 startActivity(MainScreenIntent);	
					 //finish();
		         }
            	 else if(position==1)
            	 {
            		 Intent MainScreenIntent=new Intent(mContext, ExistingOrderedTableActivity.class);
					 startActivity(MainScreenIntent);	
            	 }
            	 else if(position==2)
            	 {
            		 Intent MainScreenIntent=new Intent(mContext, TakeAwayActivity.class);
					 startActivity(MainScreenIntent);	
            	 }
            	 else
            	 {
            		 Intent MainScreenIntent=new Intent(mContext, ViewCategoryActivity.class);
					 startActivity(MainScreenIntent);	
					 //finish();
            	 }
        	}
        });
		
		 Bitmap NewOrder = BitmapFactory.decodeResource(this.getResources(), R.drawable.new_order);
		 Bitmap ExistingOrder = BitmapFactory.decodeResource(this.getResources(), R.drawable.ex_oder);
		 Bitmap TakeAway = BitmapFactory.decodeResource(this.getResources(), R.drawable.take_away);
		 Bitmap ViewMenu = BitmapFactory.decodeResource(this.getResources(), R.drawable.menu);
		 
		 gridArray.add(new Item(NewOrder,"New Order"));
		 gridArray.add(new Item(ExistingOrder,"Existing Order"));
		 gridArray.add(new Item(TakeAway,"Take Away"));
		 gridArray.add(new Item(ViewMenu,"View Menu"));
		 
		 adapter =  new TakeOrderAdapter(this,R.layout.takeoreder_row, gridArray);
		 gridview.setAdapter(adapter);
	}
}
