package com.example.weatherdemo;

public class Config {
	
	public static String CityName;
	public static String RefreshSpeed;
	public static String ProvideSmsService;
	public static String SaveSmsInfo;
	public static String KeyWord;
	//抽象 继承 多多态
	public static void LoadDefaultConfig(){
		CityName="镇江";
		RefreshSpeed="60";
		ProvideSmsService="true";
		SaveSmsInfo="true";
		KeyWord="NY";
	}
}
