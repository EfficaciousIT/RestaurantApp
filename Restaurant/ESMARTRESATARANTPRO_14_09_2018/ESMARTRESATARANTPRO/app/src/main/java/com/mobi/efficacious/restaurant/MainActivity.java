package com.mobi.efficacious.restaurant;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobi.efficacious.adapter.TakeOrderAdapter;
import com.mobi.efficacious.common.AlertDialogManager;
import com.mobi.efficacious.common.ConnectionDetector;
import com.mobi.efficacious.model.Item;
import com.mobi.efficacious.webservice.Constants;
import com.mobi.efficacious.webservice.Responce;

public class MainActivity extends Activity{
	GridView gridview;
	String RegisterId;
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
	String DeviceId;
	String OrderId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.takeorder_layout);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title_home);
		mContext=MainActivity.this;
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//		DeviceId = tm.getDeviceId();
//        if(DeviceId==null)
//        {
//        	DeviceId= Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
//        }
        
		settings = getSharedPreferences(PREFRENCES_NAME,Context.MODE_PRIVATE);
		RegisterId = settings.getString("TAG_REGISTERID", "");
		alert = new AlertDialogManager();
		cd = new ConnectionDetector(getApplicationContext());
		gridview = (GridView)findViewById(R.id.gridView_takeorder);
		
//		Intent i= new Intent(MainActivity.this, MyService.class);
//	    startService(i);
	    
		gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
            int position, long id) 
        	{
            	 if(position==0)
		         {
            		 InsertOrder();
            		 Intent intent=new Intent(mContext, CategoryActivity.class);
     				 intent.putExtra("OrderId",OrderId);
					 startActivity(intent);	
		         }
            	 else if(position==1)
            	 {
            		 Intent MainScreenIntent=new Intent(mContext, MyTrasactions.class);
					 startActivity(MainScreenIntent);	
            	 }
            	 else if(position==2)
            	 {
            		 Intent MainScreenIntent=new Intent(mContext, ViewCategoryActivity.class);
            		 startActivity(MainScreenIntent);	
            	 }
            	 else
            	 {
            		 android.app.FragmentManager fragmentManager = getFragmentManager();
    			     SettingDialog gallerydialog = new SettingDialog();
    			     gallerydialog.setRetainInstance(true);
    			     gallerydialog.show(fragmentManager, "fragment_name");
            	 }
        	}
        });
		
		 Bitmap NewOrder = BitmapFactory.decodeResource(this.getResources(), R.drawable.new_order);
		 Bitmap TakeAway = BitmapFactory.decodeResource(this.getResources(), R.drawable.my_tra);
		 Bitmap ViewMenu = BitmapFactory.decodeResource(this.getResources(), R.drawable.menu);
		 Bitmap Setting = BitmapFactory.decodeResource(this.getResources(), R.drawable.setting);
		 
		 gridArray.add(new Item(NewOrder,"New Order"));
		 gridArray.add(new Item(TakeAway,"My Trasactions"));
		 gridArray.add(new Item(ViewMenu,"View Menu"));
		 gridArray.add(new Item(Setting,"Setting"));
		 
		 adapter =  new TakeOrderAdapter(this,R.layout.takeoreder_row, gridArray);
		 gridview.setAdapter(adapter);
	}
	
	public void InsertOrder()
	{
		OPERATION_NAME = "InsertOrder";	
		SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;		
		final Responce responce=new Responce();
		SoapObject response = null;	
		String result = null;
		try
		{  
			SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);				
			request.addProperty("command", "insert");
			request.addProperty("TableName", "TakeAway");
			request.addProperty("RegisterId", RegisterId);
			request.addProperty("EmployeeId", 0);
			request.addProperty("DeviceId", DeviceId);
			request.addProperty("IsActive", 1);
			request.addProperty("status", "TakeAway");
			request.addProperty("resId", 1);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet=true;
			envelope.setOutputSoapObject(request);
	      
	        HttpTransportSE ht = new HttpTransportSE(Constants.strWEB_SERVICE_URL);
	        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy); 
	        ht.debug=true;
	        ht.call(SOAP_ACTION, envelope);	
	        if(!(envelope.bodyIn instanceof SoapFault))
	        {
	        	if(envelope.bodyIn instanceof SoapObject)
	        		response = (SoapObject)envelope.bodyIn;
	        	if(response != null)
	        	{
	        		result=response.getProperty(0).toString(); 
	            	  if(response!=null)
	  		          {
	  		        	 SoapObject str = null;
	  				        for(int i=0;i<response.getPropertyCount();i++)
	  				              	str=(SoapObject) response.getProperty(i);     	
	  				       
	  				        SoapObject str1 = (SoapObject) str.getProperty(0);
	  				        
	  				        SoapObject str2 = null;
	  				       
	  				        for(int j=0;j<str1.getPropertyCount();j++)
	  				        {			        	
	  				        	str2 = (SoapObject) str1.getProperty(j);
	  				        	String res = str2.toString();
	  				        	
	  				        	if(res.contains("No record found"))
								{
									
								}
								else
								{										
									if(isValidProperty(str2, "OrderId"))
									{
										OrderId= str2.getProperty("OrderId").toString().trim();
									}										
								}	
	  				        }
	  				     }       	 
		         } 
	        	else
	        	{
	        		Toast.makeText(MainActivity.this, "Null Response",Toast.LENGTH_LONG).show();
	        	}
	        }
	        else
	        {
	        	Toast.makeText(MainActivity.this, "Error",Toast.LENGTH_LONG).show();
	        }			          
        }	       
		catch (Exception e) 
		{		
			e.printStackTrace();
		}	
	 }
	
	boolean isValidProperty(SoapObject soapObject,String PropertyName)
	{
		if(soapObject!=null)
		{
			if(soapObject.getProperty(PropertyName) != null)
			{
				if(!soapObject.getProperty(PropertyName).toString().equalsIgnoreCase("")&&!soapObject.getProperty(PropertyName).toString().equalsIgnoreCase("anyType{}"))
					return true;
			else
				return false;
			}
			return false;		
		}
		else
			return false;
	}
}
