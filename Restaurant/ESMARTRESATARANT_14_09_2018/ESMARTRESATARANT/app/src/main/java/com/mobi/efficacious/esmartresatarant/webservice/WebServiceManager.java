package com.mobi.efficacious.esmartresatarant.webservice;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class WebServiceManager 
{
	//Constants
//	private static  String SOAP_ACTION = "";
//	private static  String OPERATION_NAME = "GetAboutADSXML";
//	public static String strURL;
//	public static List<NameValuePair> ValuePair;
//	Context context;
//	int REQUST_ID;
//	Utility utility;
//	public ProgressDialog dialog;
//	public static String line;
//	SharedPreferences sharedpreferences;
//
//	public WebServiceManager(Context context,WebServiceListener webServiceListener)
//	{
//		this.context=context;
//		this.webServiceListener=webServiceListener;
//	}
//
//	public WebServiceManager(Context context)
//	{
//		this.context=context;
//	}
//
//	public WebServiceManager(String strurl,List<NameValuePair> ListValuePair,Context context)
//	{
//		strURL=strurl;
//		ValuePair=ListValuePair;
//		this.context = context;
//		dialog = new ProgressDialog(context);
//	}
//
//	WebServiceListener webServiceListener;
//
//	public void setonWebServiceListener(WebServiceListener webServiceListener)
//	{
//		this.webServiceListener=webServiceListener;
//	}
//
//	public void getLogin(final String UserName,final String Passwrod)
//	{
//		OPERATION_NAME = "StudentRegister";
//		SOAP_ACTION = Constants.strNAMESPACE+""+OPERATION_NAME;
//		REQUST_ID=2;
//		final Responce responce=new Responce();
//		Thread thread=new Thread(new Runnable()
//		{
//			SoapObject response;
//			@Override
//			public void run()
//			{
//				try
//				{
//					SoapObject request=new SoapObject(Constants.strNAMESPACE, OPERATION_NAME);
//					//add parameters to request
//					request.addProperty("UserName", UserName);
//					request.addProperty("Password", Passwrod);
//					request.addProperty("UserName", UserName);
//					request.addProperty("Password", Passwrod);
//
//					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//			        envelope.dotNet=true;
//					envelope.setOutputSoapObject(request);
//
//			        HttpTransportSE ht = new HttpTransportSE(Constants.strWEB_SERVICE_URL);
//			        ht.call(SOAP_ACTION, envelope);
//			        if(!(envelope.bodyIn instanceof SoapFault))
//			        {
//			        	if(envelope.bodyIn instanceof SoapObject)
//			        	response = (SoapObject)envelope.bodyIn;
//			        	if(response!=null)
//				        {
//		        		 	responce.Result_Type=Constants.DataPresent;
//		        		 	SoapPrimitive soapPrimitive=(SoapPrimitive) response.getProperty(0);
//						    responce.ResponceData= soapPrimitive.toString();
//						    Log.d("GetLogin Result", "------------------------");
//						    Log.d("GetLogin Result", ""+responce.ResponceData);
//					    }
//			        }
//			        else
//			        {
//			        	responce.Result_Type=Constants.Error;
//			        }
//		        }
//				catch (Exception e)
//				{
//					e.printStackTrace();
//					responce.Result_Type=Constants.Error;
//				}
//				finally
//		        {
//		        	Message message=new Message();
//		        	message.what=REQUST_ID;
//		        	message.arg1=responce.Result_Type;
//		        	message.obj=responce;
//		        	handler.sendMessage(message);
//		        }
//			}
//		});
//		thread.start();
//	}
//
//
//	boolean isValidProperty(SoapObject soapObject,String PropertyName)
//	{
//		if(soapObject!=null)
//		{
//			if(soapObject.getProperty(PropertyName) != null)
//	//		if(soapObject.hasProperty(PropertyName))
//			{
//				if(!soapObject.getProperty(PropertyName).toString().equalsIgnoreCase("")&&!soapObject.getProperty(PropertyName).toString().equalsIgnoreCase("anyType{}"))
//					return true;
//			else
//				return false;
//			}
//			return false;
//
//		}
//		else
//			return false;
//
//	}
//
//	final Handler handler=new Handler()
//	{
//		@Override
//		public void handleMessage(Message msg)
//		{
//			if(msg.arg1==Constants.DataPresent||msg.arg1==Constants.NoDataPresent)
//				webServiceListener.onProcesCompleted(REQUST_ID, msg.obj);
//			else if(msg.arg1==Constants.Error)
//				webServiceListener.onErrorOccured(REQUST_ID);
//		}
//	};
//
//	public interface WebServiceListener
//	{
//		public  abstract void onProcesCompleted(int requestId, Object Data);
//		public  abstract void onErrorOccured(int requestId);
//	}

}
