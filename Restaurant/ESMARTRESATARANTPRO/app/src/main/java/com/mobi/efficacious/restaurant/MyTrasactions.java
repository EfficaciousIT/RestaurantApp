package com.mobi.efficacious.restaurant;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobi.efficacious.adapter.MyTrasactionAdopter;
import com.mobi.efficacious.common.AlertDialogManager;
import com.mobi.efficacious.common.ConnectionDetector;
import com.mobi.efficacious.model.CustomerWiseOrder;
import com.mobi.efficacious.webservice.Constants;
import com.mobi.efficacious.webservice.Responce;

public class MyTrasactions extends Activity{
	ListView listview;
	String RegisterId;
	MyTrasactionAdopter adapter;
	ArrayList<CustomerWiseOrder> gridArray = new ArrayList<CustomerWiseOrder>();
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
		setContentView(R.layout.mytrasaction_layout);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title_order);
		mContext=MyTrasactions.this;
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//		DeviceId = tm.getDeviceId();
//
//        if(DeviceId==null)
//        {
//        	DeviceId= Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
//        }
        
		settings = getSharedPreferences(PREFRENCES_NAME,Context.MODE_PRIVATE);
		RegisterId = settings.getString("TAG_REGISTERID", "");
		alert = new AlertDialogManager();
		cd = new ConnectionDetector(getApplicationContext());
		listview = (ListView)findViewById(R.id.gridView_myTrasactions);
		getTraction();
	}
	
	public void getTraction()
	{
		OPERATION_NAME = "CustomerWiseOrder";	
		SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;		
		final Responce responce=new Responce();
		SoapObject response = null;	
		String result = null;
		try
		{  
			SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);				
			request.addProperty("command", "select");
			request.addProperty("ResId", 1);
			request.addProperty("RegisterId", RegisterId);

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
	  				        	CustomerWiseOrder ord =new CustomerWiseOrder();
	  				        	if(res.contains("No record found"))
								{
									
								}
								else
								{										
									if(isValidProperty(str2, "Order_Id"))
									{
										ord.setOrder_Id(str2.getProperty("Order_Id").toString().trim());
									}
									
									if(isValidProperty(str2, "Total"))
									{
										ord.setTotal(str2.getProperty("Total").toString().trim());
									}	
									
									if(isValidProperty(str2, "Created_Date"))
									{
										ord.setCreated_Date(str2.getProperty("Created_Date").toString().trim());
									}	
									
									gridArray.add(ord);
								}	
	  				        }
	  				     }     
	            	  adapter = new MyTrasactionAdopter(mContext, gridArray);
	            	  listview.setAdapter(adapter);	
		         } 
	        	else
	        	{
	        		Toast.makeText(mContext, "Null Response",Toast.LENGTH_LONG).show();
	        	}
	        }
	        else
	        {
	        	Toast.makeText(mContext, "Error",Toast.LENGTH_LONG).show();
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
