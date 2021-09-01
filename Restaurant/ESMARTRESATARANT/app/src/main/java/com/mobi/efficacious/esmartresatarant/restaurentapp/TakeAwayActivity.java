package com.mobi.efficacious.esmartresatarant.restaurentapp;

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
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobi.efficacious.esmartresatarant.R;
import com.mobi.efficacious.esmartresatarant.common.AlertDialogManager;
import com.mobi.efficacious.esmartresatarant.common.ConnectionDetector;
import com.mobi.efficacious.esmartresatarant.entity.Register;
import com.mobi.efficacious.esmartresatarant.webservice.Constants;
import com.mobi.efficacious.esmartresatarant.webservice.Responce;


public class TakeAwayActivity extends Activity implements OnClickListener {
	String IMEIKEY;
	String emailPattern;
	EditText FirstNameEditText;
	EditText MiddleNameEditText;
	EditText LastNameEditText;
	EditText EmailEditText;
	EditText MobileEditText;
	EditText Address1EditText;
	EditText Address2EditText;
	EditText Address3EditText;
	Button RegisterBTN;
	private static String SOAP_ACTION = "";
	private static String OPERATION_NAME = "GetAboutADSXML";
	public static String strURL;
	TelephonyManager tel;
	ConnectionDetector cd;
	AlertDialogManager alert;
	private static final String PREFRENCES_NAME = "myprefrences";
	SharedPreferences settings;
	Context mContext;
	String RESTAURANTID;
	String OrderId;
	String TableName = "0";
	String RegisterId;
	String EmployeeId;
	private static int RESULT_LOAD_IMAGE = 1;
	TelephonyManager telephonyManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.takeaway_layout);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title_order);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
		alert = new AlertDialogManager();
		cd = new ConnectionDetector(getApplicationContext());
		settings = getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
		RESTAURANTID = settings.getString("TAG_RESTAURANTID", "");
		EmployeeId = settings.getString("TAG_EMPLOYEEID", "");
		FirstNameEditText = (EditText) findViewById(R.id.FirstName_edittext_takeaway);
		MiddleNameEditText = (EditText) findViewById(R.id.MiddleName_edittext_takeaway);
		LastNameEditText = (EditText) findViewById(R.id.LastName_edittext_takeaway);
		EmailEditText = (EditText) findViewById(R.id.emailId_edittext_takeaway);
		MobileEditText = (EditText) findViewById(R.id.mobile_edittext_takeaway);
		Address1EditText = (EditText) findViewById(R.id.address1_edittext_takeaway);
		Address2EditText = (EditText) findViewById(R.id.address2_edittext_takeaway);
		Address3EditText = (EditText) findViewById(R.id.address3_edittext_takeaway);
		try {
			ActivityCompat.requestPermissions(TakeAwayActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE}, 1);
			telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
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
			IMEIKEY = telephonyManager.getDeviceId();
			if (IMEIKEY == null && IMEIKEY.isEmpty()) {
				IMEIKEY = "0";
			} else {
				IMEIKEY = IMEIKEY;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		RegisterBTN = (Button) findViewById(R.id.RegisterButton_takeaway);
		RegisterBTN.setOnClickListener(this);
//		try {
//			tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//				// TODO: Consider calling
//				//    ActivityCompat#requestPermissions
//				// here to request the missing permissions, and then overriding
//				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//				//                                          int[] grantResults)
//				// to handle the case where the user grants the permission. See the documentation
//				// for ActivityCompat#requestPermissions for more details.
//				return;
//			}
//			IMEIKEY = tel.getDeviceId().toString();
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null)
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
			IMEIKEY = telephonyManager.getDeviceId();

	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
			case R.id.RegisterButton_takeaway:
				PostData();
			break;
		}
	}
	
	public void PostData()
	{ 
		if (!cd.isConnectingToInternet()) 
		 {
			alert.showAlertDialog(mContext,"No Internet Connection","Please connect to working Internet connection", false);
			return;
		 }
		 else
		 {
			OPERATION_NAME = "GetRegister";	
			SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
			final Responce responce=new Responce();
			SoapObject response = null;	
			String result = null;
			try
			{
				SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
				request.addProperty("command", "insert");
				request.addProperty("firstName", FirstNameEditText.getText().toString());
				request.addProperty("middleName",  MiddleNameEditText.getText().toString());
				request.addProperty("lastName", LastNameEditText.getText().toString());	
				request.addProperty("mobileNo", MobileEditText.getText().toString());	
				request.addProperty("emailId", EmailEditText.getText().toString());
				request.addProperty("address1", Address1EditText.getText().toString());
				request.addProperty("address2", Address2EditText.getText().toString());
				request.addProperty("address3", Address3EditText.getText().toString());
				request.addProperty("resId", 1);
				request.addProperty("imeiNo", IMEIKEY);
				request.addProperty("gcm", "");
				
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
									Register cate =new Register();
		  				        	if(res.contains("No record found"))
									{
										
									}
									else
									{	
										if(isValidProperty(str2, "Register_Id"))
										{
											cate.setRegisterId(str2.getProperty("Register_Id").toString().trim());
											RegisterId = str2.getProperty("Register_Id").toString().trim();
											settings.edit().putString("TAG_REGISTERID", RegisterId).commit();
											InsertOrder();
										}	
										
									}
		  				        	
		  				        }
		  				    
		  				      }	
		        	 }
			        else
			        {
			        	Toast.makeText(TakeAwayActivity.this, "No Response",Toast.LENGTH_LONG).show();
			        }	
		        }
		        else
		        {
		        	Toast.makeText(TakeAwayActivity.this, "No Response",Toast.LENGTH_LONG).show();
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
			request.addProperty("TableName", "TakeAway");
			request.addProperty("RegisterId", RegisterId);
			request.addProperty("EmployeeId", EmployeeId);
			request.addProperty("DeviceId", IMEIKEY);
			request.addProperty("IsActive", 1);
			
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
										Intent intent=new Intent(TakeAwayActivity.this, CategoryActivity.class);
										intent.putExtra("TableName",TableName);
										intent.putExtra("OrderId",OrderId);
										startActivity(intent);
										finish();
									}										
								}	
	  				        }
	  				     }       	 
		         } 
	        	else
	        	{
	        		Toast.makeText(TakeAwayActivity.this, "Null Response",Toast.LENGTH_LONG).show();
	        	}
	        }
	        else
	        {
	        	Toast.makeText(TakeAwayActivity.this, "Error",Toast.LENGTH_LONG).show();
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