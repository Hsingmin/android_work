package com.briup;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainList extends Activity
{	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		ListView list = (ListView) findViewById(R.id.list);
		if (getPath() == null)
			alert("信息提示","请插入sdcard!");			
		else if(FileUtil.getFiles(getPath())==null)		
			alert("信息提示","sdcard没有歌曲");			
	  else{
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FileUtil.getFiles(getPath()));
			list.setAdapter(adapter);		
			list.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{
					Intent intent=new Intent(MainList.this,MediaPlayerActivity.class);
					intent.putExtra("file",((TextView)view).getText());
					startActivity(intent);
				}				
			});
		}
	}

	//弹出对话框
	private void alert(String title,String msg)
	{		
		AlertDialog.Builder dialog = new Builder(this);
		dialog.setTitle(title);
		dialog.setMessage(msg);
		dialog.setPositiveButton("退出", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{

			}
		});
		dialog.create().show();
	}

	private File getPath()
	{
		File path = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			path = Environment.getExternalStorageDirectory();
		}
		return path;
	}
}
