package com.mobi.efficacious.esmartrestaurant.entity;

public class Menus {

	private String Menu_Id;
	private String Menu_Name;
	private String Res_id;
	private String Is_Active;
	private String Cat_Id;
	private String Price;
	private boolean checked;
	private String qty;
	public Menus()
	{
		
	}
	
	public String getMenu_Id() {
		return Menu_Id;
	}

	public void setMenu_Id(String menu_Id) {
		Menu_Id = menu_Id;
	}

	public String getMenu_Name() {
		return Menu_Name;
	}

	public void setMenu_Name(String menu_Name) {
		Menu_Name = menu_Name;
	}

	public String getRes_id() {
		return Res_id;
	}

	public void setRes_id(String res_id) {
		Res_id = res_id;
	}

	public String getIs_Active() {
		return Is_Active;
	}

	public void setIs_Active(String is_Active) {
		Is_Active = is_Active;
	}

	public String getCat_Id() {
		return Cat_Id;
	}

	public void setCat_Id(String cat_Id) {
		Cat_Id = cat_Id;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}



	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
