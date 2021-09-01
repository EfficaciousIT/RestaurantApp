package com.mobi.efficacious.restaurant;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import com.mobi.efficacious.webservice.Constants;
import com.mobi.efficacious.webservice.Responce;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditOrderDetailsActivity extends Activity{
	String rate;
	String qty;
	String Id;
	String Menu;
	TextView txtMenu;
	TextView txtRate;
	EditText edtQty;
	TextView btnDelete;
	TextView btnUpdate;
	private static final String PREFRENCES_NAME = "myprefrences";
	SharedPreferences settings;
	private static  String SOAP_ACTION = "";	 
	private static  String OPERATION_NAME = "GetAboutADSXML";	 
	public static String strURL;
	String OrderId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.editorderdetails_layout);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title_order);
		
		Id = getIntent().getStringExtra("Id");
		Menu = getIntent().getStringExtra("MenuName");
		qty = getIntent().getStringExtra("Qty");
		rate = getIntent().getStringExtra("Price");
		OrderId = getIntent().getStringExtra("OrderId");
		
		txtMenu = (TextView)findViewById(R.id.IteamName_editOrder);
		txtRate = (TextView)findViewById(R.id.price_editOrder);
		edtQty = (EditText)findViewById(R.id.qty_editorder);
		btnUpdate = (TextView)findViewById(R.id.Update_editorder);
		btnDelete = (TextView)findViewById(R.id.Delete_editorder);
		
		btnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DeleteOrder();
			}
		});
		
		btnUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UpdateOrder();
			}
		});
		
		txtMenu.setText(Menu);
		edtQty.setText(qty);
		txtRate.setText(rate);
	}
	
	public void UpdateOrder()
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
			request.addProperty("qty", qty);
			
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
	  				        		 Toast.makeText(EditOrderDetailsActivity.this, "Order updated sucessfully", Toast.LENGTH_LONG).show();
	  				        		 Intent intent=new Intent(EditOrderDetailsActivity.this, OrderDetailsActivity.class);
	  			     				 intent.putExtra("OrderId",OrderId);
	  			     				 startActivity(intent);	
	  								 finish();
								}
								else
								{										
									Toast.makeText(EditOrderDetailsActivity.this, "Order Not updated sucessfully", Toast.LENGTH_LONG).show();	
									finish();
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
	
	public void DeleteOrder()
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
	  				        		Toast.makeText(EditOrderDetailsActivity.this, "Order Deleted sucessfully", Toast.LENGTH_LONG).show();
	  				        		 Intent intent=new Intent(EditOrderDetailsActivity.this, OrderDetailsActivity.class);
	  			     				 intent.putExtra("OrderId",OrderId);
	  			     				 startActivity(intent);	
	  								 finish();
								}
								else
								{										
									Toast.makeText(EditOrderDetailsActivity.this, "Order Not Deleted sucessfully", Toast.LENGTH_LONG).show();
									finish();
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
