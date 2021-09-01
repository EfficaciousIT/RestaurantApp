package com.mobi.efficacious.esmartrestaurant.activity;

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
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.mobi.efficacious.esmartrestaurant.entity.Tables;
import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.adapter.mergetable_adapter;
import com.mobi.efficacious.esmartrestaurant.fragment.CategoryActivity;
import com.mobi.efficacious.esmartrestaurant.fragment.mergetable_option;
import com.mobi.efficacious.esmartrestaurant.webservice.Constants;
import com.mobi.efficacious.esmartrestaurant.webservice.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class People_Count_dialogBox extends Activity {
    EditText et_PersonCount;
    Button savebtn,cancelbtn;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    String OrderId;
    String TableName;
    String mTable,mTableId,tableSelected;
    String DeviceId,SplitStatus;
    String EmployeeId,PersonCOunt;
    String RegisterId,RESTAURANTID,Pagename;
    mergetable_adapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.person_count_dialogbox);
        et_PersonCount=(EditText)findViewById(R.id.editText288);
        savebtn=(Button)findViewById(R.id.btnsave);
        cancelbtn=(Button)findViewById(R.id.btnCancel);
        try
        {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(People_Count_dialogBox.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return ;
            }
            DeviceId = tm.getDeviceId();
            if (DeviceId == null) {
                DeviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            settings = getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
            RESTAURANTID = settings.getString("TAG_RESTAURANTID", "");
            EmployeeId = settings.getString("TAG_EMPLOYEEID", "");
            RegisterId = settings.getString("TAG_REGISTERID", "");
            if (RegisterId.equalsIgnoreCase("")) {
                RegisterId = "0";
            }
            Intent intent = getIntent();
            TableName = intent.getStringExtra("TableName");
            SplitStatus=intent.getStringExtra("SplitStatus");
            Pagename=intent.getStringExtra("Pagename");
        }catch (Exception ex)
        {

        }


        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if(et_PersonCount.getText().toString().isEmpty()||et_PersonCount.getText().toString().contentEquals(""))
                    {
                        et_PersonCount.setError("Enter Valid No ");
                    }else
                    {

                        PersonCOunt=et_PersonCount.getText().toString();
                        InsertOrder insertOrder=new InsertOrder();
                        insertOrder.execute();
                    }

                }catch (Exception ex)
                {

                }

            }
        });
    }
    private class InsertOrder extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(People_Count_dialogBox.this);
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
            if(Pagename.contentEquals("Mergetable"))
            {
                MergeTAbelASYNC mergeTAbelASYNC=new MergeTAbelASYNC();
                mergeTAbelASYNC.execute();
            }else
            {
                finish();
                CategoryActivity categoryActivity = new CategoryActivity();
                Bundle args = new Bundle();
                args.putString("TableName",TableName);
                args.putString("OrderId",OrderId);
                args.putString("pagename","NewOrder");
                categoryActivity.setArguments(args);
                MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, categoryActivity).commitAllowingStateLoss();

            }

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
                request.addProperty("TableName", TableName);
                request.addProperty("RegisterId", RegisterId);
                request.addProperty("EmployeeId", EmployeeId);
                request.addProperty("DeviceId", DeviceId);
                request.addProperty("IsActive", 1);
                request.addProperty("status", "Open");
                request.addProperty("resId", RESTAURANTID);
                request.addProperty("intPersonCOunt", PersonCOunt);
                request.addProperty("vchSplit_status",SplitStatus);
                if(Pagename.contentEquals("SplitTable"))
                {
                    request.addProperty("vchSplitTableName","B");
                }
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
                        Toast.makeText(People_Count_dialogBox.this, "Null Response",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(People_Count_dialogBox.this, "Error",Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }
    public class MergeTAbelASYNC extends AsyncTask<Void, Void, Void> {
        //private final ProgressDialog dialog = new ProgressDialog(People_Count_dialogBox.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);;
          //  this.dialog.dismiss();
            finish();
            CategoryActivity categoryActivity = new CategoryActivity();
            Bundle args = new Bundle();
            args.putString("TableName",TableName);
            args.putString("OrderId",OrderId);
            args.putString("pagename","NewOrder");
            categoryActivity.setArguments(args);
            MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, categoryActivity).commitAllowingStateLoss();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try
            {

                for(int i=0;i<mergetable_option.tablename.size();i++)
                {
                    Tables singletable=mergetable_option.tablename.get(i);
                    tableSelected= String.valueOf(singletable.isMeregeTableChkbx());
                    if(tableSelected.contentEquals("true"))
                    {
                        mTable=singletable.getTable_Name();
                        mTableId=singletable.getTable_Id();
                        OPERATION_NAME = "insertMergetable";
                        SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
                        final Responce responce=new Responce();
                        SoapObject response = null;
                        String result = null;
                        try
                        {
                            SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                            request.addProperty("command", "insert");
                            request.addProperty("int_OrderId", OrderId);
                            request.addProperty("intTableId",mTableId);
                            request.addProperty("vchTableName",mTable);
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
                    }


                }
            }catch (Exception ex)
            {
                ex.printStackTrace();
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
