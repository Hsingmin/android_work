package com.briup;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.util.Log;
/**
 * �ļ�������
 * @author Administrator
 *
 */
public class FileUtil
{
	/**
	 * ��ȡsdcard���������ļ�
	 */
	public static List<String> getFiles(File path)
	{		
		List<String> files = new ArrayList<String>();		
			File musics[] = path.listFiles();
			Log.i("MediaPlayerActivity","�ļ�����"+musics.length);
			if (musics != null)
			{
				for (int i = 0; i < musics.length; i++)
				{
					if (musics[i].getName().indexOf(".") >= 0)
					{
						/* ֻȡ.mp3|.mar|wma�ļ� */
						String file = musics[i].getName().substring(musics[i].getName().indexOf("."));
						if (file.toLowerCase().equals(".mp3") || file.toLowerCase().equals(".amr") || file.toLowerCase().equals(".wma"))
							files.add(musics[i].getName());
					}
				}
			}		
		return files;
	}
	/**
	 * ��ȡ���и���ļ�
	 * @param path
	 * @return
	 */
	public static Set<String> getLrcs(File path)
	{		
		   Set<String> files = new HashSet<String>();		
			File musics[] = path.listFiles();		
			if (musics != null)
			{
				for (int i = 0; i < musics.length; i++)
				{
					if (musics[i].getName().indexOf(".") >= 0)
					{
						/* ֻȡ.lrc�ļ� */
						String file = musics[i].getName().substring(musics[i].getName().indexOf("."));
						if (file.toLowerCase().equals(".lrc"))
						    	files.add(musics[i].getName());
					}
				}
			}		
		return files;
	}
}
