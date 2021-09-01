package com.mobi.efficacious.restaurant;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.mobi.efficacious.adapter.CategoryAdopter;
import com.mobi.efficacious.common.AlertDialogManager;
import com.mobi.efficacious.common.ConnectionDetector;
import com.mobi.efficacious.model.Categories;
import com.mobi.efficacious.webservice.Constants;
import com.mobi.efficacious.webservice.Responce;

public class CategoryActivity extends Activity implements SearchView.OnQueryTextListener{
	GridView gridView;
	String RESTAURANTID;
	ArrayList<Categories> categories = new ArrayList<Categories>();
	CategoryAdopter adapter;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.category_layout);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.windows_title_category);
		mContext=CategoryActivity.this;
		
		OrderId = getIntent().getStringExtra("OrderId");
		
		settings = getSharedPreferences(PREFRENCES_NAME,Context.MODE_PRIVATE);
		RESTAURANTID = settings.getString("TAG_RESTAURANTID", "");
		alert = new AlertDialogManager();
		cd = new ConnectionDetector(getApplicationContext());
		gridView = (GridView)findViewById(R.id.gridView_categoty);
		searchView = (SearchView)findViewById(R.id.searchview_category);
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
	            gridView.clearTextFilter();
	        } 
			else 
			{
				gridView.setFilterText(newText.toString());
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
			alert.showAlertDialog(CategoryActivity.this,"No Internet Connection","Please connect to working Internet connection", false);
			return;
		 }
		 else
		 {
			OPERATION_NAME = "GetCategory";	
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
		  				        	Categories cate =new Categories();
		  				        	if(res.contains("No record found"))
									{
										
									}
									else
									{	
										if(isValidProperty(str2, "Cat_Id"))
										{
											cate.setCat_Id(str2.getProperty("Cat_Id").toString().trim());
										}
										
										if(isValidProperty(str2, "Cat_Name"))
										{
											cate.setCat_Name(str2.getProperty("Cat_Name").toString().trim());
										}
											
										categories.add(cate);
										list.add(str2.getProperty("Cat_Name").toString().trim());
									}
		  				        	
		  				        }
		  				    
		  				      }
		            	 
		            	  adapter = new CategoryAdopter(mContext, categories,OrderId);
		            	  gridView.setAdapter(adapter);
		          		  gridView.setTextFilterEnabled(true);
		          		  setupSearchView();		            	 
			         } 
		        	else
		        	{
		        		Toast.makeText(CategoryActivity.this, "Null Response",Toast.LENGTH_LONG).show();
		        	}
		        }
		        else
		        {
		        	Toast.makeText(CategoryActivity.this, "Error",Toast.LENGTH_LONG).show();
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

