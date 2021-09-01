package com.mobi.efficacious.esmartrestaurant.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.webservice.Constants;
import com.mobi.efficacious.esmartrestaurant.webservice.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Food_description_dialogBox extends Activity {
    Button btn_positive,btn_negative;
    EditText et_name;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private static  String SOAP_ACTION = "";
    private static  String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    String TableName;
    String CategoryName;
    String OrderId;
    String qty;
    String rate;
    String menu_name;
    int Status=0,orderStatus=0;
    String EmployeeId;
    String RegisterId;
    int Total;
    String foodDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alertdialog_custom_view);
         btn_positive = (Button) findViewById(R.id.dialog_positive_btn);
         btn_negative = (Button) findViewById(R.id.dialog_negative_btn);
          et_name = (EditText) findViewById(R.id.et_name);
        Intent intent = getIntent();
        OrderId=intent.getStringExtra("OrderId");
        CategoryName=intent.getStringExtra("CategoryName");
        menu_name=intent.getStringExtra("MenuName");
        TableName=intent.getStringExtra("TableName");
        RegisterId=intent.getStringExtra("RegisterId");
        EmployeeId=intent.getStringExtra("EmployeeId");
        rate=intent.getStringExtra("Price");
        qty=intent.getStringExtra("Qty");

        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                foodDescription = et_name.getText().toString();
                PostOrder postOrder=new PostOrder();
                postOrder.execute();


            }
        });
        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostOrder postOrder=new PostOrder();
                postOrder.execute();
            }
        });
    }
    private class PostOrder extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(Food_description_dialogBox.this);

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
            super.onPostExecute(aVoid);
            this.dialog.dismiss();
            if(Status==1)
            {
                UpdateOrder updateOrder=new UpdateOrder();
                updateOrder.execute();
            }else
            {
                Toast.makeText(Food_description_dialogBox.this, "Error while Adding Order to chart",Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try
            {
                OPERATION_NAME = "InsertOrderTrasaction";
                SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
                final Responce responce=new Responce();
                SoapObject response = null;
                String result = null;
                try
                {
                    SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                    request.addProperty("command", "insert");
                    request.addProperty("OrderId", OrderId);
                    request.addProperty("CategoryName", CategoryName);
                    request.addProperty("MenuName", menu_name);
                    request.addProperty("TableName", TableName);
                    request.addProperty("RegisterId", RegisterId);
                    request.addProperty("EmployeeId", EmployeeId);
                    request.addProperty("Price", rate);
                    request.addProperty("Qty", qty);
                    request.addProperty("status", "Open");
                    request.addProperty("Kitchen_status", "Pending");
                    request.addProperty("vchFoodDescription",foodDescription);
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
                            if(result.contains("true"))
                            {
                                Total = (Total+Integer.parseInt(rate) * Integer.parseInt(qty));
                                Status=1;

                            }
                            else
                            {
                                Status=0;

                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(Food_description_dialogBox.this, "Error",Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }catch (Exception ex)
            {
                ex.printStackTrace();
            }
            return null;
        }
    }

    private class UpdateOrder extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(Food_description_dialogBox.this);

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
            super.onPostExecute(aVoid);
            if(orderStatus==0)
            {
                Toast.makeText(Food_description_dialogBox.this, "Error while Adding Order to chart",Toast.LENGTH_LONG).show();
            }else
            {
                Toast.makeText(Food_description_dialogBox.this, "Order Add to Chart",Toast.LENGTH_LONG).show();
            }

            this.dialog.dismiss();
            finish();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            OPERATION_NAME = "UpdateOrder";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("command", "update");
                request.addProperty("OrderId", OrderId);
                request.addProperty("Total", Total);
                request.addProperty("IsActive", 1);
                request.addProperty("status", "Open");
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
                        if(result.contains("true"))
                        {
                            orderStatus=1;
                        }
                        else
                        {
                            orderStatus=0;
                        }
                    }
                }
                else
                {
                    Toast.makeText(Food_description_dialogBox.this, "Error",Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }
    }
}
