package com.mobi.efficacious.esmartrestaurant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.common.ConnectionDetector;

import java.util.Timer;
import java.util.TimerTask;


public class Splash extends Activity
{
	TimerTask mTimerTask;
	Timer mTimer;
	SQLiteDatabase database;
	Context mContext;
	ConnectionDetector cd;
	String flag;
	private static final String PREFRENCES_NAME = "myprefrences";
	SharedPreferences settings;
	String RESTAURANTID;
	String USERTYPEID;
	String EMPLOYEEID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		cd = new ConnectionDetector(getApplicationContext());
		settings = getSharedPreferences(PREFRENCES_NAME,Context.MODE_PRIVATE);
		RESTAURANTID= settings.getString("TAG_RESTAURANTID", "");
		USERTYPEID= settings.getString("TAG_USERTYPEID", "");
		EMPLOYEEID= settings.getString("TAG_EMPLOYEEID", "");
		 mContext=Splash.this;
		 cd = new ConnectionDetector(getApplicationContext());
		 if (!cd.isConnectingToInternet()) 
		 {
			 AlertDialog.Builder alert = new AlertDialog.Builder(Splash.this);
			 alert.setMessage("No Internet Connection");
			 alert.setPositiveButton("OK",null);
			 alert.show();
		 }
		 else
		 {
		 	try
			{
				mTimerTask=new TimerTask()
				{
					@Override
					public void run()
					{
						if(EMPLOYEEID.equalsIgnoreCase(""))
						{
							Intent HomeScreenIntent=new Intent(mContext, LoginActivity.class);
							startActivity(HomeScreenIntent);
							finish();
						}
						else
						{
							if(USERTYPEID.equalsIgnoreCase("1"))
							{
								Intent MainScreenIntent=new Intent(Splash.this, MainActivity.class);
								startActivity(MainScreenIntent);
								finish();
							}
							else if (USERTYPEID.equalsIgnoreCase("2"))
							{
								Intent MainScreenIntent=new Intent(Splash.this, MainActivity.class);
								startActivity(MainScreenIntent);
								finish();
							}
							else
							{
								Intent MainScreenIntent=new Intent(Splash.this, KitchenActivity.class);
								startActivity(MainScreenIntent);
								finish();
							}

						}
					}
				};
				mTimer=new Timer();
				mTimer.schedule(mTimerTask, 1000);
			}catch (Exception ex)
			{

			}

		}		
	}       
}
