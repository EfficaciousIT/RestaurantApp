package com.mobi.efficacious.esmartresatarant.restaurentapp;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.mobi.efficacious.esmartresatarant.R;

public class MainActivity extends Activity {
	TextView SignIn;
	TextView SignUp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title_home);
		SignIn = (TextView)findViewById(R.id.xtxtSignin_main);
		SignUp = (TextView)findViewById(R.id.xtxtSignup_main);
		SignUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent MainScreenIntent=new Intent(MainActivity.this, SignUpActivity.class);
				startActivity(MainScreenIntent);
				finish();
			}
		});
		
		SignIn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent MainScreenIntent=new Intent(MainActivity.this, LoginActivity.class);
				startActivity(MainScreenIntent);	
				finish();
			}
		});
	}
}
