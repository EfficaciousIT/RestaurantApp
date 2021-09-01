package com.mobi.efficacious.esmartrestaurant.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.common.ConnectionDetector;
import com.mobi.efficacious.esmartrestaurant.entity.DataHolder;
import com.mobi.efficacious.esmartrestaurant.entity.Menus;
import com.mobi.efficacious.esmartrestaurant.webservice.Constants;
import com.mobi.efficacious.esmartrestaurant.webservice.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;


public class MenuAdapter extends BaseAdapter implements Filterable {
	Context context;
	int Status=0,orderStatus=0;
	static int layoutResourceId;
	ArrayList<Menus> menus = new ArrayList<Menus>();
	public ArrayList<Menus> categories;
	public ArrayList<Menus> orig;
	int counter=0;
	ConnectionDetector cd;
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
	int Total,Orderqty,qty1;
	String OrderVAlue;
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
	                                    .contains(constraint.toString().toLowerCase()))
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
          settings = context.getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
          RESTAURANTID = settings.getString("TAG_RESTAURANTID", "");
          EmployeeId = settings.getString("TAG_EMPLOYEEID", "");
		  RegisterId = settings.getString("TAG_REGISTERID", "");
		  if (RegisterId.equalsIgnoreCase("")) {
			  RegisterId = "0";
		  }

          TextView txtTitle = (TextView) row.findViewById(R.id.item_Name_menu);
          //Spinner Qtyspinner = (Spinner) row.findViewById(R.id.qtyspinner_menu);
          ImageButton DecrementOreder = (ImageButton) row.findViewById(R.id.decrement);
          ImageButton IncrementOreder = (ImageButton) row.findViewById(R.id.increment);
          TextView txtprice = (TextView) row.findViewById(R.id.price_menu);
          final TextView orderValue = (TextView) row.findViewById(R.id.ordervalue);
          final CheckBox chkCheck = (CheckBox) row.findViewById(R.id.chk_menu);
          DecrementOreder.setOnClickListener(new OnClickListener() {
              @Override
              public void onClick(View v) {
                  OrderVAlue= orderValue.getText().toString();
                  Orderqty= Integer.parseInt(OrderVAlue);
                  if(Orderqty>=0)
                  {
                      Orderqty--;
					  menus.get(position).setQty(String.valueOf(Orderqty));
                      orderValue.setText(String.valueOf(Orderqty));
                  }else
                  {
                      orderValue.setText("0");
                  }
              }
          });
          IncrementOreder.setOnClickListener(new OnClickListener() {
              @Override
              public void onClick(View v) {
                  OrderVAlue= orderValue.getText().toString();
                  Orderqty= Integer.parseInt(OrderVAlue);
                  if(Orderqty>=0)
                  {
                      Orderqty++;
					  menus.get(position).setQty(String.valueOf(Orderqty));
                      orderValue.setText(String.valueOf(Orderqty));
                  }else
                  {
                      orderValue.setText("0");
                  }
              }
          });
		    	
		    	chkCheck.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) 
					{
                        qty=  menus.get(position).getQty();
                        qty1= Integer.parseInt(qty);
                        if(qty1>0)
                        {
                            CheckBox cb=(CheckBox) v;
                            Menus contact=(Menus)cb.getTag();
                            menu_name = menus.get(position).getMenu_Name();
                            rate = menus.get(position).getPrice();
                            menus.get(position).setChecked(cb.isChecked());
                            cd = new ConnectionDetector(context);
                            if (!cd.isConnectingToInternet())
                            {
                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                alert.setMessage("No Internet Connection");
                                alert.setPositiveButton("OK",null);
                                alert.show();
                            }else {
                                PostOrder postOrder=new PostOrder();
                                postOrder.execute();
                            }
                        }else
                        {
							menus.get(position).setChecked(false);
							chkCheck.setChecked(menus.get(position).isChecked());
							chkCheck.setTag(menus.get(position));
                            Toast.makeText(context,"Please Select Quantity First ",Toast.LENGTH_SHORT).show();
                        }


					}
				});

		    	String qqty=menus.get(position).getQty();
		  if (TextUtils.isEmpty(qqty)) {
			  menus.get(position).setQty("0");
		  }
		  else
		  	{
					orderValue.setText(menus.get(position).getQty());
		  	}

		  chkCheck.setChecked(menus.get(position).isChecked());
		  chkCheck.setTag(menus.get(position));
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
			if(Status==1)
			{
				UpdateOrder updateOrder=new UpdateOrder();
				updateOrder.execute();
			}else
			{
				Toast.makeText(context, "Error while Adding Order to chart",Toast.LENGTH_LONG).show();
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
							Toast.makeText(context, "Error",Toast.LENGTH_LONG).show();
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
			if(orderStatus==0)
			{
				Toast.makeText(context, "Error while Adding Order to chart",Toast.LENGTH_LONG).show();
			}else
			{
				Toast.makeText(context, "Order Add to Chart",Toast.LENGTH_LONG).show();
			}

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

}