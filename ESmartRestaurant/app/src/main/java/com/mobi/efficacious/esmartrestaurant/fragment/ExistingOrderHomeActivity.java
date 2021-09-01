package com.mobi.efficacious.esmartrestaurant.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.activity.MainActivity;
import com.mobi.efficacious.esmartrestaurant.adapter.HomeAdapter;
import com.mobi.efficacious.esmartrestaurant.common.ConnectionDetector;
import com.mobi.efficacious.esmartrestaurant.entity.Home;

import java.util.ArrayList;


public class ExistingOrderHomeActivity extends Fragment {
	GridView gridview;
	String RESTAURANTID;
	HomeAdapter adapter;
	ArrayList<Home> gridArray = new ArrayList<Home>();
	SharedPreferences sharedpreferences;
	ConnectionDetector cd;
	RelativeLayout relative;
	private static final String PREFRENCES_NAME = "myprefrences";
	SharedPreferences settings;
	private static  String SOAP_ACTION = "";	 
	private static  String OPERATION_NAME = "GetAboutADSXML";	 
	public static String strURL;
	Context mContext;
	String TableName;
	String OrderId;
	View myview;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		myview=inflater.inflate(R.layout.existingorderhome_layout,null);
		mContext=getActivity();
		TableName = getArguments().getString("TableName");
		OrderId = getArguments().getString("OrderId");
		settings = getActivity().getSharedPreferences(PREFRENCES_NAME,Context.MODE_PRIVATE);
		RESTAURANTID = settings.getString("TAG_RESTAURANTID", "");
		cd = new ConnectionDetector(getActivity());
		gridview = (GridView)myview.findViewById(R.id.gridView_existingorderhome);

		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
									int position, long id)
			{
				if(position==0)
				{
                    CategoryActivity categoryActivity = new CategoryActivity();
                    Bundle args = new Bundle();
                    args.putString("TableName",TableName);
                    args.putString("OrderId",OrderId);
                    categoryActivity.setArguments(args);
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, categoryActivity).commitAllowingStateLoss();

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

		Bitmap attendance = BitmapFactory.decodeResource(this.getResources(), R.mipmap.neworder);
		// Bitmap leave = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
		Bitmap calendar = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ex_oder);

		gridArray.add(new Home(attendance,"Add to Order"));
		// gridArray.add(new Item(leave,"Existing Order"));
		gridArray.add(new Home(calendar,"View Order"));

		adapter =  new HomeAdapter(getActivity(),R.layout.homeadapter_row, gridArray);
		gridview.setAdapter(adapter);
		return myview;
	}

}
