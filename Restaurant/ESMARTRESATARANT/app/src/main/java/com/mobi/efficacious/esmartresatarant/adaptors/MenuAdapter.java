package com.mobi.efficacious.esmartresatarant.adaptors;

import java.net.Inet4Address;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.esmartresatarant.R;
import com.mobi.efficacious.esmartresatarant.common.AlertDialogManager;
import com.mobi.efficacious.esmartresatarant.common.ConnectionDetector;
import com.mobi.efficacious.esmartresatarant.entity.DataHolder;
import com.mobi.efficacious.esmartresatarant.entity.Menus;
import com.mobi.efficacious.esmartresatarant.webservice.Constants;
import com.mobi.efficacious.esmartresatarant.webservice.Responce;


public class MenuAdapter extends BaseAdapter implements Filterable {
	Context context;
	static int layoutResourceId;
	ArrayList<Menus> menus = new ArrayList<Menus>();
	public ArrayList<Menus> categories;
	public ArrayList<Menus> orig;
	int counter=0;
	ConnectionDetector cd;
	AlertDialogManager alert;
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
	String RESTAURANTID;
	String EmployeeId;
	String RegisterId;
	int Total;
	
	public MenuAdapter(Context context, ArrayList<Menus> Menus, String tableName, String categoryName, String orderId) {
		super();
		this.context = context;
		this.menus = Menus;
		this.TableName = tableName;
		this.CategoryName = categoryName;
		this.OrderId = orderId;
	}
	 
	 public Filter getFilter() 
	    {
	        return new Filter() 
	        {
	            @Override
	            protected FilterResults performFiltering(CharSequence constraint) {
	                final FilterResults oReturn = new FilterResults();
	                final ArrayList<Menus> results = new ArrayList<Menus>();
	                if (orig == null)
	                    orig = menus;
	                if (constraint != null) {
	                    if (orig != null && orig.size() > 0) {
	                        for (final Menus g : orig) {
	                            if (g.getMenu_Name().toLowerCase()
	                                    .contains(constraint.toString()))
	                                results.add(g);
	                        }
	                    }
	                    oReturn.values = results;
	                }
	                return oReturn;
	            }

	            @SuppressWarnings("unchecked")
	            @Override
	            protected void publishResults(CharSequence constraint,
	              FilterResults results) {
	            	menus = (ArrayList<Menus>) results.values;
	                notifyDataSetChanged();
	            }
	        };
	    }

	    public void notifyDataSetChanged() {
	        super.notifyDataSetChanged();
	    }

	    @Override
	    public int getCount() {
	        return menus.size();
	    }

	    @Override
	    public Object getItem(int position) {
	        return menus.get(position);
	    }

	    @Override
	    public long getItemId(int position) {
	        return position;
	    }

	  public View getView(final int position, View convertView, ViewGroup parent) {
	   
		    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	View row = inflater.inflate(R.layout.menuadpater_row, parent, false);
	    		final DataHolder data = new DataHolder(context);
	   		    settings = context.getSharedPreferences(PREFRENCES_NAME,Context.MODE_PRIVATE);
	   		    RESTAURANTID = settings.getString("TAG_RESTAURANTID", "");
	   		    EmployeeId = settings.getString("TAG_EMPLOYEEID", "");
	   		    RegisterId = settings.getString("TAG_REGISTERID", "");
	   		    if(RegisterId.contentEquals(""))
				{
					RegisterId="0";
				}
	   		    TextView txtTitle = (TextView) row.findViewById(R.id.item_Name_menu);
		   		Spinner Qtyspinner = (Spinner) row.findViewById(R.id.qtyspinner_menu);
		   		TextView txtprice = (TextView) row.findViewById(R.id.price_menu);
		   		CheckBox chkCheck = (CheckBox) row.findViewById(R.id.chk_menu);
		   		
		    	Qtyspinner.setAdapter(data.getAdapter());
		    	Qtyspinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	                @Override
	                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	                {
	                	data.setSelected(arg2);	                
	                	qty = data.getText();
	                }

	                @Override
	                public void onNothingSelected(AdapterView<?> arg0) 
	                {
	                	
	                }

	            });
		    	
		    	chkCheck.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) 
					{
						menu_name = menus.get(position).getMenu_Name();
						rate = menus.get(position).getPrice();
						alert = new AlertDialogManager();
						cd = new ConnectionDetector(context);
						if (!cd.isConnectingToInternet())
						{
							alert = new AlertDialogManager();
							cd = new ConnectionDetector(context);
							alert.showAlertDialog(context,"No Internet Connection","Please connect to working Internet connection", false);

						}else {
							PostOrder postOrder=new PostOrder();
							postOrder.execute();
						}

					}
				});
		   
		    txtprice.setText(menus.get(position).getPrice());
		   	txtTitle.setText(menus.get(position).getMenu_Name());
		   
		   return row;
	  }

	private class PostOrder extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(context);

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
									alert = new AlertDialogManager();
									cd = new ConnectionDetector(context);
									if (!cd.isConnectingToInternet())
									{
										alert = new AlertDialogManager();
										cd = new ConnectionDetector(context);
										alert.showAlertDialog(context,"No Internet Connection","Please connect to working Internet connection", false);

									}else
									{
										UpdateOrder updateOrder=new UpdateOrder();
										updateOrder.execute();
									}

								}
								else
								{
									Toast.makeText(context, "Order not Placed",Toast.LENGTH_LONG).show();
								}
							}
						}
						else
						{
							Toast.makeText(context, "Error",Toast.LENGTH_LONG).show();
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}

			}catch (Exception ex)
			{

			}
			return null;
		}
	}

	private class UpdateOrder extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(context);

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

							}
							else
							{
								Toast.makeText(context, "Order not Placed",Toast.LENGTH_LONG).show();
							}
						}
					}
					else
					{
						Toast.makeText(context, "Error",Toast.LENGTH_LONG).show();
					}
				}
				catch (Exception e)
				{
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
}