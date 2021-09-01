package com.mobi.efficacious.restaurant;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.mobi.efficacious.adapter.CategoryAdopter;
import com.mobi.efficacious.adapter.CustomerWiseOrderDetailAdopter;
import com.mobi.efficacious.common.AlertDialogManager;
import com.mobi.efficacious.common.ConnectionDetector;
import com.mobi.efficacious.model.Categories;
import com.mobi.efficacious.model.CustomerWiseOrderDetail;
import com.mobi.efficacious.webservice.Constants;
import com.mobi.efficacious.webservice.Responce;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class TrasactionDetailActivity extends Activity{
	ListView listView;
	String RESTAURANTID;
	ArrayList<CustomerWiseOrderDetail> orderDetail = new ArrayList<CustomerWiseOrderDetail>();
	CustomerWiseOrderDetailAdopter adapter;
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
	List<String> list = new ArrayList<String>();
	SearchView searchView;
	String TableName;
	String OrderId;
	TextView TotalAmount;
	int Amount;
	String Qty;
	String Rate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.trasactiondetails_layout);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title_order);
		mContext=TrasactionDetailActivity.this;
		
		OrderId = getIntent().getStringExtra("OrderId");

		alert = new AlertDialogManager();
		cd = new ConnectionDetector(getApplicationContext());
		listView = (ListView)findViewById(R.id.listView_trasactiondetails);
		TotalAmount = (TextView)findViewById(R.id.Total_trasactiondetails);
		PostData();
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
			OPERATION_NAME = "CustomerWiseOrderDetail";	
			SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;		
			final Responce responce=new Responce();
			SoapObject response = null;	
			String result = null;
			try
			{  
				SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);				
				request.addProperty("command", "select");
				request.addProperty("OrderId", OrderId );
			
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
		  				        	CustomerWiseOrderDetail order =new CustomerWiseOrderDetail();
		  				        	if(res.contains("No record found"))
									{
										
									}
									else
									{	
										if(isValidProperty(str2, "Order_Id"))
										{
											order.setOrder_Id(str2.getProperty("Order_Id").toString().trim());
										}
										
										if(isValidProperty(str2, "Cat_Name"))
										{
											order.setCat_Name(str2.getProperty("Cat_Name").toString().trim());
										}
											
										if(isValidProperty(str2, "Menu_Name"))
										{
											order.setMenu_Name(str2.getProperty("Menu_Name").toString().trim());
										}
										
										if(isValidProperty(str2, "Price"))
										{
											order.setPrice(str2.getProperty("Price").toString().trim());
											Rate = str2.getProperty("Price").toString().trim();
										}
										
										if(isValidProperty(str2, "Qty"))
										{
											order.setQty(str2.getProperty("Qty").toString().trim());
											Qty = str2.getProperty("Qty").toString().trim();
										}
										Amount = (Amount + Integer.parseInt(Rate) * Integer.parseInt(Qty));
										orderDetail.add(order);
									}
		  				        	
		  				        }
		  				    
		  				      }
		            	  TotalAmount.setText(String.valueOf(Amount));
		            	  adapter = new CustomerWiseOrderDetailAdopter(mContext, orderDetail);
		            	  listView.setAdapter(adapter);		            	 
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

