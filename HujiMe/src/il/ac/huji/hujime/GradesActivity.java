package il.ac.huji.hujime;

import il.ac.huji.hujime.Grades.Grade;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Grades activity
 * 
 * @author alonaba
 * 
 */
public class GradesActivity extends FragmentActivity {
	private ViewPager _pager;
	private ArrayList<Integer> _years;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		StaticMethods.getOverflowMenu(this.getApplicationContext());
		actionBar.setNavigationMode(0);
		actionBar.setSubtitle(getString(R.string.title_grades));
		actionBar.show();
		// check if access to grades with password
		SharedPreferences myPreference = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (myPreference.getBoolean("settings_grades_access", false)) {
			// open password activity
			Intent intent = new Intent(this, DialogPassword.class);
			startActivityForResult(intent, 1);
		} else {
			defineView();
		}
	}

	/**
	 * Define the View of grades activity
	 */
	private void defineView() {
		setContentView(R.layout.activity_grades);
		List<Fragment> fragments = getFragments();
		GeneralPageAdapter pageAdapter = new GeneralPageAdapter(
				getSupportFragmentManager(), fragments);
		_pager = (ViewPager) findViewById(R.id.activity_grades_viewpager);
		_pager.setAdapter(pageAdapter);

		// set average
		Database db = Database.getInstance(this);
		ArrayList<Grade> grades = db.getGrades();
		int total = 0;
		int points = 0;
		for (Grade grade : grades) {
			total += grade.getGrade() * grade.getPoints();
			points += grade.getPoints();
		}
		double average = (double) total / (double) points;
		int tempAverage = (int) (average * 100);
		average = (double) tempAverage / (double) 100;
		TextView averageTotal = (TextView) findViewById(R.id.activity_grades_text_average);
		String data = getString(R.string.grades_text_total_average) + " "
				+ String.format("%.2f", average) + " "
				+ getString(R.string.grades_text_points) + " " + points;
		averageTotal.setText(data);
		Button customAverage = (Button) findViewById(R.id.activity_grades_average_button);
		customAverage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GradesActivity.this,
						DialogGradesAverage.class);
				startActivityForResult(intent, 2);
			}

		});
	}

	@Override
	public void onActivityResult(int reqCode, int resCode, Intent data) {
		if (reqCode == 1 && resCode == Activity.RESULT_CANCELED) {
			finish();
		} else if (reqCode == 1 && resCode == Activity.RESULT_OK) {
			defineView();
		} else if (reqCode == 2 && resCode == Activity.RESULT_OK) {
			String average = data.getStringExtra("average");
			int points = data.getIntExtra("points", 0);
			String text = getString(R.string.grades_text_total_average) + " "
					+ average + " " + getString(R.string.grades_text_points)
					+ " " + points;

			// display result in dialog
			final Dialog dialog = new Dialog(GradesActivity.this);
			dialog.setContentView(R.layout.dialog_information);
			dialog.setCancelable(true);
			// set the text
			TextView textView = (TextView) dialog
					.findViewById(R.id.dialog_information_text);
			textView.setText(text);
			dialog.show();
			Button cancel = (Button) dialog
					.findViewById(R.id.dialog_information_close_button);
			cancel.setText(R.string.button_accept);
			cancel.setOnClickListener(new OnClickListener() {
				// if cancel pressed, close dialog
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}

			});
		}
	}

	/**
	 * Get Fragments of each year
	 * 
	 * @return - the list of fragments
	 */
	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();
		Database db = Database.getInstance(this);
		_years = db.getGradesYears();
		for (int year : _years) {
			fList.add(GradesFragment.newInstance(year, this));
		}
		return fList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem lessons = menu.findItem(R.id.menu_lessons);
		MenuItem exams = menu.findItem(R.id.menu_exams);
		MenuItem maps = menu.findItem(R.id.menu_maps);
		MenuItem settings = menu.findItem(R.id.menu_settings);
		menu.removeItem(R.id.menu_grades);
		lessons.setIntent(new Intent(this, LessonsActivity.class)
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP));
		exams.setIntent(new Intent(this, ExamsActivity.class)
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP));
		maps.setIntent(new Intent(this, MapsActivity.class)
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP));
		settings.setIntent(new Intent(this, Preferences.class)
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP));
		return true;
	}
}
