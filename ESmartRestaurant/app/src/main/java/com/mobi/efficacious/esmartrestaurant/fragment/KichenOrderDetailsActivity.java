package com.mobi.efficacious.esmartrestaurant.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.activity.KitchenActivity;
import com.mobi.efficacious.esmartrestaurant.activity.Splash;
import com.mobi.efficacious.esmartrestaurant.adapter.KitchenOrderDetailsAdopter;
import com.mobi.efficacious.esmartrestaurant.common.ConnectionDetector;
import com.mobi.efficacious.esmartrestaurant.entity.KichenOrderDetails;
import com.mobi.efficacious.esmartrestaurant.entity.OrderDetail;
import com.mobi.efficacious.esmartrestaurant.webservice.Constants;
import com.mobi.efficacious.esmartrestaurant.webservice.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class KichenOrderDetailsActivity extends Fragment{
	String OrderId;
	ListView listView;
	SharedPreferences sharedpreferences;
	ConnectionDetector cd;
	private static final String PREFRENCES_NAME = "myprefrences";
	SharedPreferences settings;
	private static  String SOAP_ACTION = "";	 
	private static  String OPERATION_NAME = "GetAboutADSXML";	 
	public static String strURL;
	KitchenOrderDetailsAdopter adapter;
	ArrayList<KichenOrderDetails> listArray = new ArrayList<KichenOrderDetails>();
	Context mContext;
View myview;
Button btnOrderCompleted;
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		myview = inflater.inflate(R.layout.kitchenorderdetails_layout, null);
		mContext = getActivity();
		listView = (ListView)myview.findViewById(R.id.listView_kitchenorderdetail);
		OrderId = getArguments().getString("OrderId");
		btnOrderCompleted=(Button)myview.findViewById(R.id.btnSubmittobill_billorderdetails);
		cd = new ConnectionDetector(getActivity());

		myview.setFocusableInTouchMode(true);
		myview.requestFocus();
		myview.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						try {

							Intent MainScreenIntent=new Intent(getActivity(), KitchenActivity.class);
							startActivity(MainScreenIntent);
							getActivity().finish();
						} catch (Exception ex) {

						}

						return true;
					}
				}
				return false;
			}
		});
		btnOrderCompleted.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PostKitchen postKitchen=new PostKitchen();
				postKitchen.execute();
			}
		});
		PostData postData=new PostData();
		postData.execute();

		return  myview;
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
			try
			{
				adapter = new KitchenOrderDetailsAdopter(mContext, listArray);
				listView.setAdapter(adapter);
			}catch (Exception ex)
			{

			}

			this.dialog.dismiss();
		}
		protected Void doInBackground(Void... params) {
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

									if(isValidProperty(str2, "Food_Code"))
									{
										order.setFoodCode(str2.getProperty("Food_Code").toString().trim());
									}
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
									if(isValidProperty(str2, "vchFoodDescription"))
									{
										order.setVchFoodDescription(str2.getProperty("vchFoodDescription").toString().trim());
									}

									listArray.add(order);

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

	private class PostKitchen extends AsyncTask<Void, Void, Void> {
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
			this.dialog.dismiss();

			Toast.makeText(getActivity(), "Order Dispatch successfully", Toast.LENGTH_LONG).show();
			Intent MainScreenIntent=new Intent(getActivity(), KitchenActivity.class);
			startActivity(MainScreenIntent);
			getActivity().finish();

		}
		protected Void doInBackground(Void... params) {
			OPERATION_NAME = "UpdateOrder";
			SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
			final Responce responce=new Responce();
			SoapObject response = null;
			String result = null;
			try
			{
				SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
				request.addProperty("command", "updateDispatch");
				request.addProperty("OrderId", OrderId);
				request.addProperty("IsActive", 0);
				request.addProperty("status", "Dispached");
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
									//OrderStatus=0;
								}
								else
								{
									///OrderStatus=1;
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
