package com.mobi.efficacious.restaurant;

import org.ksoap2.serialization.SoapObject;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.mobi.efficacious.common.ConnectionDetector;

public class SettingDialog extends DialogFragment{
	View viewContext=null;
	Context mContext;
	Button cancel;
	ImageView Logout;
	ImageView Notification;
	
	private static final String PREFRENCES_NAME = "myprefrences";
	private static  String SOAP_ACTION = "";	 
	private static  String OPERATION_NAME = "GetAboutADSXML";	 
	public static String strURL;
	SharedPreferences settings;
	ConnectionDetector cd;
	
	public SettingDialog()
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		viewContext = inflater.inflate(R.layout.dialog_setting, container);
		settings = getActivity().getSharedPreferences(PREFRENCES_NAME,Context.MODE_PRIVATE);
		cd = new ConnectionDetector(getActivity());
		init();
		return viewContext;
	}
	
	public void init()
	{
		cancel = (Button)viewContext.findViewById(R.id.xbtnCancel_editageDialog);
		cancel.setOnClickListener(onClick_cancel);
		Logout = (ImageView)viewContext.findViewById(R.id.LogOutImageview_settingDialog);
		Logout.setOnClickListener(onClick_logout);
		Notification = (ImageView)viewContext.findViewById(R.id.NotificationImageview_settingDialog);
		Notification.setOnClickListener(onClick_notification);
	}
	
	private android.view.View.OnClickListener onClick_cancel=new View.OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			 dismiss();
		}
	};
	
	private android.view.View.OnClickListener onClick_logout=new View.OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			settings.edit().putString("TAG_REGISTERID", "").commit();
			System.exit(0);
		}
	};
	
	private android.view.View.OnClickListener onClick_notification=new View.OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			// dismiss();
		}
	};
	
	
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
