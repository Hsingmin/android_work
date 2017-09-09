package com.coolweather.app.util;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.util.Log;

public class JSONUtil {
	
	//private static RequestQueue mQueue = Volley.newRequestQueue(VolleyApplication.getContext());
	
	public static void sendJSONRequest(final String address, final JSONCallbackListener listener){
		new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					RequestQueue mQueue = Volley.newRequestQueue(VolleyApplication.getContext());
					
					Log.d("TAG", "sendJSONRequest : Server = " + address);
					JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
							 address, null,
							new Response.Listener<JSONObject>() {
								@Override
								public void onResponse(JSONObject response){
									Log.d("TAG", "Here to the LOG.");
									Log.d("TAG", response.toString());
									if(listener != null){
										listener.onFinish(response.toString());
									}
								}
							}, new Response.ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error){
									Log.e("TAG", error.getMessage(), error);
									if(listener != null){
										listener.onError(error);
									}
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
}
