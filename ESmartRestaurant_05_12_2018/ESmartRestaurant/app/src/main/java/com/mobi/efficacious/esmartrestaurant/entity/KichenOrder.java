package com.mobi.efficacious.esmartrestaurant.entity;

public class KichenOrder {
 
	private String Order_Id; 
	private String Table_Name;
	private String vchSplitTableName;
	private String vchSplit_status;
	public KichenOrder()
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

	public String getVchSplitTableName() {
		return vchSplitTableName;
	}

	public void setVchSplitTableName(String vchSplitTableName) {
		this.vchSplitTableName = vchSplitTableName;
	}

	public String getVchSplit_status() {
		return vchSplit_status;
	}

	public void setVchSplit_status(String vchSplit_status) {
		this.vchSplit_status = vchSplit_status;
	}
}
