package com.mobi.efficacious.esmartresatarant.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public class Common 
{
	public static final String MyPREFERENCES ="MyPrefs";
	public static final String SchoolId ="null";
	public static final String UserTypeId ="null";
	public static final String UserType ="null";
	public static final String UserName ="null";
	public static final String Password ="null";
	public static final String UserId ="null";
	public static final String StudentId ="null";
	public static final String GcmKey ="null";
//	public static final String DeviceKey = "NULL"; 
	public static final String StandardId ="null";
	public static final String DivisionId ="null";
	
	public static final String Student_id ="null";
	public static final String Application_no ="null";
	public static final String Addmission_id ="null";
	public static final String Standerd_id ="null";
	public static final String StudentFirst_name ="null";
	public static final String StudentMiddle_name ="null";
	public static final String StudentLast_name ="null";
	public static final String FatherName ="null";
	public static final String MotherName ="null";
	public static final String ParentID ="null";
	public static final String Gender ="null";
	public static final String DOB ="null";
	public static final String PlaceofBirth ="null";
	public static final String User_name ="null";
	public static final String Pass ="null";
	public static final String Activestatus = "null";
	public static final String ActivationDate ="null";
	public static final String DeactivationDate ="null";
	public static final String ActivationBy ="null";
	public static final String DeactivationBy="null";
	public static final String Cast ="null";
	public static final String Subcast ="null";
	public static final String Email ="null";
	public static final String Mothertongue ="null";
	public static final String PresentAddress ="null";
	public static final String PermanentAddress ="null";
	public static final String ImageURL ="null";
	public static final String HomePhoneNo1 ="null";
	public static final String HomePhoneNo2 ="null";
	public static final String EmergencyConPerson1 ="null";
	public static final String EmergencyContat1 ="null";
	public static final String EmergencyConPerson2 ="null";
	public static final String EmergencyContat2 ="null";
	public static final String Neighbour_name ="null";
	public static final String NeighborConNo ="null";
	public static final String BloodGrp ="null";
	public static final String Passport ="null";
	public static final String Passportissue ="null";
	public static final String Expiredate ="null";
	public static final String HandicapedStatus ="null";
	public static final String Description="null";
	public static final String IdentificationMarks ="null";
	public static final String LeftStatus ="null";
	public static final String LeftDate ="null";
	public static final String LeftReason ="null";
	public static final String Division_id ="null";
	public static final String AcademicYear_id ="null";
	public static final String RollNo ="null";
	public static final String User_id ="null";
	public static final String School_id ="null";
	public static final String Session_id ="null";
	public static final String Active_flg ="null";
	public static final String Parent_id ="null";
	public static final String FatherFirst_name ="null";
	public static final String FatherMiddle_name ="null";
	public static final String FatherLast_name ="null";
	public static final String MotherFirst_name ="null";
	public static final String MotherMiddle_name ="null";
	public static final String MotherLast_name ="null";
	public static final String GuardianFirst_name ="null";
	public static final String GuardianMiddle_name ="null";
	public static final String GuardianLast_name ="null";
	public static final String FatherEmail ="null";
	public static final String MotherEmail ="null";
	public static final String GuardianEmail ="null";
	public static final String Address ="null";
	public static final String Permanent ="null";
	public static final String TelNo11 ="null";
	public static final String TelNo2 ="null";
	public static final String FatherMobile1 ="null";
	public static final String MotherMobile1 ="null";
	public static final String GuardianMobile ="null";
	public static final String FatherImageURL ="null";
	public static final String MotherImageURL ="null";
	public static final String GuardianImageURL ="null";
	public static final String FatherDBO ="null";
	public static final String MotherDBO ="null";
	public static final String MotherWorkingStatus ="null";
	public static final String FatherCompanyName ="null";
	public static final String FatherDesignation ="null";
	public static final String FatherCompAdd ="null";
	public static final String FatherTelNo ="null";
	public static final String FatherIncomeDet ="null";
	public static final String FatherHighestQualification ="null";
	public static final String GuardianCompanyName ="null";
	public static final String GuardianDesignation ="null";
	public static final String GuardianCompAdd ="null";
	public static final String GuardianTelNo ="null";
	public static final String GuardianIncomeDet ="null";
	public static final String GuardianHighestQualification ="null";
	public static final String MotherCompanyName ="null";
	public static final String MotherDesignation ="null";
	public static final String MotherCompAdd ="null";
	public static final String MotherTelNo ="null";
	public static final String MotherIncomeDet ="null";
	public static final String MotherHighestQualification ="null";
	public static final String Father_PAN ="null";
	public static final String Mother_PAN ="null";
	public static final String Guardian_PAN ="null";
	public static final String Father_BloodGrp ="null";
	public static final String Mother_BloodGrp ="null";
	public static final String Guardian_BloodGrp ="null";
	public static final String Father_VehicleNo ="null";
	public static final String Father_vehicleName ="null";
	public static final String Mother_VehicleNo ="null";
	public static final String Mother_vehicleName ="null";
	public static final String Guardian_VehicleNo ="null";
	public static final String Guardian_vehicleName ="null";
	public static final String FatherPassport_no ="null";
	public static final String MotherPassport_no ="null";
	public static final String GuardianPassport_no ="null";
	public static final String GrandFather_name ="null";
	public static final String GrandMother_name ="null";
	public static final String School_id1 ="null";
	public static final String GuardianDBO ="null";
	public static final String GuardianAddress ="null";
	public static final String Bus_alert_on1 ="null";
	public static final String Bus_alert_on2 ="null";
	public static final String Active_flg1 ="null";
	
	public static Boolean isPresentSDCard;
	static ProgressDialog progress_dialog;
	Handler handler=new Handler();
	public static final int DELAY=1000;
	static Context mcontext;

	public Common() 
	{
		// TODO Auto-generated constructor stub
	}
	
	public String encodeBase64(byte[] originalBytes) 
	{
		return new String(Base64.encode(originalBytes, 0));
	}

	public String decodeBase64(byte[] originalBytes) 
	{
		return new String(Base64.decode(originalBytes, 0));
	}

	public void sendSms(Context context,String smsBody)
	{
		try 
		{
			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			sendIntent.putExtra("sms_body", smsBody); 
			sendIntent.setType("vnd.android-dir/mms-sms");
			context.startActivity(sendIntent);      
		}
		catch (Exception e) 
		{
			Toast.makeText(context,"SMS faild, please try again later!",Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
	
	public void sendMail(Context context, String mailBody)
	{
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_TEXT, mailBody);
		email.setType("message/rfc822");
		context.startActivity(Intent.createChooser(email, "Choose an Email client :"));
	}
	
	public void sendNoteMail(Context context, String mailBody, String mailsubject)
	{
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_TEXT, mailBody);
		email.putExtra(Intent.EXTRA_SUBJECT, mailsubject);
		email.setType("message/rfc822");
		context.startActivity(Intent.createChooser(email, "Choose an Email client :"));
	}

	public void callEmergency(Context context)
	{
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:911"));
		callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		context.startActivity(callIntent);
	}

	public void callPerson(Context context,String number)
	{
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:"+number));
		callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		context.startActivity(callIntent);
	}

	public static Bitmap decodeFile(String src)
	{
		//Decode image size
		try
		{
			Log.e("src",src);
			File file=new File(src);
			FileInputStream fileInputStream;
			BitmapFactory.Options o = new BitmapFactory.Options();
			
			fileInputStream=new FileInputStream(file);
			BitmapFactory.decodeStream(fileInputStream,null,o);

			final int REQUIRED_SIZE = 50;

			//Find the correct scale value. It should be the power of 2.
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale=1;
			while (true) 
			{
		        if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
		            break;
		        }
		        width_tmp /= 2;
		        height_tmp /= 2;
		        scale *= 2;
		    }
			
			//Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inJustDecodeBounds = false;
			o2.inSampleSize=scale;
			o2.inTempStorage = new byte[16 * 1024];
			fileInputStream.close();

			fileInputStream=new FileInputStream(file);
			Bitmap b = BitmapFactory.decodeStream(fileInputStream, null, o2);

			return b;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			Log.e("Exception",e.getMessage());
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.e("Exception",e.getMessage());
			return null;
		}
		catch (OutOfMemoryError e) 
		{
			e.printStackTrace();
			Log.e("Exception",e.getMessage());
			return null;
		}
	}

	public static Bitmap getBitmapFromURL(String src) 
	{
		try
		{
			Log.d("src",src);
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();

			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			input.close();
			return myBitmap;
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public boolean canCallSms(Context context)
	{
		boolean isCall = false;
		TelephonyManager telMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		int simState = telMgr.getSimState();
		switch (simState) {
		case TelephonyManager.SIM_STATE_ABSENT:
			isCall=false;
			break;
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			// do something
			break;
		case TelephonyManager.SIM_STATE_PIN_REQUIRED:

			break;
		case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			// do something
			break;
		case TelephonyManager.SIM_STATE_READY:
			isCall=true;
			break;
		case TelephonyManager.SIM_STATE_UNKNOWN:
			isCall=false;
			break;
		}
		return isCall;
	}

//	public static float megabytesAvailable(File f)
//	{
//		StatFs stat = new StatFs(f.getPath());
//		long bytesAvailable = stat.getAvailableBlocksLong() * stat.getAvailableBlocksLong();
//		return bytesAvailable / (1024.f * 1024.f);
//	}
	
	public void sendMailWithImages(Context mContext,File file)
	{	
		String subject ="Images";
		ArrayList<Uri> uris = new ArrayList<Uri>();

		Intent messageIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
		messageIntent.setType("text/plain");

		messageIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

		Uri u = Uri.fromFile(file);
		uris.add(u);

		messageIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

		try
		{
			mContext.startActivity(Intent.createChooser(messageIntent, "Choose an e-mail client:"));
		}
		catch(android.content.ActivityNotFoundException ex)
		{
			Toast.makeText(mContext, "E-mail client is not configured", Toast.LENGTH_SHORT).show();
		}
	}

	public void sendMMS(Context mContext)
	{
		String Subject ="Images";

		ArrayList<String> filePath=new ArrayList<String>();
		File filePathWithFolder=new File(Environment.getExternalStorageDirectory()+"/DpApp");
		File[] full=filePathWithFolder.listFiles();

		Intent sendIntent = new Intent(Intent.ACTION_SEND); 
		sendIntent.putExtra("sms_body", Subject); 

		if(full==null)
		{}else if(full.length==0){}
		else
		{
			for(File fileName:full)
			{
				filePath.add(fileName.getAbsolutePath());
			}
		}

		for (String file : filePath)
		{
			File fileIn = new File(file);
			Uri u = Uri.fromFile(fileIn);
			sendIntent.putExtra(Intent.EXTRA_STREAM, u);
		}
		sendIntent.setType("vnd.android-dir/mms-sms");
		sendIntent.setType("image/png"); 
		mContext.startActivity(sendIntent);
	}

	public void sendMailWithRecord(Context mContext,File file )
	{
		String subject ="Recording";
		ArrayList<Uri> uris = new ArrayList<Uri>();

		Intent messageIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
		messageIntent.setType("audio/rfc822");

		messageIntent.putExtra(Intent.EXTRA_EMAIL, subject);

		Uri u = Uri.fromFile(file);
		uris.add(u);

		messageIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

		try
		{
			mContext.startActivity(Intent.createChooser(messageIntent, "Choose an e-mail client:"));
		}
		catch(android.content.ActivityNotFoundException ex)
		{
			Toast.makeText(mContext, "E-mail client is not configured", Toast.LENGTH_SHORT).show();
		}
	} 

	public void sendRecordMMS(Context mContext)
	{
		String Subject ="Recording";

		ArrayList<String> filePath=new ArrayList<String>();
		File filePathWithFolder=new File(Environment.getExternalStorageDirectory()+"/DPpro_recording");
		File[] full=filePathWithFolder.listFiles();

		Intent sendIntent = new Intent(Intent.ACTION_SEND); 
		sendIntent.putExtra("sms_body", Subject); 

		if(full==null)
		{}else if(full.length==0){}
		else
		{
			for(File fileName:full)
			{
				filePath.add(fileName.getAbsolutePath());
			}
		}

		for (String file : filePath)
		{
			File fileIn = new File(file);
			Uri u = Uri.fromFile(fileIn);
			sendIntent.putExtra(Intent.EXTRA_STREAM, u);
		}
		sendIntent.setType("vnd.android-dir/mms-sms");
		sendIntent.setType("audio/4gp"); 
		mContext.startActivity(sendIntent);
	}
	
	public void setDialogBoxMetric(Dialog dialog,Context context)
	{
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.show();
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int height = metrics.heightPixels;
		int width = metrics.widthPixels;
		//width=width-(width*10/100);
		dialog.getWindow().setLayout(width, height);
	}
	
    static void encrypt() throws IOException, NoSuchAlgorithmException,NoSuchPaddingException, InvalidKeyException 
    {
		// Here you read the cleartext.
		File extStore = Environment.getExternalStorageDirectory();
		FileInputStream fis = new FileInputStream(extStore + "/sampleFile");
		// This stream write the encrypted text. This stream will be wrapped by
		// another stream.
		FileOutputStream fos = new FileOutputStream(extStore + "/encrypted");
		
		// Length is 16 byte
		SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),
		            "AES");
		// Create cipher
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, sks);
		// Wrap the output stream
		CipherOutputStream cos = new CipherOutputStream(fos, cipher);
		// Write bytes
		int b;
		byte[] d = new byte[8];
		while ((b = fis.read(d)) != -1) 
		{
		      cos.write(d, 0, b);
		}
		// Flush and close streams.
		cos.flush();
		cos.close();
		fis.close();
	}

	static void decrypt() throws IOException, NoSuchAlgorithmException,NoSuchPaddingException, InvalidKeyException 
	{
		File extStore = Environment.getExternalStorageDirectory();
		FileInputStream fis = new FileInputStream(extStore + "/encrypted");
		FileOutputStream fos = new FileOutputStream(extStore + "/decrypted");
		SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, sks);
		CipherInputStream cis = new CipherInputStream(fis, cipher);
		int b;
		byte[] d = new byte[8];
		while ((b = cis.read(d)) != -1) 
		{
		   fos.write(d, 0, b);
		}
		fos.flush();
		fos.close();
		cis.close();
	}
	
	public void showProgreessDialog()
    { 		
		try 
		{
			progress_dialog=new ProgressDialog(mcontext);
			progress_dialog.setMessage("Loading please wait..");
			progress_dialog.setCancelable(true);		
			handler.postDelayed(new Runnable() 
			{
				@Override
				public void run() 
				{
					 if(!progress_dialog.isShowing())
				        	progress_dialog.show();
				}
			}, DELAY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void dismiss()
	{
		try 
		{
			handler.postDelayed(new Runnable() 
			{
				@Override
				public void run() 
				{
					if(progress_dialog!=null)
					{
						if(progress_dialog.isShowing())
						progress_dialog.dismiss();	
						progress_dialog=null;
					}
				}
			}, DELAY);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
}
