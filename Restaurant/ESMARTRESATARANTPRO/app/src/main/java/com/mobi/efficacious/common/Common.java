package com.mobi.efficacious.common;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.mobi.efficacious.database.DatabaseHandler;
//import com.mobi.efficacious.notifications.ServerUtilities;
import com.mobi.efficacious.webservice.Utility;
//import com.google.android.gcm.GCMRegistrar;

public class Common
{
	ConnectionDetector cd;
	DatabaseHandler dbhandler; 
	public static String strURL;
    Context mContext;
	private static final String SENDER_ID = "125366369787";
	Utility utility;
	AsyncTask<Void, Void, Void> mRegisterTask;

	public Common()
	{
		
	}
	
//	public void getGcmKey(final Context context)
//	{
//		final String regId = GCMRegistrar.getRegistrationId(context);
//	    if (regId.equals(""))
//	    {
//	        GCMRegistrar.register(context, SENDER_ID);
//	        GCMRegistrar.getRegistrationId(context);
//	        Toast.makeText(context," "+regId,2000).show();
//	    }
//	    else
//	    {
//	        mRegisterTask = new AsyncTask<Void, Void, Void>()
//	        {
//	            @Override
//	            protected Void doInBackground(Void... params)
//	            {
//	            	boolean registered =ServerUtilities.register(context, regId);
//	                if (!registered)
//	                {
//	                    //GCMRegistrar.unregister(context);
//	                }
//	                return null;
//	            }
//
//	            @Override
//	            protected void onPostExecute(Void result) {
//	                mRegisterTask = null;
//	            }
//	        };
//	            mRegisterTask.execute(null, null, null);
//	      }
//	  }
}
