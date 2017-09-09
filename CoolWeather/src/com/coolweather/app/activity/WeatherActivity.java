package com.coolweather.app.activity;

import com.coolweather.app.R;
import com.coolweather.app.service.AutoUpdateService;
import com.coolweather.app.util.CityModelUtil;
import com.coolweather.app.util.JSONCallbackListener;
import com.coolweather.app.util.JSONUtil;
import com.coolweather.app.util.Utility;

//import android.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherActivity extends Activity implements OnClickListener {
	
	private LinearLayout weatherInfoLayout;
	
	/**
	 * City Name
	 */
	private TextView cityNameText;
	
	/**
	 * Publish Time
	 */
	private TextView publishText;
	
	/**
	 * Weather Description 
	 */
	private TextView weatherDespText;
	
	/**
	 * Temperature 1
	 */
	private TextView temp1Text;
	
	/**
	 * Temperature 2
	 */
	private TextView temp2Text;
	
	/**
	 * Current Date
	 */
	private TextView currentDateText;
	
	/**
	 * City Switch Button
	 */
	private Button switchCity;
	
	/**
	 * Refresh Weather
	 */
	private Button refreshWeather;
	
	/**
	 * countyCode Used for Weather Query
	 */
	private String countyCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		switchCity = (Button) findViewById(R.id.switch_city);  //weather_layout
		refreshWeather = (Button) findViewById(R.id.refresh_weather);  //weather_layout
		countyCode = getIntent().getStringExtra("county_code");
		Log.d("TAG", "queryWeatherInfo of countyCode = " + countyCode);
		if(!TextUtils.isEmpty(countyCode)){
			
			publishText.setText("同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherInfo(countyCode);
		} else{
			showWeather();
		}
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.switch_city:
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			publishText.setText("同步中...");
			
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			String cityName = prefs.getString("city_name", "");
			
			Toast.makeText(getApplicationContext(), "Refresh weather of " + cityName, Toast.LENGTH_LONG).show();
			
			String countyCode = CityModelUtil.queryCityCode(cityName);
			
			if(!TextUtils.isEmpty(countyCode)){
				queryWeatherInfo(countyCode);
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * Query Weather Info 
	 * @param cityName - Actually a County Name here
	 */
	private void queryWeatherInfo(String countyCode){
		String address = "http://wthrcdn.etouch.cn/weather_mini?citykey=" + countyCode;
		queryFromServer(address);
		Log.d("TAG", "queryFromServer : " + address);
	}
	
	private void queryFromServer(final String address){
		JSONUtil.sendJSONRequest(address, new JSONCallbackListener(){
			@Override
			public void onFinish(final String response){

					Utility.handleWeatherResponse(getBaseContext(), response);
					runOnUiThread(new Runnable(){
						@Override
						public void run(){
							showWeather();
						}
					});
				
			}
			
			@Override
			public void onError(Exception e){
				runOnUiThread(new Runnable(){
					@Override
					public void run(){
						publishText.setText("同步失败");
					}
				});
			}
		});
	}
	
	private void showWeather(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("今天" + prefs.getString("publish_time", "") + "发布");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		Intent intent = new Intent(this, AutoUpdateService.class);
		startService(intent);
	}
	
}























