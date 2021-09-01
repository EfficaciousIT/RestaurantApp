package com.mobi.efficacious.esmartrestaurant.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;


import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.adapter.ExistingTableAdpter;
import com.mobi.efficacious.esmartrestaurant.adapter.KitchenOrderAdapter;
import com.mobi.efficacious.esmartrestaurant.common.ConnectionDetector;
import com.mobi.efficacious.esmartrestaurant.entity.KichenOrder;
import com.mobi.efficacious.esmartrestaurant.webservice.Constants;
import com.mobi.efficacious.esmartrestaurant.webservice.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class KitchenActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
	ListView listView;
	String RESTAURANTID;
	ArrayList<KichenOrder> KitchenOrder = new ArrayList<KichenOrder>();
	KitchenOrderAdapter adapter;
	SharedPreferences sharedpreferences;
	ConnectionDetector cd;
	RelativeLayout relative;
	private static final String PREFRENCES_NAME = "myprefrences";
	SharedPreferences settings;
	private static  String SOAP_ACTION = "";	 
	private static  String OPERATION_NAME = "GetAboutADSXML";	 
	public static String strURL;
	Context mContext;
	SearchView searchView;
	FrameLayout content_main;
	public static FragmentManager KitchenfragmentManager;
	@Override
	public void  onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kitchen_layout);
		mContext=KitchenActivity.this;
		KitchenfragmentManager=getSupportFragmentManager();
		content_main=(FrameLayout)findViewById(R.id.content_main);
		settings = getSharedPreferences(PREFRENCES_NAME,Context.MODE_PRIVATE);
		RESTAURANTID = settings.getString("TAG_RESTAURANTID", "");
		cd = new ConnectionDetector(getApplicationContext());
		listView = (ListView)findViewById(R.id.listView_kitchen);
		searchView = (SearchView)findViewById(R.id.searchview_kitchen);
		PostData postData=new PostData();
		postData.execute();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.logout, menu);
		return true;
	}


    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			try
			{
				DialogInterface.OnClickListener dialogClickListenerr = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
							case DialogInterface.BUTTON_POSITIVE:
								SharedPreferences.Editor editor_delete = settings.edit();
								editor_delete.clear().commit();
								Intent intent = new Intent(KitchenActivity.this, LoginActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
								finish();
								break;
							case DialogInterface.BUTTON_NEGATIVE:

								break;
						}
					}
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(KitchenActivity.this);
				builder.setMessage("Are you sure want to LogOut?").setPositiveButton("Yes", dialogClickListenerr)
						.setNegativeButton("No", dialogClickListenerr).show();

			}catch (Exception ex)
			{

			}
			return true;
		}

		return super.onOptionsItemSelected(item);
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

	private class PostData extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(KitchenActivity.this);


		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("Loading...");
			dialog.show();

		}


		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			try
			{
				adapter = new KitchenOrderAdapter(mContext, KitchenOrder);
				listView.setAdapter(adapter);
				listView.setTextFilterEnabled(true);
				setupSearchView();
			}catch (Exception ex)
			{

			}
			this.dialog.dismiss();
		}
		protected Void doInBackground(Void... params) {

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
			return null;
		}
	}

	
	boolean isValidProperty(SoapObject soapObject, String PropertyName)
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

