package com.mobi.efficacious.esmartrestaurant.entity;

public class Tables {

	private String Table_Id;
	private String Table_Name;
	private boolean meregeTableChkbx;
	public Tables()
	{
		
	}
	
	public String getTable_Id() {
		return Table_Id;
	}

	public void setTable_Id(String table_Id) {
		Table_Id = table_Id;
	}

	public String getTable_Name() {
		return Table_Name;
	}

	public void setTable_Name(String table_Name) {
		Table_Name = table_Name;
	}


	public boolean isMeregeTableChkbx() {
		return meregeTableChkbx;
	}

	public void setMeregeTableChkbx(boolean meregeTableChkbx) {
		this.meregeTableChkbx = meregeTableChkbx;
	}
}
