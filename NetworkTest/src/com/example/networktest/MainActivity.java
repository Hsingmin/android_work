package com.example.networktest;

import android.app.Activity;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	public static final int SHOW_RESPONSE = 0;
	private Button sendRequest;
	private TextView responseText;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case SHOW_RESPONSE:
				String response = (String) msg.obj;
				responseText.setText(response);
			}
		}
	};
	
	@Override
	protected  void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sendRequest = (Button) findViewById(R.id.send_request);
		responseText = (TextView) findViewById(R.id.response_text);
		sendRequest.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v){
		if(v.getId() == R.id.send_request){
			sendRequestWithHttpClient();
		}
	}
	
	private void sendRequestWithHttpClient(){
		new Thread(new Runnable(){
			@Override
			public void run(){
				
				try {
					HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet("http://10.0.2.2/get_data.json");
					HttpResponse httpResponse = httpClient.execute(httpGet);
					if(httpResponse.getStatusLine().getStatusCode() == 200){
						HttpEntity entity = httpResponse.getEntity();
						String response = EntityUtils.toString(entity, "utf-8");
						parseJSONWithJSONObject(response);
					}
				} catch(IOException e){
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void parseJSONWithJSONObject(String jsonData){
		Gson gson = new Gson();
		List<App> appList = gson.fromJson(jsonData, new TypeToken<List<App>>(){}.getType());
		for(App app : appList){
			Log.d("MainActivity", "id is " + app.getId());
			Log.d("MainActivity", "name is " + app.getName());
			Log.d("MainActivity", "version is " + app.getVersion());
		}
	}
}






















