package il.ac.huji.hujime;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Maps activity
 * 
 * @author alonabas
 * 
 */
public class MapsActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(0);
		actionBar.setSubtitle(getString(R.string.title_maps));
		actionBar.show();

		List<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(StaticMapsFragment.newInstance(1));
		fragments.add(DynamicMapsFragment.newInstance(2, this));
		GeneralPageAdapter pageAdapter = new GeneralPageAdapter(
				getSupportFragmentManager(), fragments);
		ViewPager pager = (ViewPager) findViewById(R.id.activity_maps_viewpager);
		pager.setAdapter(pageAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem grades = menu.findItem(R.id.menu_grades);
		MenuItem exams = menu.findItem(R.id.menu_exams);
		MenuItem lessons = menu.findItem(R.id.menu_lessons);
		MenuItem settings = menu.findItem(R.id.menu_settings);
		menu.removeItem(R.id.menu_maps);
		grades.setIntent(new Intent(this, GradesActivity.class)
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP));
		exams.setIntent(new Intent(this, ExamsActivity.class)
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP));
		lessons.setIntent(new Intent(this, LessonsActivity.class)
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP));
		settings.setIntent(new Intent(this, Preferences.class)
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP));
		return true;
	}

}
