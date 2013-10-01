package il.ac.huji.hujime;

import il.ac.huji.hujime.Exams.Exam;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Activity of Exams
 * 
 * @author alonaba
 * 
 */
public class ExamsActivity extends Activity {
	ArrayList<Exam> _exams;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.general_list_view);
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(0);
		actionBar.setSubtitle(getString(R.string.title_exams));
		actionBar.show();

		View header = defineHeader();
		Database db = Database.getInstance(this);
		_exams = db.getExams();
		ExamsAdapter adapter = new ExamsAdapter(this, _exams);
		ListView listView = (ListView) findViewById(R.id.general_list_view);
		listView.addHeaderView(header, null, false);
		listView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem lessons = menu.findItem(R.id.menu_lessons);
		MenuItem grades = menu.findItem(R.id.menu_grades);
		MenuItem maps = menu.findItem(R.id.menu_maps);
		MenuItem settings = menu.findItem(R.id.menu_settings);
		menu.removeItem(R.id.menu_exams);
		// set intents
		lessons.setIntent(new Intent(this, LessonsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
		grades.setIntent(new Intent(this, GradesActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
		maps.setIntent(new Intent(this, MapsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
		settings.setIntent(new Intent(this, Preferences.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
		return true;
	}

	/**
	 * Method that defines header of the list view exams
	 * 
	 * @return - View of header
	 */
	private View defineHeader() {
		View header = getLayoutInflater().inflate(R.layout.header_one_exam,
				null);
		TextView nameId = (TextView) header
				.findViewById(R.id.header_one_exam_name_id);
		TextView moed = (TextView) header
				.findViewById(R.id.header_one_exam_moed);
		TextView date_time = (TextView) header
				.findViewById(R.id.header_one_exam_date_time);
		TextView place = (TextView) header
				.findViewById(R.id.header_one_exam_place);

		nameId.setText(getString(R.string.header_exams_id)
				+ getString(R.string.new_line)
				+ getString(R.string.header_exams_name));
		moed.setText(R.string.header_exams_moed);
		date_time.setText(R.string.header_exams_date_time);
		place.setText(R.string.header_exams_place);
		return header;

	}
}
