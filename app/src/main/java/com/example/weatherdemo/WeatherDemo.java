package com.example.weatherdemo;

import android.app.Fragment;
import android.app.TabActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherDemo<MainActivity> extends TabActivity {
	
	TextView firstday_temp;
	TextView firstday_wind;
	TextView city;
	
	TextView secondday_temp;
	TextView secondday_wind;
	
	TextView thirdday_temp;
	TextView thirdday_wind;
	
	TextView forthday_temp;
	TextView forthday_wind;
	
	EditText cityNameView;
	EditText refreshSpeedView;
	EditText KeyWordView;
	Button savebutton;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
//			Toast.makeText(WeatherDemo.this,"sgfdg",Toast.LENGTH_LONG).show();
			Bundle data = msg.getData();
	        String val = data.getString("value");
//	        setup_city.setText("��");
	        firstday_temp.setText("�¶ȣ�"+Weather.current_date_time+"����"+Weather.current_weather);
	        firstday_wind.setText("������"+Weather.current_wind);
	        city.setText(Weather.city);
	        
	        secondday_temp.setText(Weather.day[0].temperature+Weather.day[0].weather);
	        thirdday_temp.setText(Weather.day[1].temperature+Weather.day[1].weather);
	        forthday_temp.setText(Weather.day[2].temperature+Weather.day[2].weather);
		}
		
	};
	
//方法
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TabHost tabhost=getTabHost();
		
		LayoutInflater.from(this).inflate(R.layout.tab_weather, tabhost.getTabContentView(),true);
		
		LayoutInflater.from(this).inflate(R.layout.tab_history, tabhost.getTabContentView(),true);
		
		LayoutInflater.from(this).inflate(R.layout.tab_setup, tabhost.getTabContentView(),true);
		
		tabhost.addTab(tabhost.newTabSpec("TAB1").setIndicator("����Ԥ��").setContent(R.id.layout01));
		
		tabhost.addTab(tabhost.newTabSpec("TAB2").setIndicator("��ʷ����").setContent(R.id.layout02));
		
		tabhost.addTab(tabhost.newTabSpec("TAB3").setIndicator("ϵͳ����").setContent(R.id.layout03));
		
		firstday_temp=(TextView)findViewById(R.id.txt_temp);
		
		firstday_wind=(TextView)findViewById(R.id.txt_wind);
		
		city=(TextView)findViewById(R.id.txt_firstday);

		secondday_temp=(TextView)findViewById(R.id.txt_secondday);
		
		thirdday_temp=(TextView)findViewById(R.id.txt_thirdday);
		
		forthday_temp=(TextView)findViewById(R.id.txt_forthday);
		
		cityNameView=(EditText)findViewById(R.id.editText1);
		
		refreshSpeedView=(EditText)findViewById(R.id.editText2);
		
		KeyWordView=(EditText)findViewById(R.id.editText3);
		
		savebutton=(Button)findViewById(R.id.button1);
		
		savebutton.setOnClickListener(new OnClickListener(){
			
			public void onClick(View view){
				
				DBAdapter adapter=new DBAdapter(WeatherDemo.this);
				Config.CityName=cityNameView.getText().toString().trim();
				System.out.println(Config.CityName+"jkl");
				Config.RefreshSpeed=refreshSpeedView.getText().toString();
//				System.out.println(Config.RefreshSpeed);
				Config.KeyWord=KeyWordView.getText().toString().trim();
//				System.out.println(Config.KeyWord);
				adapter.open();
				adapter.SaveConfig();
				Thread thread = new Thread(run);
				thread.start();
				adapter.close();
			}
		});
		
//		setup_city=(EditText)findViewById(R.id.editText1);
		
	}
	
	Runnable run = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = new Message();
	        Bundle data = new Bundle();
			data.putString("value",WeatherAdaptor.getWeather(Config.CityName));		
	        msg.setData(data);
	        handler.sendMessage(msg);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.weather_demo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_weather_demo,
					container, false);
			return rootView;
		}
	}

}
