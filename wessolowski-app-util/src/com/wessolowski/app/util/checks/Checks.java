package com.wessolowski.app.util.checks;

public class Checks
{
	public static boolean checkNull(Object object)
	{
		if(object != null)
		{
			return true;
		}
		return false;
	}
	
	public static boolean checkNull(Object[] objects)
	{
		for(int i = 0; i < objects.length; i++)
		{
			if(objects[i] == null)
			{
				return false;
			}
		}
		return true;
	}
	
	public static boolean ckeckNull(boolean bool, Object object)
	{
		if(bool && object != null)
		{
			return true;
		}
		return false;
	}
}
