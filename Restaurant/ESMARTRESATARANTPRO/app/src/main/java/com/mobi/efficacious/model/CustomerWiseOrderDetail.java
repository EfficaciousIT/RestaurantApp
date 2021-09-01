package com.mobi.efficacious.model;

public class CustomerWiseOrderDetail {
 
	private String Order_Id;
	private String Cat_Name;
	private String Menu_Name;
	private String Price;
	private String Qty;
	private String Created_Date;
	
	public CustomerWiseOrderDetail()
	{
		
	}
	
	public String getOrder_Id() {
		return Order_Id;
	}

	public void setOrder_Id(String order_Id) {
		Order_Id = order_Id;
	}

	public String getCat_Name() {
		return Cat_Name;
	}

	public void setCat_Name(String cat_Name) {
		Cat_Name = cat_Name;
	}

	public String getMenu_Name() {
		return Menu_Name;
	}

	public void setMenu_Name(String menu_Name) {
		Menu_Name = menu_Name;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

	public String getQty() {
		return Qty;
	}

	public void setQty(String qty) {
		Qty = qty;
	}

	public String getCreated_Date() {
		return Created_Date;
	}

	public void setCreated_Date(String created_Date) {
		Created_Date = created_Date;
	}
}
