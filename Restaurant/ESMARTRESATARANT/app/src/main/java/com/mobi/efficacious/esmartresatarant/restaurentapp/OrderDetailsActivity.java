package com.mobi.efficacious.esmartresatarant.restaurentapp;

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
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.esmartresatarant.R;
import com.mobi.efficacious.esmartresatarant.adaptors.OrderAdapter;
import com.mobi.efficacious.esmartresatarant.common.AlertDialogManager;
import com.mobi.efficacious.esmartresatarant.common.ConnectionDetector;
import com.mobi.efficacious.esmartresatarant.entity.OrderDetail;
import com.mobi.efficacious.esmartresatarant.webservice.Constants;
import com.mobi.efficacious.esmartresatarant.webservice.Responce;


public class OrderDetailsActivity extends Activity{
	ListView listView;
	String RESTAURANTID;
	ArrayList<OrderDetail> order = new ArrayList<OrderDetail>();
	OrderAdapter adapter;
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
	String TableName;
	String OrderId;
	int TotalAmount;
	TextView TxtAmount;
	String rate;
	String qty;
	String EmployeeId;
	Button btnbill;
	Button btnkitchen;
	String result;
	TextView logout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.orderdetails_layout);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title_order);
		mContext=OrderDetailsActivity.this;
		TableName = getIntent().getStringExtra("TableName");
		OrderId = getIntent().getStringExtra("OrderId");
		settings = getSharedPreferences(PREFRENCES_NAME,Context.MODE_PRIVATE);
		RESTAURANTID = settings.getString("TAG_RESTAURANTID", "");
		EmployeeId = settings.getString("TAG_EMPLOYEEID", "");
		alert = new AlertDialogManager();
		cd = new ConnectionDetector(getApplicationContext());
		listView = (ListView)findViewById(R.id.listview_orderdetails);
		TxtAmount = (TextView)findViewById(R.id.ViewOrderTotal_orderdetails);
		btnbill = (Button)findViewById(R.id.btnSubmittobill_orderdetails);
		btnkitchen = (Button)findViewById(R.id.btnSubmittokitchen_orderdetails);
		logout=(TextView)findViewById(R.id.logout);
		btnbill.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PostBill();
			}
		});
		
		btnkitchen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PostKitchen();
			}
		});
		
		adapter = new OrderAdapter(mContext, order);
		listView.setAdapter(adapter);	
		PostData();
		logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try
				{
					SharedPreferences.Editor editor_delete = settings.edit();
					editor_delete.clear().commit();
					Intent intent = new Intent(OrderDetailsActivity.this, LoginActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					finish();
				}catch (Exception ex)
				{

				}
			}
		});
	}

	public void PostData()
	{ 
		if (!cd.isConnectingToInternet()) 
		 {
			alert.showAlertDialog(OrderDetailsActivity.this,"No Internet Connection","Please connect to working Internet connection", false);
			return;
		 }
		 else
		 {
			OPERATION_NAME = "GetOrder";	
			SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
			final Responce responce=new Responce();
			SoapObject response = null;	
			String result = null;
			try
			{  
				SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);				
				request.addProperty("command", "select");
				request.addProperty("TableName", TableName);
				request.addProperty("EmployeeId", EmployeeId);
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
		  				        	OrderDetail ord =new OrderDetail();
		  				        	if(res.contains("No record found"))
									{
										
									}
									else
									{	
										if(isValidProperty(str2, "Id"))
										{
											ord.setId(str2.getProperty("Id").toString().trim());
										}
										
										if(isValidProperty(str2, "Order_Id"))
										{
											ord.setOrder_Id(str2.getProperty("Order_Id").toString().trim());
										}
										
										if(isValidProperty(str2, "Cat_Name"))
										{
											ord.setCat_Name(str2.getProperty("Cat_Name").toString().trim());
										}
											
										if(isValidProperty(str2, "Menu_Name"))
										{
											ord.setMenu_Name(str2.getProperty("Menu_Name").toString().trim());
										}
										
										if(isValidProperty(str2, "Price"))
										{
											ord.setPrice(str2.getProperty("Price").toString().trim());
											rate = str2.getProperty("Price").toString().trim();
										}
										
										if(isValidProperty(str2, "Table_Name"))
										{
											ord.setTable_Name(str2.getProperty("Table_Name").toString().trim());
										}
										
										if(isValidProperty(str2, "Qty"))
										{
											ord.setQty(str2.getProperty("Qty").toString().trim());
											qty = str2.getProperty("Qty").toString().trim();
										}
										
										TotalAmount = (TotalAmount + Integer.parseInt(rate) * Integer.parseInt(qty));
										order.add(ord);
										
									}
		  				        }
		  				      }
		            	  TxtAmount.setText(String.valueOf(TotalAmount));
		            	  adapter = new OrderAdapter(mContext, order);
		            	  listView.setAdapter(adapter);		            	 
			         } 
		        	else
		        	{
		        		Toast.makeText(OrderDetailsActivity.this, "Null Response",Toast.LENGTH_LONG).show();
		        	}
		        }
		        else
		        {
		        	Toast.makeText(OrderDetailsActivity.this, "Error",Toast.LENGTH_LONG).show();
		        }			          
	        }	       
			catch (Exception e) 
			{		
				e.printStackTrace();
			}	
		 }
	}
	
	public void PostKitchen()
	{ 
		if (!cd.isConnectingToInternet()) 
		 {
			alert.showAlertDialog(OrderDetailsActivity.this,"No Internet Connection","Please connect to working Internet connection", false);
			return;
		 }
		 else
		 {
			OPERATION_NAME = "UpdateOrder";	
			SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;		
			final Responce responce=new Responce();
			SoapObject response = null;	
			String result = null;
			try
			{  
				SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);				
				request.addProperty("command", "update");
				request.addProperty("OrderId", OrderId);
				request.addProperty("Total", TotalAmount);
				request.addProperty("IsActive", 0);
				request.addProperty("status", "Kitchen");
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
		  				        	OrderDetail ord =new OrderDetail();
		  				        	if(res.contains("true"))
									{
										
									}
									else
									{	
										Toast.makeText(OrderDetailsActivity.this, "Order Placed successfully", Toast.LENGTH_LONG).show();
									}
		  				        }
		  				    }          	 
			         } 
		        	else
		        	{
		        		Toast.makeText(OrderDetailsActivity.this, "Null Response",Toast.LENGTH_LONG).show();
		        	}
		        }
		        else
		        {
		        	Toast.makeText(OrderDetailsActivity.this, "Error",Toast.LENGTH_LONG).show();
		        }			          
	        }	       
			catch (Exception e) 
			{		
				e.printStackTrace();
			}	
		 }
	}
	
	public void PostBill()
	{ 
		if (!cd.isConnectingToInternet()) 
		 {
			alert.showAlertDialog(OrderDetailsActivity.this,"No Internet Connection","Please connect to working Internet connection", false);
			return;
		 }
		 else
		 {
			OPERATION_NAME = "UpdateOrder";	
			SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;		
			final Responce responce=new Responce();
			SoapObject response = null;	
			String result = null;
			try
			{  
				SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);				
				request.addProperty("command", "update");
				request.addProperty("OrderId", OrderId);
				request.addProperty("Total", TotalAmount);
				request.addProperty("IsActive", 0);
				request.addProperty("status", "Bill");
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
		  				        	OrderDetail ord =new OrderDetail();
		  				        	if(res.contains("true"))
									{
										
									}
									else
									{	
										Toast.makeText(OrderDetailsActivity.this, "Order Placed successfully", Toast.LENGTH_LONG).show();
									}
		  				        }
		  				      }          	 
			         } 
		        	else
		        	{
		        		Toast.makeText(OrderDetailsActivity.this, "Null Response",Toast.LENGTH_LONG).show();
		        	}
		        }
		        else
		        {
		        	Toast.makeText(OrderDetailsActivity.this, "Error",Toast.LENGTH_LONG).show();
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

