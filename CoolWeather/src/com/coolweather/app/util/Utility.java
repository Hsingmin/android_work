package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class Utility {
	/**
	 * Province Data Analyze and Disposal
	 */
	public synchronized static boolean handleProvinceResponse(CoolWeatherDB
			coolWeatherDB, String response){
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces = response.split(",");
			if(allProvinces != null && allProvinces.length > 0){
				for(String p : allProvinces){
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * City Data Analyze and Disposal
	 */
	public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,
			String response, int provinceId){
		if(!TextUtils.isEmpty(response)){
			String[] allCities = response.split(",");
			if(allCities != null && allCities.length > 0){
				for(String c : allCities){
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * County Data Analyze and Disposal
	 */
	public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,
			String response, int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] allCounties = response.split(",");
			if(allCounties != null && allCounties.length > 0){
				for(String c : allCounties){
					String[] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	} 
	
	public static void handleWeatherResponse(Context context, String response){
		try {
			Log.d("TAG", response);
			JSONObject jsonObject = new JSONObject(response);
			Log.d("TAG", jsonObject.toString());
			JSONObject dataObject = jsonObject.getJSONObject("data");
			Log.d("TAG", dataObject.toString());
			JSONArray jsonArray = dataObject.getJSONArray("forecast");
			JSONObject weatherInfo = jsonArray.getJSONObject(0);  //Get Current WeatherInfo
			String cityName = dataObject.getString("city");
			String windForce = weatherInfo.getString("fengli");
			String temp1 = weatherInfo.getString("low");
			String temp2 = weatherInfo.getString("high");
			String weatherDesp = weatherInfo.getString("type");
			String publishTime = weatherInfo.getString("date");
			saveWeatherInfo(context, cityName, windForce, temp1, temp2, weatherDesp, publishTime);
		} catch(JSONException e){
			e.printStackTrace();
		}
	}
	
	public static void saveWeatherInfo(Context context, String cityName,
			String windForce, String temp1, String temp2, String weatherDesp, String publishTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyƒÍM‘¬d»’", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("wind_force", windForce);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
	}
}






















