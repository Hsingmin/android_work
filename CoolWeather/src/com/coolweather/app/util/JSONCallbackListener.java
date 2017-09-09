package com.coolweather.app.util;

public interface JSONCallbackListener {
	
	void onFinish(String response);
	
	void onError(Exception e);
}
