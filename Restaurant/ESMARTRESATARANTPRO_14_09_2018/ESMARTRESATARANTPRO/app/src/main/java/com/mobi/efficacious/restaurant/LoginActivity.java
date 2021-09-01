package com.mobi.efficacious.restaurant;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.common.AlertDialogManager;
import com.mobi.efficacious.common.ConnectionDetector;
import com.mobi.efficacious.webservice.Constants;
import com.mobi.efficacious.webservice.Responce;
import com.mobi.efficacious.webservice.Utility;

public class LoginActivity extends Activity implements OnClickListener, OnItemSelectedListener
{
	Button btnSubmit;
	Button btnCancel;
	Context mContext;
	EditText UserName;
	EditText Password;
	Spinner RoleName;
	String loggerName;
	Utility utility;
	TextView txtforgotPassword;
	String[] Roles = { "--Select--", "Manager", "Waiter","Kitchen"}; 
	int loggerId;
	String User_name;
	String passWord;
	String flag;
	String RESTAURANTID;
	String USERTYPEID;
	String EMPLOYEEID;
	private static  String SOAP_ACTION = "";	 
	private static  String OPERATION_NAME = "GetAboutADSXML";	 
	public static String strURL;
	TelephonyManager tel;
	ConnectionDetector cd;
	AlertDialogManager alert;
	private static final String PREFRENCES_NAME = "myprefrences";
	SharedPreferences settings;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_login);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title_login);
		settings = getSharedPreferences(PREFRENCES_NAME,Context.MODE_PRIVATE);
		mContext=LoginActivity.this;
		alert = new AlertDialogManager();
		cd = new ConnectionDetector(getApplicationContext());
		RoleName=(Spinner)findViewById(R.id.RoleName_Login);
		UserName=(EditText)findViewById(R.id.UserName_Login);
		Password=(EditText)findViewById(R.id.Password_Login);
		btnSubmit=(Button)findViewById(R.id.Submit_login);
		btnCancel=(Button)findViewById(R.id.Cancel_Login);
		txtforgotPassword = (TextView)findViewById(R.id.ForgotPassword_Login);
		txtforgotPassword.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence> (this, android.R.layout.simple_spinner_item, Roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
        RoleName.setAdapter(adapter); 
        RoleName.setOnItemSelectedListener(this);
	}
	
	@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()) 
        {
          case R.id.RoleName_Login:
            loggerId = position;
            break;
          default:
          break;
        }
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
			case R.id.Submit_login:
				PostData();
			break;
//			case R.id.ForgotPassword_Login:	
//				Intent ForgotPasswordScreenIntent=new Intent(mContext, ForgotPassword.class);
//				startActivity(ForgotPasswordScreenIntent);
//				//finish();
//			break;
			default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
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
			OPERATION_NAME = "GetLoginDetails";	
			SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
			final Responce responce=new Responce();
			SoapObject response = null;	
			String result = null;
			try
			{
				SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
				request.addProperty("command", "select");
				request.addProperty("userType_id", loggerId);
				request.addProperty("userName",  UserName.getText().toString());
				request.addProperty("password", Password.getText().toString());	
						
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
		        		 if(result.contains("no record found"))
			              {
			               	Toast.makeText(this, "Record not found", Toast.LENGTH_LONG).show();
			              }
		        		  else
        				  {
		        			 SoapObject root = (SoapObject) response.getProperty(0);
			            	 SoapObject table = (SoapObject) root.getProperty("NewDataSet");
			            	 SoapObject column = (SoapObject) table.getProperty("Login");
			            	 String res = column.toString();
			            	
				            	 if(res.contains("Restaurant_id"))
			            		 {
				            		 RESTAURANTID=column.getProperty("Restaurant_id").toString();
				            		 settings.edit().putString("TAG_RESTAURANTID", RESTAURANTID).commit();
			            		 }
			            		 else
			            		 {
			            			 
			            		 }
				            	 
				            	 if(res.contains("UserType_Id"))
			            		 {
				            		 USERTYPEID=column.getProperty("UserType_Id").toString();
				            		 settings.edit().putString("TAG_USERTYPEID", USERTYPEID).commit();
			            		 }
			            		 else
			            		 {
			            			 
			            		 }
				            	 
				            	 if(res.contains("Employee_Id"))
			            		 {
				            		 EMPLOYEEID=column.getProperty("Employee_Id").toString();
				            		 settings.edit().putString("TAG_EMPLOYEEID", EMPLOYEEID).commit();
			            		 }
			            		 else
			            		 {
			            			 
			            		 }
				            	 
//				            	 if(USERTYPEID.equalsIgnoreCase("1"))
//				            	 {
//				            		 Intent MainScreenIntent=new Intent(LoginActivity.this, TakeOrderActivity.class);
//						 			 startActivity(MainScreenIntent);
//						 			 finish();
//				            	 }
//				            	 else if(USERTYPEID.equalsIgnoreCase("2"))
//				            	 {
//				            		 Intent MainScreenIntent=new Intent(LoginActivity.this, TakeOrderActivity.class);
//						 			 startActivity(MainScreenIntent);
//						 			 finish();
//				            	 }
//				            	 else 
//				            	 {
				            		 Intent MainScreenIntent=new Intent(LoginActivity.this, MainActivity.class);
						 			 startActivity(MainScreenIntent);
						 			 finish();
//				            	 }
				            	
        				  }		 
		        		 	 
		           	}
		        	else
		        	{
		        		Toast.makeText(this, "No Response",Toast.LENGTH_LONG).show();
		        	}	  
		        }
		        else
		        {
		        	Toast.makeText(this, "Error",Toast.LENGTH_LONG).show();
		        }
		    }	       
			catch (Exception e) 
			{		
				e.printStackTrace();
			}	
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
