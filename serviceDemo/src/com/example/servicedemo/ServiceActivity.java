package com.example.servicedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ServiceActivity extends Activity {
	
	private Button startLocalService;
	private Button stopLocalService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.service_demo);
		
		startLocalService = (Button) findViewById(R.id.start_local_service);
		stopLocalService = (Button) findViewById(R.id.stop_local_service);
		
		startLocalService.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				startService(new Intent("com.example.servicedemo"));
			}
		});
		
		stopLocalService.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				stopService(new Intent("com.example.servicedemo"));
			}
		});
		
	}
}



















