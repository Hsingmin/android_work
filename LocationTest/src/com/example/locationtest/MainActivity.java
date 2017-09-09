package com.example.locationtest;

import android.app.Activity;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final int SHOW_LOCATION = 0;
	private TextView positionTextView;
	private LocationManager locationManager;
	private String provider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		positionTextView = (TextView) findViewById(R.id.position_text_view);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		List<String> providerList = locationManager.getProviders(true);
		if(providerList.contains(LocationManager.GPS_PROVIDER)){
			provider = LocationManager.GPS_PROVIDER;
		} else if(providerList.contains(LocationManager.NETWORK_PROVIDER)){
			provider = LocationManager.NETWORK_PROVIDER;
		} else{
			Toast.makeText(this, "No Location Provider to USE", Toast.LENGTH_LONG).show();
			return ;
		}
		Location location = locationManager.getLastKnownLocation(provider);
		if(location != null){
			showLocation(location);
		}
		locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
	}
	
	protected void onDestroy(){
		super.onDestroy();
		if(locationManager != null){
			locationManager.removeUpdates(locationListener);
		}
	}
	
	LocationListener locationListener = new LocationListener(){
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras){
			
		}
		
		@Override
		public void onProviderEnabled(String provider){
			
		}
		
		@Override
		public void onProviderDisabled(String provider){
			
		}
		
		@Override
		public void onLocationChanged(Location location){
			showLocation(location);
		}
	};
	
	private void showLocation(final Location location){
		new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					StringBuilder url = new StringBuilder();
					url.append("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
					url.append(location.getLatitude()).append(",");
					url.append(location.getLongitude());
					url.append("&sensor=false");
					HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(url.toString());
					httpGet.addHeader("Accept-Language", "zh-CN");
					HttpResponse httpResponse = httpClient.execute(httpGet);
					if(httpResponse.getStatusLine().getStatusCode() == 200){
						HttpEntity entity = httpResponse.getEntity();
						String response = EntityUtils.toString(entity, "utf-8");
						JSONObject jsonObject = new JSONObject(response);
						JSONArray resultArray = jsonObject.getJSONArray("result");
						if(resultArray.length() > 0){
							JSONObject subObject = resultArray.getJSONObject(0);
							String address = subObject.getString("formatted_address");
							Message message = new Message();
							message.what = SHOW_LOCATION;
							message.obj = address;
							handler.sendMessage(message);
						}
					}
				} catch(Exception e){
					
				}
			}
		}).start();
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case SHOW_LOCATION:
				String currentPosition = (String) msg.obj;
				positionTextView.setText(currentPosition);
				break;
			default:
				break;
			}
		}
	};
}





















