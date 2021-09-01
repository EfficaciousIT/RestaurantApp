package com.mobi.efficacious.esmartrestaurant.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.adapter.ExistingTableAdpter;
import com.mobi.efficacious.esmartrestaurant.adapter.OrderAdapter;
import com.mobi.efficacious.esmartrestaurant.adapter.confrmation_order;
import com.mobi.efficacious.esmartrestaurant.entity.OrderDetail;
import com.mobi.efficacious.esmartrestaurant.fragment.main_fragment;
import com.mobi.efficacious.esmartrestaurant.webservice.Constants;
import com.mobi.efficacious.esmartrestaurant.webservice.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class order_confrm_dialogbox extends Activity {
    ListView listView;
    int OrderStatus = 0;
    Button btn_confrm, btn_cancel;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    confrmation_order adapter;
    int KitchenStatus = 0;
    ArrayList<OrderDetail> order = new ArrayList<OrderDetail>();
    String rate;
    String qty;
    String TableName, EmployeeId, OrderId, Kitchen_status, TotalAmount;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.food_order_listview_confrm);
        listView = (ListView) findViewById(R.id.listview_orderdetails);
        btn_confrm = (Button) findViewById(R.id.btn_confrm);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        Intent intent = getIntent();
        TableName = intent.getStringExtra("TableName");
        EmployeeId = intent.getStringExtra("EmployeeId");
        OrderId = intent.getStringExtra("OrderId");
        TotalAmount = intent.getStringExtra("TotalAmount");
        PostData postData = new PostData();
        postData.execute();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_confrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostKitchen postKitchen = new PostKitchen();
                 postKitchen.execute();
            }
        });
    }

    private class PostData extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(order_confrm_dialogbox.this);


        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Loading...");
            dialog.show();

        }


        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                adapter = new confrmation_order(order_confrm_dialogbox.this, order);
                listView.setAdapter(adapter);
                this.dialog.dismiss();
            } catch (Exception ex) {

            }

        }

        protected Void doInBackground(Void... params) {
            OPERATION_NAME = "GetOrder";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("command", "select");
                request.addProperty("TableName", TableName);
                request.addProperty("EmployeeId", EmployeeId);
                request.addProperty("orderId", OrderId);

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
                                OrderDetail ord = new OrderDetail();
                                if (res.contains("No record found")) {

                                } else {
                                    if (isValidProperty(str2, "Kitchen_status")) {
                                        ord.setKitchenStaus(str2.getProperty("Kitchen_status").toString().trim());
                                        Kitchen_status = str2.getProperty("Kitchen_status").toString().trim();
                                        if (!Kitchen_status.contentEquals("Completed")) {
                                            KitchenStatus = 1;
                                        }
                                    }
                                    if (isValidProperty(str2, "Id")) {
                                        ord.setId(str2.getProperty("Id").toString().trim());
                                    }

                                    if (isValidProperty(str2, "Order_Id")) {
                                        ord.setOrder_Id(str2.getProperty("Order_Id").toString().trim());
                                    }

                                    if (isValidProperty(str2, "Cat_Name")) {
                                        ord.setCat_Name(str2.getProperty("Cat_Name").toString().trim());
                                    }

                                    if (isValidProperty(str2, "Menu_Name")) {
                                        ord.setMenu_Name(str2.getProperty("Menu_Name").toString().trim());
                                    }

                                    if (isValidProperty(str2, "Price")) {
                                        ord.setPrice(str2.getProperty("Price").toString().trim());
                                        rate = str2.getProperty("Price").toString().trim();
                                    }

                                    if (isValidProperty(str2, "Table_Name")) {
                                        ord.setTable_Name(str2.getProperty("Table_Name").toString().trim());
                                    }

                                    if (isValidProperty(str2, "Qty")) {
                                        ord.setQty(str2.getProperty("Qty").toString().trim());
                                        qty = str2.getProperty("Qty").toString().trim();
                                    }
                                    if (isValidProperty(str2, "vchFoodDescription")) {
                                        ord.setVchFoodDescription(str2.getProperty("vchFoodDescription").toString().trim());
                                    }
                                    if(!Kitchen_status.contentEquals("Completed"))
                                    {
                                        order.add(ord);
                                    }

                                }
                            }
                        }

                    } else {
                        Toast.makeText(order_confrm_dialogbox.this, "Null Response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(order_confrm_dialogbox.this, "Error", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class PostKitchen extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(order_confrm_dialogbox.this);


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
if(OrderStatus==0)
{
    Toast.makeText(order_confrm_dialogbox.this, "Order Placed successfully", Toast.LENGTH_LONG).show();
    finish();
    main_fragment homeFragment = new main_fragment();
    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, homeFragment).commitAllowingStateLoss();
}else
{
    Toast.makeText(order_confrm_dialogbox.this, "Error while Order Placing", Toast.LENGTH_LONG).show();

}

        }

        protected Void doInBackground(Void... params) {
            OPERATION_NAME = "UpdateOrder";
            SOAP_ACTION = Constants.strNAMESPACE + "" + OPERATION_NAME;
            final Responce responce = new Responce();
            SoapObject response = null;
            String result = null;
            try {
                SoapObject request = new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("command", "update");
                request.addProperty("OrderId", OrderId);
                request.addProperty("Total", TotalAmount);
                request.addProperty("IsActive", 0);
                request.addProperty("status", "Kitchen");
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
                                OrderDetail ord = new OrderDetail();
                                if (res.contains("true")) {
                                    OrderStatus = 0;
                                } else {
                                    OrderStatus = 1;
                                }
                            }
                        }
                    } else {
                        Toast.makeText(order_confrm_dialogbox.this, "Null Response", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(order_confrm_dialogbox.this, "Error", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    boolean isValidProperty(SoapObject soapObject, String PropertyName) {
        if (soapObject != null) {
            if (soapObject.getProperty(PropertyName) != null) {
                if (!soapObject.getProperty(PropertyName).toString().equalsIgnoreCase("") && !soapObject.getProperty(PropertyName).toString().equalsIgnoreCase("anyType{}"))
                    return true;
                else
                    return false;
            }
            return false;
        } else
            return false;
    }
}


