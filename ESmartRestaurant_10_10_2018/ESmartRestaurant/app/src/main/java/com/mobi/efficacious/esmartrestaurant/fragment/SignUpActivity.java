package com.mobi.efficacious.esmartrestaurant.fragment;

import android.Manifest;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.activity.LoginActivity;
import com.mobi.efficacious.esmartrestaurant.common.ConnectionDetector;

import com.mobi.efficacious.esmartrestaurant.entity.Employee;
import com.mobi.efficacious.esmartrestaurant.webservice.Constants;
import com.mobi.efficacious.esmartrestaurant.webservice.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class SignUpActivity extends Fragment implements OnClickListener {
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
	RadioGroup GenderRadioGroup;
	RadioButton MaleRadioButton;
	RadioButton FemaleRadioButton;
	Button Register;
	private static String SOAP_ACTION = "";
	private static String OPERATION_NAME = "GetAboutADSXML";
	public static String strURL;
	TelephonyManager tel;
	ConnectionDetector cd;
	private static final String PREFRENCES_NAME = "myprefrences";
	SharedPreferences settings;
	Context mContext;
	String EmployeeId;
	View myview;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.activity_signup, null);
        myview.setFocusableInTouchMode(true);
        myview.requestFocus();
        myview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        try {
                            Intent intent=new Intent(getActivity(),LoginActivity.class);
                            startActivity(intent);
                        } catch (Exception ex) {

                        }

                        return true;
                    }
                }
                return false;
            }
        });
		emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
		cd = new ConnectionDetector(getActivity());
		FirstNameEditText = (EditText) myview.findViewById(R.id.FirstName_edittext_signup);
		MiddleNameEditText = (EditText) myview.findViewById(R.id.MiddleName_edittext_signup);
		LastNameEditText = (EditText) myview.findViewById(R.id.LastName_edittext_signup);
		EmailEditText = (EditText) myview.findViewById(R.id.emailId_edittext_signup);
		MobileEditText = (EditText) myview.findViewById(R.id.mobile_edittext_signup);
		Address2EditText = (EditText) myview.findViewById(R.id.address2_edittext_signup);
		Address3EditText = (EditText) myview.findViewById(R.id.address3_edittext_signup);
		Register = (Button) myview.findViewById(R.id.RegisterButton_signup);
		Register.setOnClickListener(this);
        try {
            tel = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
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
            IMEIKEY = tel.getDeviceId().toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
		return myview;
	}
    private class SignUpAsync extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        String Gender;

        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Signup...");
            dialog.show();

        }


        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid); this.dialog.dismiss();
        }

        protected Void doInBackground(Void... params) {

            try {

                int selectedId = GenderRadioGroup.getCheckedRadioButtonId();
                if (selectedId == MaleRadioButton.getId()) {
                    Gender = "Male";
                } else {
                    Gender = "Female";
                }
            } catch (Exception ex) {

            }
            OPERATION_NAME = "GetEmployee";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("command", "insert");
                request.addProperty("firstName", FirstNameEditText.getText().toString());
                request.addProperty("middleName", MiddleNameEditText.getText().toString());
                request.addProperty("lastName", LastNameEditText.getText().toString());
                request.addProperty("mobileNo", MobileEditText.getText().toString());
                request.addProperty("emailId", EmailEditText.getText().toString());
                request.addProperty("address1", Address1EditText.getText().toString());
                request.addProperty("address2", Address2EditText.getText().toString());
                request.addProperty("address3", Address3EditText.getText().toString());
                request.addProperty("gender", Gender);
                request.addProperty("resId", "1");
                request.addProperty("imeiNo", IMEIKEY);
                request.addProperty("gcm", "");

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE ht = new HttpTransportSE(Constants.strWEB_SERVICE_URL);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                ht.debug = true;
                ht.call(SOAP_ACTION, envelope);
                if (!(envelope.bodyIn instanceof SoapFault)) {
                    if (envelope.bodyIn instanceof SoapObject)
                        response = (SoapObject) envelope.bodyIn;
                    if (response != null) {
                        result = response.getProperty(0).toString();
                        if (response != null) {
                            SoapObject str = null;
                            for (int i = 0; i < response.getPropertyCount(); i++)
                                str = (SoapObject) response.getProperty(i);

                            SoapObject str1 = (SoapObject) str.getProperty(0);

                            SoapObject str2 = null;

                            for (int j = 0; j < str1.getPropertyCount(); j++) {
                                str2 = (SoapObject) str1.getProperty(j);
                                String res = str2.toString();
                                Employee cate = new Employee();
                                if (res.contains("No record found")) {

                                } else {
                                    if (isValidProperty(str2, "Employee_Id")) {
                                        cate.setEmployeeId(str2.getProperty("Employee_Id").toString().trim());
                                        EmployeeId = str2.getProperty("Employee_Id").toString().trim();
                                        settings.edit().putString("TAG_EMPLOYEEID", EmployeeId).commit();
                                    }

                                }

                            }

                        }
                    } else {
                        Toast.makeText(getActivity(), "No Response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Response", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
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

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
			case R.id.RegisterButton_signup:
				SignUpAsync signUpAsync=new SignUpAsync();
				signUpAsync.execute();
			break;
		}
	}
}
