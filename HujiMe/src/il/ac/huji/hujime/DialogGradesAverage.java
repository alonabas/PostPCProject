package il.ac.huji.hujime;

import il.ac.huji.hujime.Grades.Grade;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class DialogGradesAverage extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(getString(R.string.title_grades));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.show();

		// define custom title
		setContentView(R.layout.dialog_list);
		TextView title = (TextView) findViewById(R.id.dialog_list_text);
		title.setText(R.string.message_select_courses);
		Database db = Database.getInstance(this);
		final ArrayList<Grade> grades = db.getGrades();
		final GradesSelectAdapter adapter = new GradesSelectAdapter(this,
				grades);
		final ListView list = (ListView) findViewById(R.id.dialog_list_list);
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		list.addHeaderView(defineHeader(), null, false);
		list.setAdapter(adapter);
		// on CANCEL button click action
		findViewById(R.id.dialog_list_close).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent resultIntent = new Intent();
						setResult(RESULT_CANCELED, resultIntent);
						finish();
					}
				});
		// on OK button click action
		findViewById(R.id.dialog_list_enter).setOnClickListener(
				new OnClickListener() {
					@SuppressLint("DefaultLocale")
					@Override
					public void onClick(View view) {
						int total = 0;
						int points = 0;
						ArrayList<Grade> selected = adapter.getCheckedItems();
						for (Grade grade : selected) {
							total += grade.getGrade() * grade.getPoints();
							points += grade.getPoints();
						}
						double average = (double) total / (double) points;
						int tempAverage = (int) (average * 100);
						average = (double) tempAverage / (double) 100;
						String data = String.format("%.2f", average);
						Intent resultIntent = new Intent();
						resultIntent.putExtra("average", data);
						resultIntent.putExtra("points", points);
						setResult(RESULT_OK, resultIntent);
						finish();
					}
				});
	}

	/**
	 * Method that defines the header of list view
	 * 
	 * @return the View of header
	 */
	private View defineHeader() {
		View header = getLayoutInflater().inflate(
				R.layout.header_one_grade_select, null);

		TextView name = (TextView) header
				.findViewById(R.id.header_one_grade_select_name);
		TextView grade = (TextView) header
				.findViewById(R.id.header_one_grade_select_grade);
		TextView points = (TextView) header
				.findViewById(R.id.header_one_grade_select_points);

		name.setText(getString(R.string.header_grade_name));
		grade.setText(getString(R.string.header_grade_grade));
		points.setText(getString(R.string.header_grade_points));
		return header;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// add back button
		MenuItem back = menu.add("");
		back.setIcon(R.drawable.ic_action_navigation_back);
		back.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		back.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				DialogGradesAverage.this.finish();
				return true;
			}

		});
		return true;
	}

}
