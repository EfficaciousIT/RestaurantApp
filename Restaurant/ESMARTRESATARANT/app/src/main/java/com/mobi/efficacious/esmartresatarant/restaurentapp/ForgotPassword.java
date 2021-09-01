package com.mobi.efficacious.esmartresatarant.restaurentapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.mobi.efficacious.esmartresatarant.R;
import com.mobi.efficacious.esmartresatarant.webservice.Utility;


public class ForgotPassword extends Activity
{
	EditText Mobile;
	EditText Email;
	Button btnRegister,btnCancel;
	Utility utility;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_forgotpassword);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title_forgetpassword);
		Mobile=(EditText)findViewById(R.id.xedtMobile_forgot);
		Email=(EditText)findViewById(R.id.xedtEmail_forgot);
		btnRegister=(Button)findViewById(R.id.xbtnRegister_forgot);
		btnCancel=(Button)findViewById(R.id.xbtnCancel_forgot);
		
	}
}
