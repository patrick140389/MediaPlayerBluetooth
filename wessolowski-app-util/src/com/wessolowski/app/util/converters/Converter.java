package com.wessolowski.app.util.converters;

import java.math.BigDecimal;

public class Converter
{
	public static String[] toStringArray(String s)
	{
		String[] strArr = {s};
		return strArr;
	}
	
	public static BigDecimal toBigDecimal(float d, int decimalPlace)
	{
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd;
	}
}
