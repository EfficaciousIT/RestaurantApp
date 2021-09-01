package com.mobi.efficacious.esmartrestaurant.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.fragment.KichenOrderDetailsActivity;
import com.mobi.efficacious.esmartrestaurant.fragment.Order_View;


public class Notifiacton extends AppCompatActivity {
    String value;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    String OrdeId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        settings = getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        FragmentManager mfragment = getSupportFragmentManager();
        Intent intent = getIntent();
            value = intent.getStringExtra("pagename");
        OrdeId=intent.getStringExtra("OrdeId");
            if(value.contentEquals("Kitchen"))
            {
                KichenOrderDetailsActivity kichenOrderDetailsActivity = new KichenOrderDetailsActivity();
                Bundle args = new Bundle();
                args.putString("OrderId",OrdeId);
                kichenOrderDetailsActivity.setArguments(args);
                mfragment.beginTransaction().replace(R.id.content_main, kichenOrderDetailsActivity).commitAllowingStateLoss();
            }
        if(value.contentEquals("Table"))
        {
            Order_View order_view = new Order_View();
            Bundle args = new Bundle();
            args.putString("OrderId",OrdeId);
            order_view.setArguments(args);
            mfragment.beginTransaction().replace(R.id.content_main, order_view).commitAllowingStateLoss();
        }

    }
}
