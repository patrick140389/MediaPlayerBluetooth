package mathematik;

import com.wessolowski.app.util.converters.Converter;

public class Mathematik
{
	public static float round(float f, int decimalPlace)
	{
		return Converter.toBigDecimal(f, decimalPlace).floatValue();
	}
	
}
