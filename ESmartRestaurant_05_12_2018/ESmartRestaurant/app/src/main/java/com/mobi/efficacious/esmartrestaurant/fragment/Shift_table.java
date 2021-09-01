package com.mobi.efficacious.esmartrestaurant.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.activity.MainActivity;
import com.mobi.efficacious.esmartrestaurant.adapter.DashBoardAdapter;
import com.mobi.efficacious.esmartrestaurant.common.ConnectionDetector;
import com.mobi.efficacious.esmartrestaurant.entity.Tables;
import com.mobi.efficacious.esmartrestaurant.webservice.Constants;
import com.mobi.efficacious.esmartrestaurant.webservice.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class Shift_table  extends Fragment {
    GridView gridView;
    String RESTAURANTID;
    ArrayList<Tables> tables = new ArrayList<Tables>();
    DashBoardAdapter adapter;
    SharedPreferences sharedpreferences;
    ConnectionDetector cd;
    RelativeLayout relative;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    Context mContext;
    String OrderId;
    String TableName;
    String DeviceId;
    String EmployeeId,tableName,orderId;
    String RegisterId;
    View myview;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview = inflater.inflate(R.layout.new_dashboard_layout, null);
        mContext = getActivity();
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        RESTAURANTID = settings.getString("TAG_RESTAURANTID", "");
        tableName = getArguments().getString("TableName");
        orderId = getArguments().getString("OrderId");
        cd = new ConnectionDetector(getActivity());
        gridView = (GridView) myview.findViewById(R.id.gridView1);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TableName = ((TextView) view.findViewById(R.id.item_text)).getText().toString();
                try {
                    DialogInterface.OnClickListener dialogClickListenerr = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    UpdateTAble updateTAble=new UpdateTAble();
                                    updateTAble.execute();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    } ;

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure want to Shift "+ tableName +" to "+TableName+" ?").setPositiveButton("Yes", dialogClickListenerr)
                            .setNegativeButton("No", dialogClickListenerr).show();

                }catch(Exception ex)
                {

                }

            }
        });
        if (!cd.isConnectingToInternet()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setMessage("No Internet Connection");
            alert.setPositiveButton("OK", null);
            alert.show();
        } else{
            PostData postData = new PostData();
            postData.execute();
        }
        return myview;
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
                adapter = new DashBoardAdapter(mContext, tables);
                gridView.setAdapter(adapter);
            }catch (Exception ex)
            {

            }

            this.dialog.dismiss();
        }
        protected Void doInBackground(Void... params) {
            OPERATION_NAME = "GetTable";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("command", "select");
                request.addProperty("res_id", RESTAURANTID);

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
                                Tables tab =new Tables();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {
                                    if(isValidProperty(str2, "Table_Name"))
                                    {
                                        tab.setTable_Name(str2.getProperty("Table_Name").toString().trim());
                                    }

                                    tables.add(tab);
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

    private class UpdateTAble extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(getActivity());
        //
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Processing...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);;
            this.dialog.dismiss();
            ExistingOrderedTableActivity existingOrderedTableActivity = new ExistingOrderedTableActivity();
            Bundle args = new Bundle();
            args.putString("pagename","ExistingOrder");
            existingOrderedTableActivity.setArguments(args);
           MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, existingOrderedTableActivity).commitAllowingStateLoss();
        }

        @Override
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "UpdateTable";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);

                request.addProperty("command", "update");
                request.addProperty("OrderId", orderId);
                request.addProperty("Table_Name",TableName);
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
