package com.wessolowski.app.util.ressources;
import java.util.*;
import android.net.*;
import java.io.*;
import com.wessolowski.app.util.checks.*;

public class FileLoader
{
	public static ArrayList<Uri> loadUris(File dir, String... searchName)
	{
		File[] files = dir.listFiles();
		ArrayList<Uri> loadedUris = new ArrayList<Uri>();
		
		if(Checks.checkNull(files))
		{
			for(int i = 0; i < files.length; i++)
			{
				if(!files[i].isDirectory())
				{
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
