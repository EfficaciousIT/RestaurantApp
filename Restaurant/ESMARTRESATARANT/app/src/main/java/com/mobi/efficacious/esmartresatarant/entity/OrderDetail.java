package com.mobi.efficacious.esmartresatarant.entity;

public class OrderDetail {
	private String Id;
	private String Order_Id;
	private String Cat_Name;
	private String Menu_Name;
	private String Price;
	private String Table_Name;
	private String Qty;
	private String Register_Id;

	public OrderDetail()
	{
		
	}
	
	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
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

	public String getTable_Name() {
		return Table_Name;
	}

	public void setTable_Name(String table_Name) {
		Table_Name = table_Name;
	}

	public String getQty() {
		return Qty;
	}

	public void setQty(String qty) {
		Qty = qty;
	}
	
	public String getRegister_Id() {
		return Register_Id;
	}

	public void setRegister_Id(String register_Id) {
		Register_Id = register_Id;
	}

}
