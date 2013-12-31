package com.wessolowski.app.util.ressources;
import java.io.File;
import java.util.ArrayList;

import android.net.Uri;
import android.util.Log;

import com.wessolowski.app.util.checks.Checks;

public class FileLoader
{
	private static final String	TAG				= FileLoader.class.getSimpleName();
	
	public static ArrayList<Uri> loadUris(File dir, String... searchName)
	{
		Log.i(TAG, "File dir: " + dir.getAbsolutePath());
		File[] files = dir.listFiles();
		Log.i(TAG, "Files lenght: " + files.length);
		ArrayList<Uri> loadedUris = new ArrayList<Uri>();
		
		if(Checks.checkNull(files))
		{
			for(int i = 0; i < files.length; i++)
			{
				if(!files[i].isDirectory())
				{
					Log.i(TAG, "File: " + files[i].getName());
					Log.i(TAG, "File: " + files[i].getAbsolutePath());
					for(int searchIndex = 0; searchIndex < searchName.length; searchIndex++)
					{
						if(files[i].getName().contains(searchName[searchIndex]))
						{
							Uri loadedUri = Uri.fromFile(files[i]);
							loadedUris.add(loadedUri);
						}
					}
				}
			}
		}
		return loadedUris;
	}
	
}
