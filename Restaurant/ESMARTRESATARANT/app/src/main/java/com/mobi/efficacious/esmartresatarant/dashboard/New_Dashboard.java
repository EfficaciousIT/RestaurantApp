package com.mobi.efficacious.esmartresatarant.dashboard;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.esmartresatarant.R;
import com.mobi.efficacious.esmartresatarant.adaptors.DashBoardAdapter;
import com.mobi.efficacious.esmartresatarant.common.AlertDialogManager;
import com.mobi.efficacious.esmartresatarant.common.ConnectionDetector;
import com.mobi.efficacious.esmartresatarant.entity.Tables;
import com.mobi.efficacious.esmartresatarant.restaurentapp.CategoryActivity;
import com.mobi.efficacious.esmartresatarant.webservice.Constants;
import com.mobi.efficacious.esmartresatarant.webservice.Responce;


public class New_Dashboard extends Activity {
	GridView gridView;
	String RESTAURANTID;
	ArrayList<Tables> tables = new ArrayList<Tables>();
	DashBoardAdapter adapter;
	SharedPreferences sharedpreferences;
	ConnectionDetector cd;
	AlertDialogManager alert;
	RelativeLayout relative;
	private static final String PREFRENCES_NAME = "myprefrences";
	SharedPreferences settings;
	private static String SOAP_ACTION = "";
	private static String OPERATION_NAME = "GetAboutADSXML";
	public static String strURL;
	Context mContext;
	String OrderId;
	String TableName;
	String DeviceId;
	String EmployeeId;
	String RegisterId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.new_dashboard_layout);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title_home);
		mContext = New_Dashboard.this;
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		DeviceId = tm.getDeviceId();
        if(DeviceId==null)
        {
        	DeviceId= Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        }
		settings = getSharedPreferences(PREFRENCES_NAME,Context.MODE_PRIVATE);
		RESTAURANTID = settings.getString("TAG_RESTAURANTID", "");
		EmployeeId = settings.getString("TAG_EMPLOYEEID", "");
		RegisterId = settings.getString("TAG_REGISTERID", "");
		if(RegisterId.equalsIgnoreCase(""))
		{
			RegisterId = "0";
		}
		
		alert = new AlertDialogManager();
		cd = new ConnectionDetector(getApplicationContext());
		gridView = (GridView)findViewById(R.id.gridView1);
		gridView.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
		    {
				TableName = ((TextView) view.findViewById(R.id.item_text)).getText().toString();
				InsertOrder();
				Intent intent=new Intent(New_Dashboard.this, CategoryActivity.class);
				intent.putExtra("TableName",TableName);
				intent.putExtra("OrderId",OrderId);
				startActivity(intent);
				finish();
		    }
		});
		
		PostData();
	}
	
	public void PostData()
	{ 
		if (!cd.isConnectingToInternet()) 
		 {
			alert.showAlertDialog(New_Dashboard.this,"No Internet Connection","Please connect to working Internet connection", false);
			return;
		 }
		 else
		 {
			OPERATION_NAME = "GetTable";	
			SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
			final Responce responce=new Responce();
			SoapObject response = null;	
			String result = null;
			try
			{  
				SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);				
				request.addProperty("command", "select");
				request.addProperty("res_id", 1);
			
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
		  				        	Tables tab =new Tables();
		  				        	if(res.contains("No record found"))
									{
										
									}
									else
									{										
										if(isValidProperty(str2, "Table_Name"))
										{
											tab.setTable_Name(str2.getProperty("Table_Name").toString().trim());
										}
																				
										tables.add(tab);
									}
		  				        	
		  				        }
		  				    
		  				      }
		            	 
		            	  adapter = new DashBoardAdapter(mContext, tables);
		            	  gridView.setAdapter(adapter);		            	 
			         } 
		        	else
		        	{
		        		Toast.makeText(New_Dashboard.this, "Null Response",Toast.LENGTH_LONG).show();
		        	}
		        }
		        else
		        {
		        	Toast.makeText(New_Dashboard.this, "Error",Toast.LENGTH_LONG).show();
		        }			          
	        }	       
			catch (Exception e) 
			{		
				e.printStackTrace();
			}	
		 }
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
			request.addProperty("TableName", TableName);
			request.addProperty("RegisterId", RegisterId);
			request.addProperty("EmployeeId", EmployeeId);
			request.addProperty("DeviceId", DeviceId);
			request.addProperty("IsActive", 1);
			request.addProperty("status", "Open");
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
	        		Toast.makeText(New_Dashboard.this, "Null Response",Toast.LENGTH_LONG).show();
	        	}
	        }
	        else
	        {
	        	Toast.makeText(New_Dashboard.this, "Error",Toast.LENGTH_LONG).show();
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
