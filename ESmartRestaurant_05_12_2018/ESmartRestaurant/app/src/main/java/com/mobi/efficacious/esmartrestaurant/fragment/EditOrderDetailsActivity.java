package com.mobi.efficacious.esmartrestaurant.fragment;

import android.app.Activity;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.activity.MainActivity;
import com.mobi.efficacious.esmartrestaurant.common.ConnectionDetector;
import com.mobi.efficacious.esmartrestaurant.tab.ExistionOrder_Tab;
import com.mobi.efficacious.esmartrestaurant.webservice.Constants;
import com.mobi.efficacious.esmartrestaurant.webservice.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class EditOrderDetailsActivity extends Fragment{
	String rate;
	String qty;
	String Id;
	String Menu;
	String TableName;
	String OrderId;
	TextView txtMenu;
	TextView txtRate,edtQty;
	EditText description_editOrder;
	Button txtDelete;
	Button txtUpdate;
	ConnectionDetector cd;
	RelativeLayout relative;
	private static final String PREFRENCES_NAME = "myprefrences";
	SharedPreferences settings;
	private static  String SOAP_ACTION = "";	 
	private static  String OPERATION_NAME = "GetAboutADSXML";	 
	public static String strURL;
	Context mContext;
	View myview;
	ImageButton DecrementOreder,IncrementOreder;
String pagename,FoodDescription="";
	int Total,Orderqty,qty1;
	String OrderVAlue;
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		myview=inflater.inflate(R.layout.editorderdetails_layout,null);
		mContext = getActivity();
		cd = new ConnectionDetector( getActivity());
		pagename=getArguments().getString("pagename");
		Id = getArguments().getString("Id");
		Menu = getArguments().getString("MenuName");
		qty = getArguments().getString("Qty");
		rate = getArguments().getString("Price");
		TableName = getArguments().getString("TableName");
		OrderId = getArguments().getString("OrderId");

		description_editOrder = (EditText) myview.findViewById(R.id.description_editOrder);
		txtMenu = (TextView)myview.findViewById(R.id.IteamName_editOrder);
		txtRate = (TextView)myview.findViewById(R.id.price_editOrder);
		edtQty = (TextView)myview.findViewById(R.id.qty_editorder);
		txtUpdate = (Button)myview.findViewById(R.id.Update_editorder);
		txtDelete = (Button)myview.findViewById(R.id.Delete_editorder);
		 DecrementOreder = (ImageButton) myview.findViewById(R.id.decrement);
		 IncrementOreder = (ImageButton)myview.findViewById(R.id.increment);
		myview.setFocusableInTouchMode(true);
		myview.requestFocus();
		myview.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						try {
							if(pagename.contentEquals("ViewOrder"))
							{
								OrderDetailsActivity orderDetailsActivity = new OrderDetailsActivity();
								Bundle args = new Bundle();
								args.putString("TableName",TableName);
								args.putString("OrderId",OrderId);
								orderDetailsActivity.setArguments(args);
								MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, orderDetailsActivity).commitAllowingStateLoss();


							}else
							{
								ExistionOrder_Tab existingOrderHomeActivity = new ExistionOrder_Tab();
								Bundle args = new Bundle();
								args.putString("TableName",TableName);
								args.putString("OrderId",OrderId);
								existingOrderHomeActivity.setArguments(args);
								MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, existingOrderHomeActivity).commitAllowingStateLoss();

							}

						} catch (Exception ex) {

						}

						return true;
					}
				}
				return false;
			}
		});

		txtUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PostUpdate postUpdate=new PostUpdate();
				postUpdate.execute();
			}
		});

		txtDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PostDelete postDelete=new PostDelete();
				postDelete.execute();
			}
		});
		IncrementOreder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				OrderVAlue= edtQty.getText().toString();
				Orderqty= Integer.parseInt(OrderVAlue);
				if(Orderqty>=0)
				{
					Orderqty++;
					edtQty.setText(String.valueOf(Orderqty));
				}else
				{
					edtQty.setText("1");
				}
			}
		});
		DecrementOreder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				OrderVAlue= edtQty.getText().toString();
				Orderqty= Integer.parseInt(OrderVAlue);
				if(Orderqty>=0)
				{
					Orderqty--;
					edtQty.setText(String.valueOf(Orderqty));
				}else
				{
					edtQty.setText("0");
				}
			}
		});
		FoodDescription= getArguments().getString("FoodDescription");
		description_editOrder.setText(FoodDescription);
		txtMenu.setText(Menu);
		edtQty.setText(qty);
		txtRate.setText(rate);
		return myview;
	}
	private class PostUpdate extends AsyncTask<Void, Void, Void> {
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
			ExistionOrder_Tab existingOrderHomeActivity = new ExistionOrder_Tab();
			Bundle args = new Bundle();
			args.putString("TableName",TableName);
			args.putString("OrderId",OrderId);
			existingOrderHomeActivity.setArguments(args);
			MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, existingOrderHomeActivity).commitAllowingStateLoss();

			this.dialog.dismiss();
		}
		protected Void doInBackground(Void... params) {
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
				request.addProperty("qty", edtQty.getText().toString());
				request.addProperty("vchFoodDescription",description_editOrder.getText().toString() );
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
									Toast.makeText(getActivity(), "Updated Sucessfully", Toast.LENGTH_LONG).show();
									OrderDetailsActivity orderDetailsActivity = new OrderDetailsActivity();
									Bundle args = new Bundle();
									args.putString("TableName",TableName);
									args.putString("OrderId",OrderId);
									orderDetailsActivity.setArguments(args);
									MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, orderDetailsActivity).commitAllowingStateLoss();
								}
								else
								{
									Toast.makeText(getActivity(), "Not Updated", Toast.LENGTH_LONG).show();
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

	private class PostDelete extends AsyncTask<Void, Void, Void> {
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
			ExistionOrder_Tab existingOrderHomeActivity = new ExistionOrder_Tab();
			Bundle args = new Bundle();
			args.putString("TableName",TableName);
			args.putString("OrderId",OrderId);
			existingOrderHomeActivity.setArguments(args);
			MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, existingOrderHomeActivity).commitAllowingStateLoss();

			this.dialog.dismiss();
		}
		protected Void doInBackground(Void... params) {
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
									Toast.makeText(getActivity(), "Deleted Sucessfully", Toast.LENGTH_LONG).show();
									OrderDetailsActivity orderDetailsActivity = new OrderDetailsActivity();
									Bundle args = new Bundle();
									args.putString("TableName",TableName);
									args.putString("OrderId",OrderId);
									orderDetailsActivity.setArguments(args);
									MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, orderDetailsActivity).commitAllowingStateLoss();

								}
								else
								{
									Toast.makeText(getActivity(), "Not Deleted", Toast.LENGTH_LONG).show();
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

	

	
	 public interface EditAgeDialogListener {
	        void EditAgeDialogResult(String inputText);
	 }
}
