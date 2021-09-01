package com.mobi.efficacious.restaurant;

import java.util.ArrayList;
import java.util.List;

import com.mobi.efficacious.adapter.NotificationsListAdapter;
import com.mobi.efficacious.database.DatabaseHandler;
import com.mobi.efficacious.database.NotificationList;

import android.app.Activity;
import android.app.Notification;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NotificationFragment extends Activity{
	 ListView listview;
	 private List<Notification> notificationList = null;
     ArrayAdapter<NotificationList> noteadapter;
	 DatabaseHandler db;
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.notification_layout);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title_login);
		listview = (ListView)findViewById(R.id.notificationlistView);
		db = new DatabaseHandler(this);
		List<NotificationList> notelist = db.getNoteList();
		noteadapter = new NotificationsListAdapter(this, R.layout.screen_list,(ArrayList<NotificationList>) notelist);
		listview.setAdapter(noteadapter);
		
	 }
}
