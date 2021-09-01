package com.mobi.efficacious.esmartrestaurant.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.activity.MainActivity;
import com.mobi.efficacious.esmartrestaurant.activity.People_Count_dialogBox;
import com.mobi.efficacious.esmartrestaurant.adapter.DashBoardAdapter;
import com.mobi.efficacious.esmartrestaurant.adapter.ExistingTableAdpter;
import com.mobi.efficacious.esmartrestaurant.adapter.mergetable_adapter;
import com.mobi.efficacious.esmartrestaurant.entity.Tables;
import com.mobi.efficacious.esmartrestaurant.webservice.Constants;
import com.mobi.efficacious.esmartrestaurant.webservice.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class mergetable_option extends Fragment {
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    private static String SOAP_ACTION = "";
    private static String OPERATION_NAME = "GetAboutADSXML";
    public static String strURL;
    View myview;
    String RESTAURANTID,tableSelected;
    ArrayList<Tables> tables = new ArrayList<Tables>();
    mergetable_adapter adapter;
    GridView gridView;
   public static ArrayList<Tables> tablename;
    Button btn_confrm,btn_cancel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       myview=inflater.inflate(R.layout.mergetable_gridview,null);
        settings = getActivity().getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        RESTAURANTID = settings.getString("TAG_RESTAURANTID", "");
        gridView = (GridView) myview.findViewById(R.id.gridView1);
        btn_confrm=(Button) myview.findViewById(R.id.btn_confrm);
        btn_cancel=(Button) myview.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                New_Dashboard new_dashboard = new New_Dashboard();
                MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, new_dashboard).commitAllowingStateLoss();
            }
        });
        btn_confrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String tablenamess="",tablenames;
                    tablename=((mergetable_adapter)adapter).getMergeTable();
                    for(int i=0;i<tablename.size();i++)
                    {
                        Tables singletable=tablename.get(i);
                        tableSelected= String.valueOf(singletable.isMeregeTableChkbx());
                        if(tableSelected.contentEquals("true"))
                        {
                            tablenames=singletable.getTable_Name();
                            tablenamess=tablenamess+" "+tablenames;
                        }

                    }
                    DialogInterface.OnClickListener dialogClickListenerr = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    Intent intent=new Intent(getActivity(),People_Count_dialogBox.class);
                                    intent.putExtra("TableName","MergeTable");
                                    intent.putExtra("Pagename","Mergetable");
                                    intent.putExtra("SplitStatus","No");
                                    startActivity(intent);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    New_Dashboard new_dashboard = new New_Dashboard();
                                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, new_dashboard).commitAllowingStateLoss();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure want to Merge " +tablenamess +" ?").setPositiveButton("Yes", dialogClickListenerr)
                            .setNegativeButton("No", dialogClickListenerr).show();

                } catch (Exception ex) {

                }
            }
        });
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
                adapter = new mergetable_adapter(getContext(), tables);
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
                request.addProperty("command", "selectForMergetable");
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
                                    if(isValidProperty(str2, "Table_Id"))
                                    {
                                        tab.setTable_Id(str2.getProperty("Table_Id").toString().trim());
                                    }
                                    tab.setMeregeTableChkbx(false);
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
