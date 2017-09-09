package com.example.fragmenttest;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Build;

public class MainActivity extends Activity /*implements OnClickListener*/ {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button button = (Button) findViewById(R.id.button);
		/*button.setOnClickListener(this);*/

	}

	/*
	@Override
	public void onClick(View v){
		switch(v.getId()){
		case R.id.button:
			AnotherRightFragment fragment = new AnotherRightFragment();
			FragmentManager fragmentManager = getFragmentManager();
			android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.right_layout, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
			break;
			default:
				break;
		}
	}
	*/
}
















