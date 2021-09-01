package com.mobi.efficacious.esmartrestaurant.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.common.ConnectionDetector;
import com.mobi.efficacious.esmartrestaurant.database.Databasehelper;
import com.mobi.efficacious.esmartrestaurant.fragment.ForgotPassword;
import com.mobi.efficacious.esmartrestaurant.fragment.SignUpActivity;
import com.mobi.efficacious.esmartrestaurant.webservice.Constants;
import com.mobi.efficacious.esmartrestaurant.webservice.Responce;
import com.mobi.efficacious.esmartrestaurant.webservice.Utility;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import static com.mobi.efficacious.esmartrestaurant.common.Spinner_error.setSpinnerError;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener
{
    Button btnSubmit;
    Context mContext;
    EditText UserName;
    EditText Password;
    Spinner RoleName;
    String loggerName;
    Utility utility;
    String[] Roles = { "--Select--", "Manager", "Waiter","Kitchen"};
    int loggerId;
    String User_name;
    String passWord;
    String flag;
    String RESTAURANTID;
    String USERTYPEID;
    String EMPLOYEEID;
    int LoginStatus=1;
    private static  String SOAP_ACTION = "";
    private static  String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    TelephonyManager tel;
    ConnectionDetector cd;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    Databasehelper mydb;
    FrameLayout content_main;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        settings = getSharedPreferences(PREFRENCES_NAME,Context.MODE_PRIVATE);
        mContext=LoginActivity.this;
        cd = new ConnectionDetector(getApplicationContext());
        RoleName=(Spinner)findViewById(R.id.RoleName_Login);
        content_main=(FrameLayout)findViewById(R.id.content_main);
        UserName=(EditText)findViewById(R.id.UserName_Login);
        Password=(EditText)findViewById(R.id.Password_Login);
        btnSubmit=(Button)findViewById(R.id.Submit_login);
        btnSubmit.setOnClickListener(this);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence> (this, android.R.layout.simple_spinner_item, Roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RoleName.setAdapter(adapter);
        RoleName.setOnItemSelectedListener(this);
        mydb = new Databasehelper(LoginActivity.this, "OrderMaster", null, 1);
        mydb.query("Create table if not exists tblOrderMaster(ID INTEGER PRIMARY KEY AUTOINCREMENT,orderId varchar ,CategoryName varchar ,MenuName varchar ,TableName varchar,RegisterId varchar,EmployeeId varchar,Price varchar,QTY varchar,status varchar,KitchenStatus varchar,Total INTEGER )");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId())
        {
            case R.id.RoleName_Login:
                loggerId = position;
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.Submit_login:
                if (UserName.getText().toString().contentEquals("")||Password.getText().toString().contentEquals("")||loggerId==0) {
                    if (TextUtils.isEmpty(UserName.getText().toString())) {
                        UserName.setError("Enter Valid Username ");
                    }
                    if (TextUtils.isEmpty(Password.getText().toString())) {
                        Password.setError("Enter Valid Password ");
                    }
                    if (loggerId==0) {
                        setSpinnerError(RoleName,"Select valid Usertype ");
                    }
                }else
                {
                    User_name=UserName.getText().toString();
                    passWord=Password.getText().toString();
                    PostData postData=new  PostData(User_name,passWord);
                    postData.execute();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }
    private class PostData extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
String userNAme,password;
        public PostData(String user_name, String passWord) {
            userNAme=user_name;
            password=passWord;
        }


        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Loading...");
            dialog.show();

        }


        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(LoginStatus==1)
            {
                Intent MainScreenIntent=new Intent(LoginActivity.this, Gmail_login.class);
                startActivity(MainScreenIntent);
                finish();

            }else
            {
                Toast.makeText(LoginActivity.this,"Incorrect Id Or Password",Toast.LENGTH_SHORT).show();
            }
            this.dialog.dismiss();
        }
        protected Void doInBackground(Void... params) {
            OPERATION_NAME = "GetLoginDetails";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("command", "select");
                request.addProperty("userType_id", loggerId);
                request.addProperty("userName",  userNAme);
                request.addProperty("password", password);

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
                        if(result.contains("no record found"))
                        {
                            LoginStatus=0;
                        }
                        else
                        {
                            SoapObject root = (SoapObject) response.getProperty(0);
                            SoapObject table = (SoapObject) root.getProperty("NewDataSet");
                            SoapObject column = (SoapObject) table.getProperty("Login");
                            String res = column.toString();
                            LoginStatus=1;
                            if(res.contains("Restaurant_id"))
                            {
                                RESTAURANTID=column.getProperty("Restaurant_id").toString();
                                settings.edit().putString("TAG_RESTAURANTID", RESTAURANTID).commit();
                            }
                            else
                            {

                            }

                            if(res.contains("UserType_Id"))
                            {
                                USERTYPEID=column.getProperty("UserType_Id").toString();
                                settings.edit().putString("TAG_USERTYPEID", USERTYPEID).commit();
                            }
                            else
                            {

                            }

                            if(res.contains("Employee_Id"))
                            {
                                EMPLOYEEID=column.getProperty("Employee_Id").toString();
                                settings.edit().putString("TAG_EMPLOYEEID", EMPLOYEEID).commit();
                            }
                            else
                            {

                            }



                        }

                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "No Response",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Error",Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                LoginStatus=0;
                e.printStackTrace();
            }
            return null;
        }
    }


}
