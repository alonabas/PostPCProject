package il.ac.huji.hujime;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Page Adapter 
 * @author alonaba
 *
 */
class GeneralPageAdapter extends FragmentPagerAdapter {
	private List<Fragment> fragments;

	public GeneralPageAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;

	}

	@Override
	public Fragment getItem(int position) {
		return this.fragments.get(position);
	}

	@Override
	public int getCount() {
		return this.fragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		int data_fragment = fragments.get(position).getArguments()
				.getInt("EXTRA_MESSAGE");
		if(data_fragment == 1){
			return "מפות סטטיות";
		} else if (data_fragment == 2) {
			return "חיפוש במפות גוגל";
		} else {
			return "שנת "
					+ data_fragment;
		}
	}

}
