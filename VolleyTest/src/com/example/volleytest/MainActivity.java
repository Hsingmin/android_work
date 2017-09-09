package com.example.volleytest;

import android.app.Activity;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{

	public static final int SHOW_RESPONSE = 0;
	private Button sendHttpRequest;
	private Button sendJSONRequest;
	private Button sendCityCodeRequest;
	private Button loadCityCodeFile;
	private Button queryCityCode;
	private TextView responseText;
	private RequestQueue mQueue;
	private int what;
	private Object obj;
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case SHOW_RESPONSE:
				String uiResponse = (String) msg.obj;
				responseText.setText(uiResponse);
			}
		}
	};
	
	private Message msg = handler.obtainMessage(what, obj);
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sendHttpRequest = (Button) findViewById(R.id.send_http_request);
		sendJSONRequest = (Button) findViewById(R.id.send_json_request);
		sendCityCodeRequest = (Button) findViewById(R.id.send_citycode_request);
		loadCityCodeFile = (Button) findViewById(R.id.load_citycode);
		queryCityCode = (Button) findViewById(R.id.query_citycode);
		responseText = (TextView) findViewById(R.id.response);
		mQueue = Volley.newRequestQueue(VolleyApplication.getContext());
		sendHttpRequest.setOnClickListener(this);
		sendJSONRequest.setOnClickListener(this);
		sendCityCodeRequest.setOnClickListener(this);
		loadCityCodeFile.setOnClickListener(this);
		queryCityCode.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.send_http_request:
			sendHttpRequestWithVolley();
			break;
		case R.id.send_json_request:
			sendJSONRequestWithVolley();
			break;
		case R.id.send_citycode_request:
			sendCityCodeRequest();
			break;
		case R.id.load_citycode:
			JSONUtil.load("citymodel.txt");
			JSONUtil.JSONGenerate();
		case R.id.query_citycode:
			JSONUtil.queryCityName("101110312");
			JSONUtil.queryCityCode("¼ÃÄÏ");
			break;
		default:
			break;
		}
	}
	
	private void sendHttpRequestWithVolley(){
		new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					
					StringRequest stringRequest = new StringRequest(
							"http://wthrcdn.etouch.cn/weather_mini?citykey=101020800",
							new Response.Listener<String>() {
								@Override
								public void onResponse(String response){
									Log.d("TAG", response);
								}
							}, new Response.ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error){
									Log.e("TAG", error.getMessage(), error);
								}
							
							});
					
					mQueue.add(stringRequest);
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void sendJSONRequestWithVolley(){
		new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					
					JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
							"http://wthrcdn.etouch.cn/weather_mini?citykey=101020800", null,
							new Response.Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response){
									Log.d("TAG", response.toString());
									
									parseJSONData((String) (response.toString()));
								}
							}, new Response.ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error){
									Log.e("TAG", error.getMessage(), error);
								}
							
							}){
						
						protected Response<JSONObject> parseNetworkResponse(NetworkResponse response){
							JSONObject jsonObject;
							try {
								jsonObject = new JSONObject(new String(response.data, "UTF-8"));
								return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
							} catch(UnsupportedEncodingException e){
								e.printStackTrace();
								return Response.error(new ParseError(e));
							} catch(JSONException e){
								e.printStackTrace();
								return Response.error(new ParseError(e));
							}
						}
						
					};
					
					mQueue.add(jsonObjectRequest);
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void parseJSONData(String jsonData){
		
		try {
			
			/**
			 * Parse JSONObject
			 */
			JSONObject jsonObject = new JSONObject(jsonData);
			
			JSONObject dataJSONObject = jsonObject.getJSONObject("data");
			Log.d("TAG", dataJSONObject.toString());
			
			JSONObject yesterdayJSONObject = dataJSONObject.getJSONObject("yesterday");
			Log.d("TAG", yesterdayJSONObject.toString());
			
			JSONArray jsonArray = dataJSONObject.getJSONArray("forecast");
			Log.d("TAG", jsonArray.toString());
			
			/**
			 * Parse JSONArray
			 */
			for(int i = 0; i < jsonArray.length(); i ++){
				JSONObject iObject = jsonArray.getJSONObject(i);
				Log.d("TAG", iObject.toString());
			}
			
			
			msg.what = SHOW_RESPONSE;
			msg.obj = (Object) (jsonArray.toString());
			handler.sendMessage(msg);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Parse CityCode 
	 */
	private void sendCityCodeRequest(){
		new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					
					StringRequest stringRequest = new StringRequest(
							"http://www.cnblogs.com/nozer/archive/2012/05/04/2482286.html",
							new Response.Listener<String>() {
								@Override
								public void onResponse(String response){
									Log.d("TAG", response);
								}
							}, new Response.ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error){
									Log.e("TAG", error.getMessage(), error);
								}
							
							});
					
					mQueue.add(stringRequest);
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	
}




















