package com.mobi.efficacious.model;

public class CustomerWiseOrder {

	private String Order_Id;
	private String Total;
	private String Created_Date;
	
	public CustomerWiseOrder()
	{
		
	}
	
	public String getOrder_Id() {
		return Order_Id;
	}

	public void setOrder_Id(String order_Id) {
		Order_Id = order_Id;
	}

	public String getTotal() {
		return Total;
	}

	public void setTotal(String total) {
		Total = total;
	}

	public String getCreated_Date() {
		return Created_Date;
	}

	public void setCreated_Date(String created_Date) {
		Created_Date = created_Date;
	}
}
