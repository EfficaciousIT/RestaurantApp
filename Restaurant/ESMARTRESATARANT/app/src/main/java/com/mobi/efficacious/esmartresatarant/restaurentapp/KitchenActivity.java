package com.mobi.efficacious.esmartresatarant.restaurentapp;

import java.util.ArrayList;
import java.util.List;

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
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.mobi.efficacious.esmartresatarant.R;
import com.mobi.efficacious.esmartresatarant.adaptors.KitchenOrderAdapter;
import com.mobi.efficacious.esmartresatarant.common.AlertDialogManager;
import com.mobi.efficacious.esmartresatarant.common.ConnectionDetector;
import com.mobi.efficacious.esmartresatarant.entity.KichenOrder;
import com.mobi.efficacious.esmartresatarant.webservice.Constants;
import com.mobi.efficacious.esmartresatarant.webservice.Responce;

public class KitchenActivity extends Activity implements SearchView.OnQueryTextListener{
	ListView listView;
	String RESTAURANTID;
	ArrayList<KichenOrder> KitchenOrder = new ArrayList<KichenOrder>();
	KitchenOrderAdapter adapter;
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
	SearchView searchView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kitchen_layout);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title_order);
		mContext=KitchenActivity.this;
		settings = getSharedPreferences(PREFRENCES_NAME,Context.MODE_PRIVATE);
		RESTAURANTID = settings.getString("TAG_RESTAURANTID", "");
		alert = new AlertDialogManager();
		cd = new ConnectionDetector(getApplicationContext());
		listView = (ListView)findViewById(R.id.listView_kitchen);
		searchView = (SearchView)findViewById(R.id.searchview_kitchen);
		PostData();
	}
	
	  private void setupSearchView() 
	  {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true); 
        searchView.setQueryHint("Search Here");
	  }

	    public boolean onQueryTextChange(String newText) 
	    {
	        if (TextUtils.isEmpty(newText)) 
	        {
	            listView.clearTextFilter();
	        } 
			else 
			{
				listView.setFilterText(newText.toString());
	        }
	        return true;
	    }

	    public boolean onQueryTextSubmit(String query) {
	        return false;
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
			OPERATION_NAME = "GetKichenOrderList";	
			SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
			final Responce responce=new Responce();
			SoapObject response = null;	
			String result = null;
			try
			{  
				SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);				
				request.addProperty("command", "select");
				request.addProperty("resId", RESTAURANTID);
			
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
		  				        	KichenOrder kitchen =new KichenOrder();
		  				        	if(res.contains("No record found"))
									{
										
									}
									else
									{	
										if(isValidProperty(str2, "Order_Id"))
										{
											kitchen.setOrder_Id(str2.getProperty("Order_Id").toString().trim());
										}
										
										if(isValidProperty(str2, "Table_Name"))
										{
											kitchen.setTable_Name(str2.getProperty("Table_Name").toString().trim());
										}
											
										KitchenOrder.add(kitchen);
									}
		  				        	
		  				        }
		  				    
		  				      }
		            	 
		            	  adapter = new KitchenOrderAdapter(mContext, KitchenOrder);
		            	  listView.setAdapter(adapter);
		            	  listView.setTextFilterEnabled(true);
		          		  setupSearchView();		            	 
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

