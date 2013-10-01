package il.ac.huji.hujime;

import java.util.Vector;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Pager adapter
 * 
 * @author alonaba
 * 
 */
public class CustomPagerAdapter extends PagerAdapter {

	private Vector<View> pages;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            - context
	 * @param pages
	 *            - vector of pages
	 */
	public CustomPagerAdapter(Vector<View> pages) {
		this.pages = pages;
	}

	@Override
	public CharSequence getPageTitle(int position) {

		switch (position) {
		case 4:
			return "יום ראשון";
		case 3:
			return "יום שני";
		case 2:
			return "יום שלישי";
		case 1:
			return "יום רביעי";
		case 0:
			return "יום חמישי";
		}
		return "יום אחר";
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View page = pages.get(position);
		container.addView(page);
		return page;
	}

	@Override
	public int getCount() {
		return pages.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}