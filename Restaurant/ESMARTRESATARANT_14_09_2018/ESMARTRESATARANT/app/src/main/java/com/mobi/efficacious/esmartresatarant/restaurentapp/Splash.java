package com.mobi.efficacious.esmartresatarant.restaurentapp;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Window;

import com.mobi.efficacious.esmartresatarant.R;
import com.mobi.efficacious.esmartresatarant.common.AlertDialogManager;
import com.mobi.efficacious.esmartresatarant.common.ConnectionDetector;


public class Splash extends Activity
{
	TimerTask mTimerTask;
	Timer mTimer;
	SQLiteDatabase database;
	Context mContext;
	ConnectionDetector cd;
	AlertDialogManager alert;
	String flag;
	private static final String PREFRENCES_NAME = "myprefrences";
	SharedPreferences settings;
	String RESTAURANTID;
	String USERTYPEID;
	String EMPLOYEEID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_splash);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title_spash);
		settings = getSharedPreferences(PREFRENCES_NAME,Context.MODE_PRIVATE);
		RESTAURANTID= settings.getString("TAG_RESTAURANTID", "");
		USERTYPEID= settings.getString("TAG_USERTYPEID", "");
		EMPLOYEEID= settings.getString("TAG_EMPLOYEEID", "");
		
		 mContext=Splash.this;
		 alert = new AlertDialogManager();
		 cd = new ConnectionDetector(getApplicationContext());
		 if (!cd.isConnectingToInternet()) 
		 {
			alert.showAlertDialog(mContext,"Internet Connection Error","Please connect to working Internet connection", false);
			return;
		 }
		 else
		 {
			 mTimerTask=new TimerTask() 
		     {			
				@Override
				public void run() 
				{
					if(EMPLOYEEID.equalsIgnoreCase(""))
					{
						 Intent HomeScreenIntent=new Intent(mContext, MainActivity.class);
						 startActivity(HomeScreenIntent);
						 finish();
					}
					else
					{
						if(USERTYPEID.equalsIgnoreCase("1"))
						{
							Intent MainScreenIntent=new Intent(mContext, TakeOrderActivity.class);
							startActivity(MainScreenIntent);	
							finish();
						}
						else if (USERTYPEID.equalsIgnoreCase("2"))
						{
							Intent MainScreenIntent=new Intent(mContext, TakeOrderActivity.class);
							startActivity(MainScreenIntent);	
							finish();
						}
						else 
						{
							Intent MainScreenIntent=new Intent(mContext, KitchenActivity.class);
							startActivity(MainScreenIntent);	
							finish();
						}												
					}							
				}
			};  
			mTimer=new Timer();
			mTimer.schedule(mTimerTask, 5000);
		}		
	}       
}
