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
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.esmartresatarant.R;
import com.mobi.efficacious.esmartresatarant.adaptors.MenuAdapter;
import com.mobi.efficacious.esmartresatarant.common.AlertDialogManager;
import com.mobi.efficacious.esmartresatarant.common.ConnectionDetector;
import com.mobi.efficacious.esmartresatarant.entity.Menus;
import com.mobi.efficacious.esmartresatarant.webservice.Constants;
import com.mobi.efficacious.esmartresatarant.webservice.Responce;


public class MenuActivity extends Activity implements SearchView.OnQueryTextListener{
	ListView listView;
	String RESTAURANTID;
	String cate_Id;
	String tableName;
	String orderId;
	
	ArrayList<Menus> menus = new ArrayList<Menus>();
	MenuAdapter adapter;
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
	TextView ViewOrder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_menu);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title_menu);
		mContext=MenuActivity.this;
		cate_Id = getIntent().getStringExtra("Cat_Id");
		tableName = getIntent().getStringExtra("TableName");
		orderId = getIntent().getStringExtra("OrderId");
		settings = getSharedPreferences(PREFRENCES_NAME,Context.MODE_PRIVATE);
		RESTAURANTID = settings.getString("TAG_RESTAURANTID", "");
		alert = new AlertDialogManager();
		cd = new ConnectionDetector(getApplicationContext());
		listView = (ListView)findViewById(R.id.ListView_menu);
		ViewOrder = (TextView)findViewById(R.id.ViewOrder_menu);
		
		searchView = (SearchView)findViewById(R.id.search_view_menu);

		ViewOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MenuActivity.this, OrderDetailsActivity.class);
				intent.putExtra("TableName", tableName ); 
				intent.putExtra("OrderId", orderId ); 
				startActivity(intent);
			}
		});
		
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
			alert.showAlertDialog(MenuActivity.this,"No Internet Connection","Please connect to working Internet connection", false);
			return;
		 }
		 else
		 {
			OPERATION_NAME = "GetMenu";	
			SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
			final Responce responce=new Responce();
			SoapObject response = null;	
			String result = null;
			try
			{  
				SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);				
				request.addProperty("command", "select");
				request.addProperty("res_id", 1 );
				request.addProperty("cat_id", cate_Id);
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
		  				        	Menus men =new Menus();
		  				        	if(res.contains("No record found"))
									{
										
									}
									else
									{										
										if(isValidProperty(str2, "Menu_Id"))
										{
											men.setMenu_Id(str2.getProperty("Menu_Id").toString().trim());
										}
										
										if(isValidProperty(str2, "Menu_Name"))
										{
											men.setMenu_Name(str2.getProperty("Menu_Name").toString().trim());
										}
										
										if(isValidProperty(str2, "Res_id"))
										{
											men.setRes_id(str2.getProperty("Res_id").toString().trim());
										}
										
										if(isValidProperty(str2, "Is_Active"))
										{
											men.setIs_Active(str2.getProperty("Is_Active").toString().trim());
										}
										
										if(isValidProperty(str2, "Cat_Id"))
										{
											men.setCat_Id(str2.getProperty("Cat_Id").toString().trim());
										}
										
										if(isValidProperty(str2, "Price"))
										{
											men.setPrice(str2.getProperty("Price").toString().trim());
										}
										menus.add(men);
										
									}
		  				        	
		  				        }
		  				    
		  				      }
		            	 
		            	  adapter = new MenuAdapter(mContext, menus, tableName, cate_Id, orderId );
		            	  listView.setAdapter(adapter);	
		            	  listView.setTextFilterEnabled(true);
		            	  setupSearchView();
			         } 
		        	else
		        	{
		        		Toast.makeText(MenuActivity.this, "Null Response",Toast.LENGTH_LONG).show();
		        	}
		        }
		        else
		        {
		        	Toast.makeText(MenuActivity.this, "Error",Toast.LENGTH_LONG).show();
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
