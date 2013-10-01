package il.ac.huji.hujime;

import il.ac.huji.hujime.Lessons.Semester;

import java.util.Calendar;
import java.util.Vector;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity of Lessons
 * @author alonaba
 *
 */
public class LessonsActivity extends Activity {

	private Context _context;
	private ViewPager _viewPager;
	private Database _db;
	private ListView _listViewSunday;
	private ListView _listViewMonday;
	private ListView _listViewTuesday;
	private ListView _listViewWednesday;
	private ListView _listViewThursday;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		StaticMethods.getOverflowMenu(this.getApplicationContext());
		actionBar.setNavigationMode(0);

		actionBar.setSubtitle(getString(R.string.title_lessons));
		actionBar.show();

		_context = this;
		// check if logged in
		SessionManager session = new SessionManager(getApplicationContext());
		if (!session.isLoggedIn()) {
			Intent i = new Intent(_context, LoginActivity.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// Staring Login Activity
			_context.startActivity(i);
			finish();
		} else {
			_db = Database.getInstance(_context);
			Semester semester = null;
			Intent intent = getIntent();
			if (intent == null) {
				semester = getCurrentSemester();
			}
			// if "for_semester" = 1, set semester to A, if "for_semester" = 2,
			// set
			// semester to B
			// if is null i.e 0, calculate semester
			else {
				int type = intent.getIntExtra("for_semster", 0);
				switch (type) {
				case 0:
					// check current semester
					semester = getCurrentSemester();
					break;
				case 1:
					semester = Semester.A;
					break;
				case 2:
					semester = Semester.B;
					break;
				}
			}
			setContentView(R.layout.activity_lessons);
			_listViewSunday = new ListView(_context);
			_listViewSunday.addHeaderView(defineHeader(), null, false);
			_listViewMonday = new ListView(_context);
			_listViewMonday.addHeaderView(defineHeader(), null, false);
			_listViewTuesday = new ListView(_context);
			_listViewTuesday.addHeaderView(defineHeader(), null, false);
			_listViewWednesday = new ListView(_context);
			_listViewWednesday.addHeaderView(defineHeader(), null, false);
			_listViewThursday = new ListView(_context);
			_listViewThursday.addHeaderView(defineHeader(), null, false);

			Vector<View> pages = new Vector<View>();
			pages.add(_listViewThursday);
			pages.add(_listViewWednesday);
			pages.add(_listViewTuesday);
			pages.add(_listViewMonday);
			pages.add(_listViewSunday);

			_viewPager = (ViewPager) findViewById(R.id.activity_lessons_viewpager);
			CustomPagerAdapter adapter = new CustomPagerAdapter(pages);
			_viewPager.setAdapter(adapter);

			_listViewSunday.setAdapter(new LessonsAdapter((Activity) _context,
					_db.getLessonsDay(semester, 0)));
			_listViewMonday.setAdapter(new LessonsAdapter((Activity) _context,
					_db.getLessonsDay(semester, 1)));
			_listViewTuesday.setAdapter(new LessonsAdapter((Activity) _context,
					_db.getLessonsDay(semester, 2)));
			_listViewWednesday.setAdapter(new LessonsAdapter(
					(Activity) _context, _db.getLessonsDay(semester, 3)));
			_listViewThursday.setAdapter(new LessonsAdapter(
					(Activity) _context, _db.getLessonsDay(semester, 4)));
			registerForContextMenu(_listViewSunday);
			registerForContextMenu(_listViewMonday);
			registerForContextMenu(_listViewTuesday);
			registerForContextMenu(_listViewWednesday);
			registerForContextMenu(_listViewThursday);

			Calendar calendar = Calendar.getInstance();
			// set current tab and selected item
			setDayHour(calendar);
		}
	}

	/**
	 * Get the current semester according to date
	 * 
	 * @return - current semester
	 */
	private Semester getCurrentSemester() {
		Calendar current = Calendar.getInstance();
		Calendar endFirstSemester = _db.getDate("end_first_semester");
		if (current.compareTo(endFirstSemester) <= 0) {
			// so current semester is 1
			return Semester.A;
		} else {
			return Semester.B;
		}
	}

	/*
	 * Set current tab according to day and make current list to focus on
	 * current hour
	 * 
	 * @param calendar - the current day and hour
	 */
	private void setDayHour(Calendar calendar) {
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		if (day == Calendar.FRIDAY || day == Calendar.SATURDAY) {
			_viewPager.setCurrentItem(4);
			_listViewSunday.setSelection(0);
			_listViewSunday.setItemChecked(0, true);

		} else {
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			if (hour > 21) {
				_viewPager.setCurrentItem(4 - day);
			} else {
				_viewPager.setCurrentItem(5 - day);
			}
			switch (day) {
			case Calendar.SUNDAY:
				if (hour > 21) {
					_listViewMonday.setSelection(0);
					_listViewMonday.setItemChecked(0, true);
				} else if (hour < 8) {
					_listViewSunday.setSelection(0);
					_listViewSunday.setItemChecked(0, true);
				} else {
					_listViewSunday.setSelection(hour - 7);
					_listViewSunday.setItemChecked(hour - 7, true);
				}
				break;
			case Calendar.MONDAY:
				if (hour > 21) {
					_listViewTuesday.setSelection(0);
					_listViewTuesday.setItemChecked(0, true);
				} else if (hour < 8) {
					_listViewMonday.setSelection(0);
					_listViewMonday.setItemChecked(0, true);
				} else {
					_listViewMonday.setSelection(hour - 7);
					_listViewMonday.setItemChecked(hour - 7, true);
				}
				break;
			case Calendar.TUESDAY:
				if (hour > 21) {
					_listViewWednesday.setSelection(0);
					_listViewWednesday.setItemChecked(0, true);
				} else if (hour < 8) {
					_listViewTuesday.setSelection(0);
					_listViewTuesday.setItemChecked(0, true);
				} else {
					_listViewTuesday.setSelection(hour - 7);
					_listViewTuesday.setItemChecked(hour - 7, true);
				}
				break;
			case Calendar.WEDNESDAY:
				if (hour > 21) {
					_listViewThursday.setSelection(0);
					_listViewThursday.setItemChecked(0, true);
				} else if (hour < 8) {
					_listViewWednesday.setSelection(0);
					_listViewWednesday.setItemChecked(0, true);
				} else {
					_listViewWednesday.setSelection(hour - 7);
					_listViewWednesday.setItemChecked(hour - 7, true);
				}
				break;
			case Calendar.THURSDAY:
				if (hour > 21) {
					_listViewSunday.setSelection(0);
					_listViewSunday.setItemChecked(0, true);
				} else if (hour < 8) {
					_listViewThursday.setSelection(0);
					_listViewThursday.setItemChecked(0, true);
				} else {
					_listViewThursday.setSelection(hour - 7);
					_listViewThursday.setItemChecked(hour - 7, true);
				}
				break;
			}
		}
	}

	/**
	 * Method that defines header of the list view exams
	 * 
	 * @return - View of header
	 */
	private View defineHeader() {
		View header = getLayoutInflater().inflate(R.layout.header_one_lesson,
				null);
		TextView place = (TextView) header
				.findViewById(R.id.header_one_lesson_place);
		TextView lesson = (TextView) header
				.findViewById(R.id.header_one_lesson_name_type);
		TextView hour = (TextView) header
				.findViewById(R.id.header_one_lesson_hour);
		place.setText(R.string.lesson_place);
		lesson.setText(R.string.lesson_name_type);
		hour.setText(R.string.lesson_hour);
		return header;

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		// display context menu only on items with text
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		String title = ((TextView) info.targetView
				.findViewById(R.id.one_lesson_name)).getText().toString();
		if (!title.equals("")) {
			getMenuInflater().inflate(R.menu.lessons_context_menu, menu);
			super.onCreateContextMenu(menu, view, menuInfo);
			menu.setHeaderTitle(title);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Intent intent;
		String name;
		switch (item.getItemId()) {
		case R.id.lesson_edit:
			name = ((TextView) info.targetView
					.findViewById(R.id.one_lesson_name)).getText().toString();
			String old_place = ((TextView) info.targetView
					.findViewById(R.id.one_lesson_place)).getText().toString();
			intent = new Intent(this, DialogLessonEdit.class);
			// call activity to edit item
			intent.putExtra("name", name);
			intent.putExtra("place", old_place);
			intent.putExtra("position", info.position);
			startActivityForResult(intent, 1);

			break;
		case R.id.lesson_remove:
			intent = new Intent(this, DialogLessonDelete.class);
			name = ((TextView) info.targetView
					.findViewById(R.id.one_lesson_name)).getText().toString();
			intent.putExtra("name", name);
			intent.putExtra("position", info.position);
			startActivityForResult(intent, 2);
			break;
		case R.id.lessonOtherGroups:
			// get course id from db
			name = ((TextView) info.targetView
					.findViewById(R.id.one_lesson_name)).getText().toString();
			String id = _db.queryForCourseId(name);
			if (id == null) {
				// should not happened
				Toast.makeText(this,
						getString(R.string.message_course_not_found),
						Toast.LENGTH_LONG).show();
			} else {
				intent = new Intent(this, GroupsActivity.class);
				intent.putExtra("id", id);
				intent.putExtra("name", name);
				startActivity(intent);
			}
			break;
		}
		return true;
	}

	@Override
	public void onActivityResult(int reqCode, int resCode, Intent data) {
		if (reqCode == 1 && resCode == Activity.RESULT_OK) {
			// get extras
			String newName = data.getStringExtra("new_name");
			String newPlace = data.getStringExtra("new_place");
			String oldName = data.getStringExtra("old_name");
			int position = data.getIntExtra("position", -1);
			if (position != -1) {
				updateLesson(position, newName, newPlace);
				int time = position + 7;
				int day = _viewPager.getCurrentItem();
				boolean status = _db.updateLesson(Semester.A, (4 - day), time,
						oldName, newName, newPlace);
				if (!status) {
					Toast.makeText(this,
							getString(R.string.message_update_failed),
							Toast.LENGTH_LONG).show();
				}
			}
		} else if (reqCode == 2 && resCode == Activity.RESULT_OK) {
			int position = data.getIntExtra("position", -1);
			String name = data.getStringExtra("name");
			if (position != -1) {
				int day = _viewPager.getCurrentItem();
				int time = position + 7;
				boolean status = _db.removeLesson(Semester.A, (4 - day), time,
						name);
				removeLesson(position);
				if (!status) {
					Toast.makeText(this,
							getString(R.string.message_update_failed),
							Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem grades = menu.findItem(R.id.menu_grades);
		MenuItem exams = menu.findItem(R.id.menu_exams);
		MenuItem maps = menu.findItem(R.id.menu_maps);
		MenuItem settings = menu.findItem(R.id.menu_settings);
		menu.removeItem(R.id.menu_lessons);
		grades.setIntent(new Intent(this, GradesActivity.class).setFlags(
				Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
		exams.setIntent(new Intent(this, ExamsActivity.class).setFlags(
				Intent.FLAG_ACTIVITY_CLEAR_TOP));
		maps.setIntent(new Intent(this, MapsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
				Intent.FLAG_ACTIVITY_CLEAR_TOP));
		settings.setIntent(new Intent(this, Preferences.class).setFlags(
				Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP));
		return true;
	}

	/**
	 * Update item in ListView
	 * 
	 * @param position - item position
	 * 
	 * @param newName - new name of item
	 * 
	 * @param newPlace - new place of item
	 */
	private void updateLesson(int position, String newName, String newPlace) {
		View v = null;
		int visiblePosition;
		switch (_viewPager.getCurrentItem()) {
		case 4:
			visiblePosition = _listViewSunday.getFirstVisiblePosition();
			v = _listViewSunday.getChildAt(position - visiblePosition);
			break;
		case 3:
			visiblePosition = _listViewMonday.getFirstVisiblePosition();
			v = _listViewMonday.getChildAt(position - visiblePosition);
			break;
		case 2:
			visiblePosition = _listViewTuesday.getFirstVisiblePosition();
			v = _listViewTuesday.getChildAt(position - visiblePosition);
			break;
		case 1:
			visiblePosition = _listViewWednesday.getFirstVisiblePosition();
			v = _listViewWednesday.getChildAt(position - visiblePosition);
			break;
		case 0:
			visiblePosition = _listViewThursday.getFirstVisiblePosition();
			v = _listViewThursday.getChildAt(position - visiblePosition);
			break;
		}
		TextView lessonPlace = (TextView) v.findViewById(R.id.one_lesson_place);
		lessonPlace.setText(newPlace);
		TextView lessonName = (TextView) v.findViewById(R.id.one_lesson_name);
		lessonName.setText(newName);
	}

	/**
	 * Remove item from listView
	 * 
	 * @param position - position of item to remove
	 */
	private void removeLesson(int position) {
		View v = null;
		int visiblePosition;
		switch (_viewPager.getCurrentItem()) {
		case 4:
			visiblePosition = _listViewSunday.getFirstVisiblePosition();
			v = _listViewSunday.getChildAt(position - visiblePosition);
			break;
		case 3:
			visiblePosition = _listViewMonday.getFirstVisiblePosition();
			v = _listViewMonday.getChildAt(position - visiblePosition);
			break;
		case 2:
			visiblePosition = _listViewTuesday.getFirstVisiblePosition();
			v = _listViewTuesday.getChildAt(position - visiblePosition);
			break;
		case 1:
			visiblePosition = _listViewWednesday.getFirstVisiblePosition();
			v = _listViewWednesday.getChildAt(position - visiblePosition);
			break;
		case 0:
			visiblePosition = _listViewThursday.getFirstVisiblePosition();
			v = _listViewThursday.getChildAt(position - visiblePosition);
			break;
		}
		TextView lessonPlace = (TextView) v.findViewById(R.id.one_lesson_place);
		lessonPlace.setText("");
		TextView lessonName = (TextView) v.findViewById(R.id.one_lesson_name);
		lessonName.setText("");
		TextView lessonType = (TextView) v.findViewById(R.id.one_lesson_type);
		lessonType.setText("");
	}
}