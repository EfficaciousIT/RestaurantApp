package com.mobi.efficacious.esmartresatarant.restaurentapp;

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
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.mobi.efficacious.esmartresatarant.R;
import com.mobi.efficacious.esmartresatarant.adaptors.KitchenOrderDetailsAdopter;
import com.mobi.efficacious.esmartresatarant.common.AlertDialogManager;
import com.mobi.efficacious.esmartresatarant.common.ConnectionDetector;
import com.mobi.efficacious.esmartresatarant.entity.KichenOrderDetails;
import com.mobi.efficacious.esmartresatarant.webservice.Constants;
import com.mobi.efficacious.esmartresatarant.webservice.Responce;

public class KichenOrderDetailsActivity extends Activity{
	String OrderId;
	ListView listView;
	SharedPreferences sharedpreferences;
	ConnectionDetector cd;
	AlertDialogManager alert;
	private static final String PREFRENCES_NAME = "myprefrences";
	SharedPreferences settings;
	private static  String SOAP_ACTION = "";	 
	private static  String OPERATION_NAME = "GetAboutADSXML";	 
	public static String strURL;
	KitchenOrderDetailsAdopter adapter;
	ArrayList<KichenOrderDetails> listArray = new ArrayList<KichenOrderDetails>();
	Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.kitchenorderdetails_layout);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title_order);
		mContext = KichenOrderDetailsActivity.this;
		listView = (ListView)findViewById(R.id.listView_kitchenorderdetail);
		OrderId = getIntent().getStringExtra("OrderId");
		alert = new AlertDialogManager();
		cd = new ConnectionDetector(getApplicationContext());
		PostData();
	}
	
	public void PostData()
	{ 
		if (!cd.isConnectingToInternet()) 
		 {
			alert.showAlertDialog(KichenOrderDetailsActivity.this,"No Internet Connection","Please connect to working Internet connection", false);
			return;
		 }
		 else
		 {
			OPERATION_NAME = "GetKichenOrderDetails";	
			SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
			final Responce responce=new Responce();
			SoapObject response = null;	
			String result = null;
			try
			{  
				SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);				
				request.addProperty("command", "select");
				request.addProperty("orderId", OrderId);
			
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
		  				        	KichenOrderDetails order =new KichenOrderDetails();
		  				        	if(res.contains("No record found"))
									{
										
									}
									else
									{	
										if(isValidProperty(str2, "Id"))
										{
											order.setId(str2.getProperty("Id").toString().trim());
										}
										
										if(isValidProperty(str2, "Order_Id"))
										{
											order.setOrder_Id(str2.getProperty("Order_Id").toString().trim());
										}
										
										if(isValidProperty(str2, "Menu_Name"))
										{
											order.setMenu_Name(str2.getProperty("Menu_Name").toString().trim());
										}
										
										if(isValidProperty(str2, "Qty"))
										{
											order.setQty(str2.getProperty("Qty").toString().trim());
										}
											
										listArray.add(order);

									}
		  				        	
		  				        }
		  				    
		  				      }
		            	 
		            	  adapter = new KitchenOrderDetailsAdopter(mContext, listArray);
		            	  listView.setAdapter(adapter);	            	 
			         } 
		        	else
		        	{
		        		Toast.makeText(KichenOrderDetailsActivity.this, "Null Response",Toast.LENGTH_LONG).show();
		        	}
		        }
		        else
		        {
		        	Toast.makeText(KichenOrderDetailsActivity.this, "Error",Toast.LENGTH_LONG).show();
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
