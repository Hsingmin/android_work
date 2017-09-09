package com.briup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;


/**
 * 播放歌词服务
 * 
 * @author Administrator
 * 
 */
public class PlayLrc
{

	/**
	 * 换算时间
	 */
	public static String formatMS(int ms)
	{
		int s = ms / 1000;// 秒
		int m = s / 60;// 分
		int add = s % 60;// 秒	
		String con = "";
		if (add > 10 && m > 10)
			con = m + ":" + add;
		else if (add < 10 && m > 10)
			con = m + ":0" + add;
		else if (m < 10&& add < 10)
			con = "0" + m + ":0" + add;
		else if (m > 10 && add < 10)
			con = "" + m + ":0" + add;	
		else if (m < 10 &&  add > 10)
			con = "0" + m + ":" + add ;	
		return con;
	}
   
	/**
	 * 读取歌词文件
	 */
	public static String read(File path, String filename)
	{
		StringBuilder sb = new StringBuilder();
		Set<String> lrcs = FileUtil.getLrcs(path);
		//获取歌词文件名
		String lrcfile=filename.substring(0,filename.indexOf("."));		
			try
			{				
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path+"/"+lrcfile+".lrc"),"GBK"));
				String con = "";
				while ((con = br.readLine()) != null)
				{
					sb.append(con + "\n");
				}
				br.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}	
		return sb.toString();

	}
}
