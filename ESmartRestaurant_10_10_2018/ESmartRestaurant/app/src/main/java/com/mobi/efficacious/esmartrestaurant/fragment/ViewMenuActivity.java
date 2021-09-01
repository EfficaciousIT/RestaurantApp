package com.mobi.efficacious.esmartrestaurant.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.activity.MainActivity;
import com.mobi.efficacious.esmartrestaurant.adapter.ViewMenuAdapter;
import com.mobi.efficacious.esmartrestaurant.common.ConnectionDetector;
import com.mobi.efficacious.esmartrestaurant.entity.Menus;
import com.mobi.efficacious.esmartrestaurant.webservice.Constants;
import com.mobi.efficacious.esmartrestaurant.webservice.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import static com.mobi.efficacious.esmartrestaurant.activity.MainActivity.fragmentManager;

public class ViewMenuActivity extends Fragment implements SearchView.OnQueryTextListener{
	ListView listView;
	String RESTAURANTID;
	String Cate_Id;
	String tableName;
	String orderId;
	ArrayList<Menus> menus = new ArrayList<Menus>();
	ViewMenuAdapter adapter;
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
	View myview;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		myview=inflater.inflate(R.layout.viewmenu_layout,null);
		mContext=getActivity();
		try
		{
			((MainActivity) getActivity()).getSupportActionBar().setTitle("Menu");
		}catch (Exception ex)
		{

		}

		myview.setFocusableInTouchMode(true);
		myview.requestFocus();
		myview.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						try {
							ViewCategoryActivity viewCategoryActivity = new ViewCategoryActivity();
							fragmentManager.beginTransaction().replace(R.id.content_main, viewCategoryActivity).commit();
						} catch (Exception ex) {

						}

						return true;
					}
				}
				return false;
			}
		});
		Cate_Id = getArguments().getString("Cat_Id");
		settings = getActivity().getSharedPreferences(PREFRENCES_NAME,Context.MODE_PRIVATE);
		RESTAURANTID = settings.getString("TAG_RESTAURANTID", "");

		cd = new ConnectionDetector(getActivity());
		listView = (ListView)myview.findViewById(R.id.ListView_viewmenu);
		searchView = (SearchView)myview.findViewById(R.id.search_viewmenu);
		PostData postData=new PostData();
		postData.execute();
		return myview;
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
		private final ProgressDialog dialog = new ProgressDialog(getActivity());


		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("Loading...");
			dialog.show();

		}


		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			adapter = new ViewMenuAdapter(mContext, menus);//, tableName, Cate_Id, orderId );
			listView.setAdapter(adapter);
			listView.setTextFilterEnabled(true);
			setupSearchView();
			this.dialog.dismiss();
		}
		protected Void doInBackground(Void... params) {
			OPERATION_NAME = "GetMenu";
			SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
			final Responce responce=new Responce();
			SoapObject response = null;
			String result = null;
			try
			{
				SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
				request.addProperty("command", "select");
				request.addProperty("res_id", RESTAURANTID);
				request.addProperty("cat_id", Cate_Id);
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


					}
					else
					{
						Toast.makeText(getActivity(), "Null Response",Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					Toast.makeText(getActivity(), "Error",Toast.LENGTH_LONG).show();
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
