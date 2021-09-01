package com.mobi.efficacious.esmartrestaurant.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.activity.MainActivity;
import com.mobi.efficacious.esmartrestaurant.activity.People_Count_dialogBox;
import com.mobi.efficacious.esmartrestaurant.entity.ExistingTable;
import com.mobi.efficacious.esmartrestaurant.fragment.Bill_fragment;
import com.mobi.efficacious.esmartrestaurant.fragment.OrderDetailsActivity;
import com.mobi.efficacious.esmartrestaurant.fragment.Shift_table;
import com.mobi.efficacious.esmartrestaurant.tab.ExistionOrder_Tab;
import com.mobi.efficacious.esmartrestaurant.webservice.Constants;
import com.mobi.efficacious.esmartrestaurant.webservice.Responce;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;


public class ExistingTableAdpter extends ArrayAdapter<ExistingTable> {
	Context context;
	static int layoutResourceId;
	ArrayList<ExistingTable> table = new ArrayList<ExistingTable>();
	TextView txtTitle;
	ImageView imageItem;
	LinearLayout top;
	String TableName;
	String OrderId;
	String pagenamee;
	String[] listItems;
	String SplitStatus;
	private static String SOAP_ACTION = "";
	private static String OPERATION_NAME = "GetAboutADSXML";
	public static String strURL;
	public ExistingTableAdpter(Context context, ArrayList<ExistingTable> tables, String pagename) {
		super(context, layoutResourceId, tables);
		this.context = context;
		this.table = tables;
		this.pagenamee = pagename;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.existingtableadapter_row, parent, false);
		top = (LinearLayout) rowView.findViewById(R.id.top_existingtable);
		txtTitle = (TextView) rowView.findViewById(R.id.itemtext_existingtable);
		imageItem = (ImageView) rowView.findViewById(R.id.itemimage_existingtable);
		top.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (pagenamee.contentEquals("ExistingOrder")) {
					listItems = getContext().getResources().getStringArray(R.array.table_option);
					AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
					mBuilder.setTitle("Sharing & Split");
					TableName = table.get(position).getTable_Name();
					OrderId = table.get(position).getOrder_Id();
					 SplitStatus=table.get(position).getVchSplit_status();
					mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							String selection = listItems[i];
							dialogInterface.dismiss();
							if (selection.contentEquals("Shift")) {
								String TableSelected = table.get(position).getTable_Name();

								if(TableSelected.contentEquals("TakeAway")||TableSelected.contentEquals("MergeTable"))
								{
									AlertDialog.Builder alert = new AlertDialog.Builder(context);
									alert.setMessage("You cannot Shift this table");
									alert.setPositiveButton("OK", null);
									alert.show();
								}else {
									try {
										DialogInterface.OnClickListener dialogClickListenerr = new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												switch (which) {
													case DialogInterface.BUTTON_POSITIVE:

														Shift_table existingOrderHomeActivity = new Shift_table();
														Bundle args = new Bundle();
														args.putString("TableName", TableName);
														args.putString("OrderId", OrderId);
														existingOrderHomeActivity.setArguments(args);
														MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, existingOrderHomeActivity).commitAllowingStateLoss();

														break;
													case DialogInterface.BUTTON_NEGATIVE:
														break;
												}
											}
										};

										AlertDialog.Builder builder = new AlertDialog.Builder(context);
										builder.setMessage("Are you sure want to Shift this table ?").setPositiveButton("Yes", dialogClickListenerr)
												.setNegativeButton("No", dialogClickListenerr).show();

									} catch (Exception ex) {

									}
								}
							}else if(selection.contentEquals("Split"))
							{
								String TableSelected = table.get(position).getTable_Name();
								if(SplitStatus.contentEquals("Yes")||TableSelected.contentEquals("TakeAway")||TableSelected.contentEquals("MergeTable"))
								{
									AlertDialog.Builder alert = new AlertDialog.Builder(context);
									alert.setMessage("You Cannot Split this table");
									alert.setPositiveButton("OK", null);
									alert.show();
								}else
								{
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
										};

										AlertDialog.Builder builder = new AlertDialog.Builder(context);
										builder.setMessage("Are you sure want to Split this table ?").setPositiveButton("Yes", dialogClickListenerr)
												.setNegativeButton("No", dialogClickListenerr).show();

									} catch (Exception ex) {

									}
								}

							}
						}
					});

					AlertDialog mDialog = mBuilder.create();
					mDialog.show();

				}
				return false;
			}
		});
		top.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TableName = table.get(position).getTable_Name();
				OrderId = table.get(position).getOrder_Id();
				if (pagenamee.contentEquals("ExistingOrder")) {
					ExistionOrder_Tab existingOrderHomeActivity = new ExistionOrder_Tab();
					Bundle args = new Bundle();
					args.putString("TableName", TableName);
					args.putString("OrderId", OrderId);
					existingOrderHomeActivity.setArguments(args);
					MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, existingOrderHomeActivity).commitAllowingStateLoss();

				} else if (pagenamee.contentEquals("BillTable")) {
					Bill_fragment bill_fragment = new Bill_fragment();
					Bundle args = new Bundle();
					args.putString("TableName", TableName);
					args.putString("OrderId", OrderId);
					bill_fragment.setArguments(args);
					MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, bill_fragment).commitAllowingStateLoss();

				} else {
					OrderDetailsActivity orderDetailsActivity = new OrderDetailsActivity();
					Bundle args = new Bundle();
					args.putString("TableName", TableName);
					args.putString("OrderId", OrderId);
					orderDetailsActivity.setArguments(args);
					MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, orderDetailsActivity).commitAllowingStateLoss();

				}

			}
		});
		if (pagenamee.contentEquals("ExistingOrder")||pagenamee.contentEquals("ViewOrder")) {
			String vchSplitStatus=table.get(position).getVchSplit_status();
			if(vchSplitStatus.contentEquals("Yes"))
			{
				txtTitle.setText(table.get(position).getTable_Name()+" ("+table.get(position).getVchSplitTableName()+")");
			}else
			{
				txtTitle.setText(table.get(position).getTable_Name());
			}

		}else
		{
			txtTitle.setText(table.get(position).getTable_Name());
		}

		TableName = table.get(position).getTable_Name();
		imageItem.setImageResource(R.mipmap.table);
		return rowView;
	}
	private class UpdateTAble extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(context);
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
			Intent intent=new Intent(context,People_Count_dialogBox.class);
			intent.putExtra("TableName",TableName);
			intent.putExtra("Pagename","SplitTable");
			intent.putExtra("SplitStatus","Yes");
			context.startActivity(intent);
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

				request.addProperty("command", "updateSplit");
				request.addProperty("OrderId", OrderId);
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