package com.mobi.efficacious.esmartrestaurant.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.adapter.OrderAdapter;
import com.mobi.efficacious.esmartrestaurant.common.ConnectionDetector;
import com.mobi.efficacious.esmartrestaurant.entity.OrderDetail;
import com.mobi.efficacious.esmartrestaurant.tab.ExistionOrder_Tab;
import com.mobi.efficacious.esmartrestaurant.webservice.Constants;
import com.mobi.efficacious.esmartrestaurant.webservice.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class Bill_fragment extends Fragment{
    ListView listView;
    String RESTAURANTID;
    ArrayList<OrderDetail> order = new ArrayList<OrderDetail>();
    OrderAdapter adapter;
    SharedPreferences sharedpreferences;
    ConnectionDetector cd;
    RelativeLayout relative;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private static  String SOAP_ACTION = "";
    private static  String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    Context mContext;
    int KitchenStatus=0;
    String TableName="",Kitchen_status;
    String OrderId;
    int TotalAmount;
    TextView TxtAmount;
    String rate;
    String qty;
    //	Button btnbill;
    Button btnkitchen;
    String result;
    View myview;
    int OrderStatus=0;
    String pagename;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.orderdetails_layout,null);
        mContext=getActivity();
        try
        {
            OrderId = getArguments().getString("OrderId");
            TableName = getArguments().getString("TableName");
            pagename="Bill";
        }catch (Exception ex)
        {
        }


        settings = getActivity().getSharedPreferences(PREFRENCES_NAME,Context.MODE_PRIVATE);
        RESTAURANTID = settings.getString("TAG_RESTAURANTID", "");

        cd = new ConnectionDetector(getActivity());
        listView = (ListView)myview.findViewById(R.id.listview_orderdetails);
        TxtAmount = (TextView)myview.findViewById(R.id.ViewOrderTotal_orderdetails);
        btnkitchen = (Button)myview.findViewById(R.id.btnSubmittokitchen_orderdetails);
        btnkitchen.setVisibility(View.GONE);




        PostData postData=new PostData();
        postData.execute();
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
                TxtAmount.setText(String.valueOf(TotalAmount));
                adapter = new OrderAdapter(mContext, order,pagename);
                listView.setAdapter(adapter);
                this.dialog.dismiss();
            }catch (Exception ex)
            {

            }

        }
        protected Void doInBackground(Void... params) {

            OPERATION_NAME = "GetOrder";
            SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
            final Responce responce=new Responce();
            SoapObject response = null;
            String result = null;
            try
            {
                SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
                request.addProperty("command", "select");
                request.addProperty("TableName", TableName);
                request.addProperty("orderId", OrderId);

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
                                OrderDetail ord =new OrderDetail();
                                if(res.contains("No record found"))
                                {

                                }
                                else
                                {
                                    if(isValidProperty(str2, "Kitchen_status"))
                                    {
                                        ord.setKitchenStaus(str2.getProperty("Kitchen_status").toString().trim());
                                    }
                                    if(isValidProperty(str2, "Id"))
                                    {
                                        ord.setId(str2.getProperty("Id").toString().trim());
                                    }

                                    if(isValidProperty(str2, "Order_Id"))
                                    {
                                        ord.setOrder_Id(str2.getProperty("Order_Id").toString().trim());
                                    }

                                    if(isValidProperty(str2, "Cat_Name"))
                                    {
                                        ord.setCat_Name(str2.getProperty("Cat_Name").toString().trim());
                                    }

                                    if(isValidProperty(str2, "Menu_Name"))
                                    {
                                        ord.setMenu_Name(str2.getProperty("Menu_Name").toString().trim());
                                    }

                                    if(isValidProperty(str2, "Price"))
                                    {
                                        ord.setPrice(str2.getProperty("Price").toString().trim());
                                        rate = str2.getProperty("Price").toString().trim();
                                    }

                                    if(isValidProperty(str2, "Table_Name"))
                                    {
                                        ord.setTable_Name(str2.getProperty("Table_Name").toString().trim());
                                    }

                                    if(isValidProperty(str2, "Qty"))
                                    {
                                        ord.setQty(str2.getProperty("Qty").toString().trim());
                                        qty = str2.getProperty("Qty").toString().trim();
                                    }

                                    TotalAmount = (TotalAmount + Integer.parseInt(rate) * Integer.parseInt(qty));
                                    order.add(ord);

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
