package com.mobi.efficacious.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "Profile";
	private static final String TABLE_NOTIFICATION = "NotificationTable";
	
	private static final String KEY_ID = "_id";
	private static final String KEY_NOTIFICATION = "notofication";
	private static final String KEY_DATE = "date";
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	//Create Table
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
		onCreate(db);
	}
  
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String SQL = "CREATE TABLE " + TABLE_NOTIFICATION + "("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NOTIFICATION + " VARCHAR," + KEY_DATE + " TEXT )";
		db.execSQL(SQL);
			
	}
	
	public void addnotifcationdata(Context context, String name, String date) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NOTIFICATION, name);
		values.put(KEY_DATE, date);
		db.insert(TABLE_NOTIFICATION, null, values);
		db.close();
	}
	
	public int getCount() 
	{
		String countQuery = "SELECT * FROM " + TABLE_NOTIFICATION;
		int count = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		if(cursor != null && !cursor.isClosed()){
			count = cursor.getCount();
			cursor.close();
		} 
		return count;
	}
	
	public List<NotificationList> getNoteList(){
		String selectQuery = "SELECT * FROM " + TABLE_NOTIFICATION + " ORDER BY " + KEY_DATE +" desc";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		List<NotificationList> NoteList = new ArrayList<NotificationList>();
		if (cursor.moveToFirst()) {
			do 
			{
				NotificationList list = new NotificationList();
				list.setId(Integer.parseInt(cursor.getString(0)));
				list.setName(cursor.getString(1));
				list.setDate(cursor.getString(2));
				NoteList.add(list);
			} while (cursor.moveToNext());
		}
		return NoteList;
	}
}
