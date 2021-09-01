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
import android.text.TextUtils;
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
	Button RegisterBTN,signup_takeaway,RegButton_takeaway;
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
	String firstName,middleName,lastName,mobileNo,emailId,address1,address2,address3;
	int SearchResultStatus=0;
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
		RegButton_takeaway= (Button) myview.findViewById(R.id.RegButton_takeaway);
		signup_takeaway= (Button) myview.findViewById(R.id.signup_takeaway);
		RegisterBTN = (Button) myview.findViewById(R.id.RegisterButton_takeaway);
		FirstNameEditText.setVisibility(View.GONE);
		MiddleNameEditText.setVisibility(View.GONE);
		LastNameEditText.setVisibility(View.GONE);
		EmailEditText.setVisibility(View.GONE);
		Address1EditText.setVisibility(View.GONE);
		Address2EditText.setVisibility(View.GONE);
		Address3EditText.setVisibility(View.GONE);
		RegisterBTN.setVisibility(View.GONE);
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

		RegButton_takeaway.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FirstNameEditText.setVisibility(View.VISIBLE);
				EmailEditText.setVisibility(View.VISIBLE);
				Address1EditText.setVisibility(View.VISIBLE);
				Address3EditText.setVisibility(View.VISIBLE);
				MobileEditText.setVisibility(View.VISIBLE);

				RegButton_takeaway.setVisibility(View.GONE);
				signup_takeaway.setVisibility(View.GONE);
				RegisterBTN.setVisibility(View.VISIBLE);
			}
		});

		signup_takeaway.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mobileNo= MobileEditText.getText().toString();
					if (TextUtils.isEmpty(MobileEditText.getText().toString())) {
						MobileEditText.setError("Enter Valid Mobile No ");
				}else
				{
					SearchData searchData=new SearchData();
					searchData.execute();
				}
			}
		});

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

				if (TextUtils.isEmpty(FirstNameEditText.getText().toString())) {
					FirstNameEditText.setError("Enter Valid Name ");
				}
				else
				if (TextUtils.isEmpty(MobileEditText.getText().toString())) {
					MobileEditText.setError("Enter Valid Mobile No ");
				}else
				if (TextUtils.isEmpty(Address1EditText.getText().toString())) {
					Address1EditText.setError("Enter Valid Address ");
				}else
				{
					firstName= FirstNameEditText.getText().toString();
					middleName= MiddleNameEditText.getText().toString();
					lastName=LastNameEditText.getText().toString();
					mobileNo= MobileEditText.getText().toString();
					emailId=EmailEditText.getText().toString();
					address1=Address1EditText.getText().toString();
					address2=Address2EditText.getText().toString();
					address3=Address3EditText.getText().toString();
					PostData postData=new PostData();
					postData.execute();
				}

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
				request.addProperty("firstName", firstName);
				request.addProperty("middleName",  middleName);
				request.addProperty("lastName", lastName);
				request.addProperty("mobileNo",mobileNo);
				request.addProperty("emailId", emailId);
				request.addProperty("address1", address1);
				request.addProperty("address2",address2);
				request.addProperty("address3",address3);
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
	private class SearchData extends AsyncTask<Void, Void, Void> {
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
				if(SearchResultStatus==1)
				{
					FirstNameEditText.setText(firstName);
					MobileEditText.setText(mobileNo);
					EmailEditText.setText(emailId);
					Address1EditText.setText(address1);
					FirstNameEditText.setVisibility(View.VISIBLE);
					EmailEditText.setVisibility(View.VISIBLE);
					Address1EditText.setVisibility(View.VISIBLE);
					Address3EditText.setVisibility(View.VISIBLE);
					MobileEditText.setVisibility(View.VISIBLE);

					RegButton_takeaway.setVisibility(View.GONE);
					signup_takeaway.setVisibility(View.GONE);
					RegisterBTN.setVisibility(View.VISIBLE);
				}else
				{
					this.dialog.dismiss();
					Toast.makeText(getActivity(), "No Data Available",Toast.LENGTH_LONG).show();
				}

			}catch (Exception ex)
			{
				SearchResultStatus=0;
				this.dialog.dismiss();
				Toast.makeText(getActivity(), "No Data Available",Toast.LENGTH_LONG).show();
			}

			this.dialog.dismiss();
		}
		protected Void doInBackground(Void... params) {
			SearchResultStatus=0;
			OPERATION_NAME = "GetRegister";
			SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
			final Responce responce=new Responce();
			SoapObject response = null;
			String result = null;
			try
			{
				SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
				request.addProperty("command", "Search");
				request.addProperty("mobileNo",mobileNo);
				request.addProperty("resId", 1);
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
										SearchResultStatus=1;
									}
									if(isValidProperty(str2, "First_Name"))
									{
										firstName = str2.getProperty("First_Name").toString().trim();

									}
									if(isValidProperty(str2, "Email_Id"))
									{
										emailId = str2.getProperty("Email_Id").toString().trim();

									}
									if(isValidProperty(str2, "Address_1"))
									{
										address1 = str2.getProperty("Address_1").toString().trim();

									}
									if(isValidProperty(str2, "Mobile_No"))
									{
										mobileNo = str2.getProperty("Mobile_No").toString().trim();

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
				SearchResultStatus=0;
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