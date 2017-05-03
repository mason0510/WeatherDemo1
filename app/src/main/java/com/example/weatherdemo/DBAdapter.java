package com.example.weatherdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBAdapter {
	
	
	private static final String DB_NAME="weather_app.db";
	private static final String DB_TABLE_CONFIG="setup_config";
	private static final String DB_CONFIG_ID="1";
	private static final int DB_VERSION=1;
	
	public static final String KEY_ID="_id";
	public static final String KEY_CITY_NAME="city_name";
	public static final String KEY_REFRESH_SPEED="refresh_speed";
	public static final String KEY_SMS_SERVICE="sms_service";
	public static final String KEY_SMS_INFO="sms_info";
	public static final String KEY_KEY_WORD="key_word";
	
	private static final String DB_TABLE_SMS="sms_data";
	public static final String KEY_SENDER="sms_sender";
	public static final String KEY_BODY="sms_body";
	public static final String KEY_RECEIVE_TIME="sms_receive_time";
	public static final String KEY_RETURN_RESULT="return_result";
	
	private static SQLiteDatabase db;
	private DBOpenHelper dbOpenHelper;
	private static Context context;
	

	public DBAdapter(Context context){
		this.context = context;
	}
	
	public void open() throws SQLiteException{
		dbOpenHelper = new DBOpenHelper(context, DB_NAME, null, DB_VERSION);
		try{
			db = dbOpenHelper.getWritableDatabase();
		}catch(SQLiteException e){
			db = dbOpenHelper.getReadableDatabase();
		}
	}
	
	public void close() {
		 if (db != null){
		  //关闭数据库
		   db.close();
		   db = null;
		 }
	}
	
	public void SaveConfig(){
		ContentValues updateValues = new ContentValues();
		updateValues.put(KEY_CITY_NAME, Config.CityName);
		updateValues.put(KEY_REFRESH_SPEED,Config.RefreshSpeed);
		updateValues.put(KEY_SMS_SERVICE,Config.ProvideSmsService);
		updateValues.put(KEY_SMS_INFO,Config.SaveSmsInfo);
		updateValues.put(KEY_KEY_WORD,Config.KeyWord);
		db.update(DB_TABLE_CONFIG, updateValues, KEY_ID+"="+DB_CONFIG_ID, null);
		Toast.makeText(context, "系统保存设置成功！", Toast.LENGTH_LONG).show();
	}
	
	private static class DBOpenHelper extends SQLiteOpenHelper{
		public DBOpenHelper(Context context,String name,CursorFactory factory,int version){
			super(context,name,factory,version);
		}
		
		private static final String DB_CREATE_CONFIG = "create table "+DB_TABLE_CONFIG
				+"("+KEY_ID+" integer primary key autoincrement, "
				+KEY_CITY_NAME+" text not null, "
				+KEY_REFRESH_SPEED+" text, "+KEY_SMS_SERVICE+" text, "
				+KEY_SMS_INFO+" text, "+KEY_KEY_WORD+" text);";
		
		private static final String DB_CREATE_SMS = "create table "+DB_TABLE_SMS+"("
				+KEY_ID+" integer primary key autoincrement, "
				+KEY_SENDER+" text not null, "+KEY_BODY+" text, "
				+KEY_RECEIVE_TIME+" text, "+KEY_RETURN_RESULT+" text);";

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(DB_CREATE_CONFIG);
			db.execSQL(DB_CREATE_SMS);
			Config.LoadDefaultConfig();
			ContentValues newValues=new ContentValues();
			newValues.put(KEY_CITY_NAME, Config.CityName);
			newValues.put(KEY_REFRESH_SPEED,Config.RefreshSpeed);
			newValues.put(KEY_SMS_SERVICE,Config.ProvideSmsService);
			newValues.put(KEY_SMS_INFO,Config.SaveSmsInfo);
			newValues.put(KEY_KEY_WORD,Config.KeyWord);
			db.insert(DB_TABLE_CONFIG, null, newValues);
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int arg1, int arg2) {
			// TODO Auto-generated method stub
			_db.execSQL("Drop table if exists "+DB_TABLE_CONFIG);
			_db.execSQL("Drop table if exists "+DB_CREATE_SMS);
			onCreate(_db);
		}
		
//		public void SaveConfig(){
//			ContentValues updateValues = new ContentValues();
//			updateValues.put(KEY_CITY_NAME, Config.CityName);
//			updateValues.put(KEY_REFRESH_SPEED,Config.RefreshSpeed);
//			updateValues.put(KEY_SMS_SERVICE,Config.ProvideSmsService);
//			updateValues.put(KEY_SMS_INFO,Config.SaveSmsInfo);
//			updateValues.put(KEY_KEY_WORD,Config.KeyWord);
//			db.update(DB_TABLE_CONFIG, updateValues, KEY_ID+"="+DB_CONFIG_ID, null);
//			Toast.makeText(context, "系统保存设置成功！", Toast.LENGTH_LONG).show();
//		}
//		public void LoadConfig(){
//			Cursor result = db.query(DB_TABLE_CONFIG, new String[]{
//					KEY_ID,KEY_CITY_NAME,KEY_REFRESH_SPEED,KEY_SMS_SERVICE,
//					KEY_SMS_INFO,KEY_KEY_WORD}, KEY_ID+"="+DB_CONFIG_ID,
//					null,null,null,null);
//			if(result.getCount() == 0 || !result.moveToFirst()){
//				return;
//			}
//			Config.CityName = result.getString(result.getColumnIndex(KEY_CITY_NAME));
//			Config.RefreshSpeed = result.getString(result.getColumnIndex
//					(KEY_REFRESH_SPEED));
//			Config.ProvideSmsService = result.getString(result.getColumnIndex
//					(KEY_SMS_SERVICE));
//			Config.SaveSmsInfo = result.getString(result.getColumnIndex(KEY_SMS_INFO));
//			Config.KeyWord = result.getString(result.getColumnIndex(KEY_KEY_WORD));
//			Toast.makeText(context, "系统读取设置成功", Toast.LENGTH_LONG).show();
//		}
		
//		public void SaveOneSms(SimpleSms sms){
//			ContentValues newValues = new ContentValues();
//			newValues.put(KEY_SENDER, sms.sender);
//			newValues.put(KEY_BODY,sms.body);
//			newValues.put(KEY_RECEIVE_TIME,sms.receiveTime);
//			newValues.put(KEY_RETURN_RESULT,sms.returnResults);
//			db.insert(DB_TABLE_SMS, null, newValues);
//		}
		
//		public long deleteAllSms(){
//			return db.delete(DB_TABLE_SMS, null, null);
//		}
//		
//		public SimpleSms[] GetAllSms(){
//			Cursor results = db.query(DB_TABLE_SMS,new String[]{
//			KEY_ID,KEY_SENDER,KEY_RECEIVE_TIME,KEY_RETURN_RESULT},
//			null,null,null,null,null);
//			return toSimpleSms(results);	
//		}

		private SimpleSms[] toSimpleSms(Cursor results) {
			// TODO Auto-generated method stub
			int resultCounts = results.getCount();
			if(resultCounts == 0 || results.moveToFirst()){
				return null;
			}
			SimpleSms[] sms = new SimpleSms[resultCounts];
			for(int i = 0;i<resultCounts;i++){
				sms[i] = new SimpleSms();
				sms[i].sender = results.getString(results.getColumnIndex(KEY_SENDER));
				sms[i].body =  results.getString(results.getColumnIndex(KEY_BODY));
				sms[i].receiveTime =  results.getString(results.getColumnIndex(KEY_RECEIVE_TIME));
				sms[i].returnResults =  results.getString(results.getColumnIndex(KEY_RETURN_RESULT));
				results.moveToNext();
			}
			return sms;
		}
	}

	
}
