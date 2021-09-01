package com.mobi.efficacious.esmartresatarant.webservice;

import java.util.ArrayList;

public class Constants 
{
	public static final String strWEB_SERVICE_URL="http://e-smartrestaurant.com/ESmartRestaurantService/ESmartRestaurantService.asmx";
			//="http://e-smarts.net/sampleweservice/PushService.asmx";
			//"http://180.179.99.46/restaurant/Service.asmx";
	public static final String strNAMESPACE = "http://tempuri.org/";
			//"http://www.efficacious.co.in/";
	public static final String DATA_NOT_AVAILABELE = "Information not available.";
	public static final String INTERNET_NOT_AVAILABELE = "Internet connection not available.";
	public static final String ERROR_MESSAGE = "Error occured,Please try later ...";
	
	public static ArrayList<Object> Data=new ArrayList<Object>();
	
	public static final int RequestId=1;
	public static final int ResponceId=1;
	
	public static final int DataPresent=1;
	public static final int NoDataPresent=2;
	public static final int Error=3;
	public static final int AlredyExist=4;
	
}
