package com.mobi.efficacious.esmartresatarant.restaurentapp;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.esmartresatarant.R;
import com.mobi.efficacious.esmartresatarant.common.AlertDialogManager;
import com.mobi.efficacious.esmartresatarant.common.ConnectionDetector;
import com.mobi.efficacious.esmartresatarant.webservice.Constants;
import com.mobi.efficacious.esmartresatarant.webservice.Responce;


public class EditOrderDetailsActivity extends Activity{
	String rate;
	String qty;
	String Id;
	String Menu;
	String TableName;
	String OrderId;
	TextView txtMenu;
	TextView txtRate;
	EditText edtQty;
	TextView txtDelete;
	TextView txtUpdate;
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
		setContentView(R.layout.editorderdetails_layout);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title_order);
		mContext = EditOrderDetailsActivity.this;
		alert = new AlertDialogManager();
		cd = new ConnectionDetector(getApplicationContext());
		
		Id = getIntent().getStringExtra("Id");
		Menu = getIntent().getStringExtra("MenuName");
		qty = getIntent().getStringExtra("Qty");
		rate = getIntent().getStringExtra("Price");
		TableName = getIntent().getStringExtra("TableName");
		OrderId = getIntent().getStringExtra("OrderId");
		
		txtMenu = (TextView)findViewById(R.id.IteamName_editOrder);
		txtRate = (TextView)findViewById(R.id.price_editOrder);
		edtQty = (EditText)findViewById(R.id.qty_editorder);
		txtUpdate = (TextView)findViewById(R.id.Update_editorder);
		txtDelete = (TextView)findViewById(R.id.Delete_editorder);
		
		txtUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PostUpdate();
			}
		});
		
		txtDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PostDelete();
			}
		});
		
		txtMenu.setText(Menu);
		edtQty.setText(qty);
		txtRate.setText(rate);
	}
	
	public void PostUpdate()
	{ 
		if (!cd.isConnectingToInternet()) 
		 {
			alert.showAlertDialog(EditOrderDetailsActivity.this,"No Internet Connection","Please connect to working Internet connection", false);
			return;
		 }
		 else
		 {
			OPERATION_NAME = "UpdateOrderTrasaction";	
			SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
			final Responce responce=new Responce();
			SoapObject response = null;	
			String result = null;
			try
			{  
				SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);				
				request.addProperty("command", "update");
				request.addProperty("id", Id);
				request.addProperty("qty", edtQty.getText().toString());

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
		  				        	if(res.contains("true"))
									{
		  				        		Toast.makeText(EditOrderDetailsActivity.this, "Updated Sucessfully", Toast.LENGTH_LONG).show();
		  				        		 Intent intent=new Intent(mContext, OrderDetailsActivity.class);
		  			            		 intent.putExtra("TableName",TableName);
		  			     				 intent.putExtra("OrderId",OrderId);
		  			     				 startActivity(intent);	
		  								 finish();
									}
									else
									{	
										Toast.makeText(EditOrderDetailsActivity.this, "Not Updated", Toast.LENGTH_LONG).show();
									}
		  				        	
		  				        }
		  				    
		  				    }            	 
			         } 
		        	else
		        	{
		        		Toast.makeText(EditOrderDetailsActivity.this, "Null Response",Toast.LENGTH_LONG).show();
		        	}
		        }
		        else
		        {
		        	Toast.makeText(EditOrderDetailsActivity.this, "Error",Toast.LENGTH_LONG).show();
		        }			          
	        }	       
			catch (Exception e) 
			{		
				e.printStackTrace();
			}	
		 }
	}
	
	public void PostDelete()
	{ 
		if (!cd.isConnectingToInternet()) 
		 {
			alert.showAlertDialog(EditOrderDetailsActivity.this,"No Internet Connection","Please connect to working Internet connection", false);
			return;
		 }
		 else
		 {
			OPERATION_NAME = "DeleteOrderTrasaction";	
			SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;		
			final Responce responce=new Responce();
			SoapObject response = null;	
			String result = null;
			try
			{  
				SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);				
				request.addProperty("command", "delete");
				request.addProperty("id", Id);

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
		  				        	if(res.contains("true"))
									{
		  				        		Toast.makeText(EditOrderDetailsActivity.this, "Deleted Sucessfully", Toast.LENGTH_LONG).show();
		  				        		 Intent intent=new Intent(mContext, OrderDetailsActivity.class);
		  			            		 intent.putExtra("TableName",TableName);
		  			     				 intent.putExtra("OrderId",OrderId);
		  			     				 startActivity(intent);	
		  								 finish();
									}
									else
									{	
										Toast.makeText(EditOrderDetailsActivity.this, "Not Deleted", Toast.LENGTH_LONG).show();
									}
		  				        	
		  				        }
		  				    
		  				    }            	 
			         } 
		        	else
		        	{
		        		Toast.makeText(EditOrderDetailsActivity.this, "Null Response",Toast.LENGTH_LONG).show();
		        	}
		        }
		        else
		        {
		        	Toast.makeText(EditOrderDetailsActivity.this, "Error",Toast.LENGTH_LONG).show();
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
	
	 public interface EditAgeDialogListener {
	        void EditAgeDialogResult(String inputText);
	 }
}
