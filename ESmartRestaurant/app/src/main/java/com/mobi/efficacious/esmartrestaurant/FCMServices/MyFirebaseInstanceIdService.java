package com.mobi.efficacious.esmartrestaurant.FCMServices;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.mobi.efficacious.esmartrestaurant.webservice.Constants;
import com.mobi.efficacious.esmartrestaurant.webservice.Responce;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


/**
 * Created by haripal on 7/25/17.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String RESTAURANTID;
    String USERTYPEID;
    String EMPLOYEEID,command;
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (refreshedToken != null) {
            Log.d(TAG, "Refreshed token: " + refreshedToken);
            settings = getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
            RESTAURANTID= settings.getString("TAG_RESTAURANTID", "");
            USERTYPEID= settings.getString("TAG_USERTYPEID", "");
            EMPLOYEEID= settings.getString("TAG_EMPLOYEEID", "");


                sendTokenToServer(refreshedToken);


        }
    }

    public void sendTokenToServer(final String strToken) {
        // API call to send token to Server
//        settings.edit().putString("TAG_USERFIREBASETOKEN",strToken).clear();
      String  email = settings.getString("TAG_USEREMAILID", "");

//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        command="FcmTokenEmployee";
        settings.edit().putString("TAG_USERFIREBASETOKEN",strToken).apply();
        FCMTOKENASYNC fcmtokenasync=new FCMTOKENASYNC(command,strToken,email);
        fcmtokenasync.execute();
    }
    private class FCMTOKENASYNC extends AsyncTask<Void, Void, Void> {
        String Command;
        String firebasetoken,Email;
        public FCMTOKENASYNC(String command,String Firebasetoken,String EMail) {
            Command=command;
            firebasetoken=Firebasetoken;
            Email=EMail;
        }

        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "FCMTokenUpdate";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command",Command);
                request.addProperty("vchFcmToken",firebasetoken);
                request.addProperty("Res_Id",RESTAURANTID);
                request.addProperty("Email_Id",Email);
                request.addProperty("Employee_Id",EMPLOYEEID);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet=true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE ht = new HttpTransportSE(Constants.strWEB_SERVICE_URL);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                ht.debug = true;
                ht.call(SOAP_ACTION, envelope);
                if (!(envelope.bodyIn instanceof SoapFault)) {
                    if (envelope.bodyIn instanceof SoapObject)
                        response = (SoapObject) envelope.bodyIn;
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}