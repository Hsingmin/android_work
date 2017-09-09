package com.example.mediaplayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

public class SDUtil {
	
	public static String getInnerSDCardPath(){
		return Environment.getExternalStorageDirectory().getPath();
	}
	
	public static List<String> getExtSDCardPath(){
		
		List<String> lResult = new ArrayList<String>();
		
		try{
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec("mount");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while((line = br.readLine()) != null){
				if(line.contains("extSdCard")){
					String[] arr = line.split(" ");
					String path = arr[1];
					File file = new File(path);
					if(file.isDirectory()){
						lResult.add(path);
					}
				}
			}
			isr.close();
		} catch(Exception e){
			e.printStackTrace();
		}
		return lResult;
	}
	
}











