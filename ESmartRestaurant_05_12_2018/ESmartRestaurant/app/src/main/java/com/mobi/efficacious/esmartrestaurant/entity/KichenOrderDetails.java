package com.mobi.efficacious.esmartrestaurant.entity;

public class KichenOrderDetails {

	private String Id;
	private String Order_Id;
	private String Menu_Name;
	private String Qty;
	private String FoodCode;
	private String vchFoodDescription;
	public KichenOrderDetails()
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

	public String getMenu_Name() {
		return Menu_Name;
	}

	public void setMenu_Name(String menu_Name) {
		Menu_Name = menu_Name;
	}

	public String getQty() {
		return Qty;
	}

	public void setQty(String qty) {
		Qty = qty;
	}

	public String getFoodCode() {
		return FoodCode;
	}

	public void setFoodCode(String foodCode) {
		FoodCode = foodCode;
	}

	public String getVchFoodDescription() {
		return vchFoodDescription;
	}

	public void setVchFoodDescription(String vchFoodDescription) {
		this.vchFoodDescription = vchFoodDescription;
	}
}
