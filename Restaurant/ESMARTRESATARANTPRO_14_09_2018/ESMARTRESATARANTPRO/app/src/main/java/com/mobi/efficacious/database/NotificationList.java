package com.mobi.efficacious.database;

public class NotificationList {

	//private variables
	int id;
	String name;
	String date;
	
	public NotificationList()
	{
		
	}
	
	// constructor
	public NotificationList(int id, String name, String date){
		this.id = id;
		this.name = name;
		this.date = date;
	}

	// getting id
	public int getId(){
		return this.id;
	}

	// setting id
	public void setId(int id){
		this.id = id;
	}

	// getting name
	public String getName(){
		return this.name;
	}

	// setting name
	public void setName(String name){
		this.name = name;
	}
	
	public String getDate(){
		return this.date;
	}
	
	public void setDate(String date){
		this.date = date;
	}
}
