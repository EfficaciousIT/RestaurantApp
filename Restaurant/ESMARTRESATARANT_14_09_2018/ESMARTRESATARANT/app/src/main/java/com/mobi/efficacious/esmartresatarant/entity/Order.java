package com.mobi.efficacious.esmartresatarant.entity;

public class Order {

	private String Order_Id;
	private String Table_Name;
	private String Register_Id;

	public Order()
	{
		
	}
	
	public String getOrder_Id() {
		return Order_Id;
	}

	public void setOrder_Id(String order_Id) {
		Order_Id = order_Id;
	}
	
	public String getTable_Name() {
		return Table_Name;
	}

	public void setTable_Name(String table_Name) {
		Table_Name = table_Name;
	}

	public String getRegister_Id() {
		return Register_Id;
	}

	public void setRegister_Id(String register_Id) {
		Register_Id = register_Id;
	}

}
