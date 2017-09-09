package com.briup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;


/**
 * ���Ÿ�ʷ���
 * 
 * @author Administrator
 * 
 */
public class PlayLrc
{

	/**
	 * ����ʱ��
	 */
	public static String formatMS(int ms)
	{
		int s = ms / 1000;// ��
		int m = s / 60;// ��
		int add = s % 60;// ��	
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
	 * ��ȡ����ļ�
	 */
	public static String read(File path, String filename)
	{
		StringBuilder sb = new StringBuilder();
		Set<String> lrcs = FileUtil.getLrcs(path);
		//��ȡ����ļ���
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
