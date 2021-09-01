package com.mobi.efficacious.esmartrestaurant.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.activity.MainActivity;
import com.mobi.efficacious.esmartrestaurant.common.ConnectionDetector;
import com.mobi.efficacious.esmartrestaurant.entity.Register;
import com.mobi.efficacious.esmartrestaurant.webservice.Constants;
import com.mobi.efficacious.esmartrestaurant.webservice.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static android.app.Activity.RESULT_OK;


public class TakeAwayActivity extends Fragment implements OnClickListener {
	String IMEIKEY;
	String emailPattern;
	EditText FirstNameEditText;
	EditText MiddleNameEditText;
	EditText LastNameEditText;
	EditText EmailEditText;
	EditText MobileEditText;
	EditText Address1EditText;
	EditText Address2EditText;
	EditText Address3EditText;
	Button RegisterBTN;
	private static String SOAP_ACTION = "";
	private static String OPERATION_NAME = "GetAboutADSXML";
	public static String strURL;
	TelephonyManager tel;
	ConnectionDetector cd;
	private static final String PREFRENCES_NAME = "myprefrences";
	SharedPreferences settings;
	Context mContext;
	String RESTAURANTID;
	String OrderId;
	String TableName = "0";
	String RegisterId;
	String EmployeeId;
	private static int RESULT_LOAD_IMAGE = 1;
	TelephonyManager telephonyManager;
	View myview;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		myview=inflater.inflate(R.layout.takeaway_layout,null);
		emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
		cd = new ConnectionDetector(getActivity());
		settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
		RESTAURANTID = settings.getString("TAG_RESTAURANTID", "");
		EmployeeId = settings.getString("TAG_EMPLOYEEID", "");
		FirstNameEditText = (EditText) myview.findViewById(R.id.FirstName_edittext_takeaway);
		MiddleNameEditText = (EditText) myview.findViewById(R.id.MiddleName_edittext_takeaway);
		LastNameEditText = (EditText) myview.findViewById(R.id.LastName_edittext_takeaway);
		EmailEditText = (EditText) myview.findViewById(R.id.emailId_edittext_takeaway);
		MobileEditText = (EditText) myview.findViewById(R.id.mobile_edittext_takeaway);
		Address1EditText = (EditText) myview.findViewById(R.id.address1_edittext_takeaway);
		Address2EditText = (EditText) myview.findViewById(R.id.address2_edittext_takeaway);
		Address3EditText = (EditText) myview.findViewById(R.id.address3_edittext_takeaway);
		try {
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE}, 1);
			telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
			if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return null;
			}
			IMEIKEY = telephonyManager.getDeviceId();
			if (IMEIKEY == null && IMEIKEY.isEmpty()) {
				IMEIKEY = "0";
			} else {
				IMEIKEY = IMEIKEY;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		RegisterBTN = (Button) myview.findViewById(R.id.RegisterButton_takeaway);
		RegisterBTN.setOnClickListener(this);
		return myview;
	}


	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null)
			if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			IMEIKEY = telephonyManager.getDeviceId();

	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
			case R.id.RegisterButton_takeaway:
				PostData postData=new PostData();
				postData.execute();
			break;
		}
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
				InsertOrder insertOrder=new InsertOrder();
				insertOrder.execute();
			}catch (Exception ex)
			{

			}

			this.dialog.dismiss();
		}
		protected Void doInBackground(Void... params) {
			OPERATION_NAME = "GetRegister";
			SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
			final Responce responce=new Responce();
			SoapObject response = null;
			String result = null;
			try
			{
				SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
				request.addProperty("command", "insert");
				request.addProperty("firstName", FirstNameEditText.getText().toString());
				request.addProperty("middleName",  MiddleNameEditText.getText().toString());
				request.addProperty("lastName", LastNameEditText.getText().toString());
				request.addProperty("mobileNo", MobileEditText.getText().toString());
				request.addProperty("emailId", EmailEditText.getText().toString());
				request.addProperty("address1", Address1EditText.getText().toString());
				request.addProperty("address2", Address2EditText.getText().toString());
				request.addProperty("address3", Address3EditText.getText().toString());
				request.addProperty("resId", 1);
				request.addProperty("imeiNo", IMEIKEY);
				request.addProperty("gcm", "");

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
								Register cate =new Register();
								if(res.contains("No record found"))
								{

								}
								else
								{
									if(isValidProperty(str2, "Register_Id"))
									{
										cate.setRegisterId(str2.getProperty("Register_Id").toString().trim());
										RegisterId = str2.getProperty("Register_Id").toString().trim();
										settings.edit().putString("TAG_REGISTERID", RegisterId).commit();

									}

								}

							}

						}
					}
					else
					{
						Toast.makeText(getActivity(), "No Response",Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					Toast.makeText(getActivity(), "No Response",Toast.LENGTH_LONG).show();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}
	}
	private class InsertOrder extends AsyncTask<Void, Void, Void> {
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
				CategoryActivity menuActivity = new CategoryActivity();
				Bundle args = new Bundle();
				args.putString("TableName",TableName);
				args.putString("OrderId",OrderId);
				args.putString("pagename","NewOrder");
				menuActivity.setArguments(args);
				MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, menuActivity).commitAllowingStateLoss();

			}catch (Exception ex)
			{

			}
			this.dialog.dismiss();
		}
		protected Void doInBackground(Void... params) {
			OPERATION_NAME = "InsertOrder";
			SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
			final Responce responce=new Responce();
			SoapObject response = null;
			String result = null;
			try
			{
				SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
				request.addProperty("command", "insert");
				request.addProperty("TableName", "TakeAway");
				request.addProperty("RegisterId", RegisterId);
				request.addProperty("EmployeeId", EmployeeId);
				request.addProperty("DeviceId", IMEIKEY);
				request.addProperty("IsActive", 1);
				request.addProperty("status", "Open");
				request.addProperty("resId", RESTAURANTID);
				request.addProperty("intPersonCOunt", "0");
				request.addProperty("vchSplit_status","No");
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

								if(res.contains("No record found"))
								{

								}
								else
								{
									if(isValidProperty(str2, "OrderId"))
									{
										OrderId= str2.getProperty("OrderId").toString().trim();


									}
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