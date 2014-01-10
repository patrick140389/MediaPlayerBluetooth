package classholder;

import java.util.Comparator;

public class SortHolder implements Comparator<ClassHolderOne>
{

	@Override
	public int compare(ClassHolderOne cho1, ClassHolderOne cho2)
	{
		return cho1.NAME.compareToIgnoreCase(cho2.NAME);
	}

}
