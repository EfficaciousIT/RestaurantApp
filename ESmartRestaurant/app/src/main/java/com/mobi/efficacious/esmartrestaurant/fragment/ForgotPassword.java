package com.mobi.efficacious.esmartrestaurant.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.activity.LoginActivity;
import com.mobi.efficacious.esmartrestaurant.webservice.Utility;


public class ForgotPassword extends Fragment {
	EditText Mobile;
	EditText Email;
	Button btnRegister,btnCancel;
	Utility utility;
	View myview;
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		myview=inflater.inflate(R.layout.activity_forgotpassword,null);
		myview.setFocusableInTouchMode(true);
		myview.requestFocus();
		myview.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						try {
							Intent intent=new Intent(getActivity(),LoginActivity.class);
							startActivity(intent);
						} catch (Exception ex) {

						}

						return true;
					}
				}
				return false;
			}
		});
		Mobile=(EditText)myview.findViewById(R.id.xedtMobile_forgot);
		Email=(EditText)myview.findViewById(R.id.xedtEmail_forgot);
		btnRegister=(Button)myview.findViewById(R.id.xbtnRegister_forgot);
		btnCancel=(Button)myview.findViewById(R.id.xbtnCancel_forgot);
		return myview;
	}
}
