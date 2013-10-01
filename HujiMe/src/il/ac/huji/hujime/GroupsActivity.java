package il.ac.huji.hujime;

import il.ac.huji.hujime.Query.Argument;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Activity that displays groups for specific course
 * @author alonaba
 *
 */
public class GroupsActivity extends Activity {
	private String _courseId;
	private static final String _shnaton = "http://shnaton.huji.ac.il/index.php";
	private ArrayList<DisplayLesson> _lessons;
	private ListView _listView;
	private ProgressDialog _dialog;
	GroupsAdapter _adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(getString(R.string.title_shnaton_data));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.show();

		// define custom title
		setContentView(R.layout.activity_other_groups);
		Intent intent = getIntent();
		_courseId = intent.getStringExtra("id");
		String courseName = intent.getStringExtra("name");
		TextView title = (TextView) findViewById(R.id.activity_other_groups_text);
		title.setText(_courseId + " " + courseName);
		// allow Internet connection
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		// check internet
		if (!StaticMethods.checkInternetConnection(this)) {
			displayNotification(this, getString(R.string.message_no_intenet_error));
		}
		if (!StaticMethods.checkInternetAccess(_shnaton)) {
			displayNotification(this, getString(R.string.message_some_internet_error));
		} else {
			// get data from shnaton
			_lessons = new ArrayList<DisplayLesson>();
			_listView = (ListView) findViewById(R.id.activity_other_groups_list);
			_listView.addHeaderView(defineHeader(), null, false);
			GetShnaton getData = new GetShnaton();
			getData.execute();
		}
	}

	@Override
	protected void onDestroy() {
		if (_dialog != null) {
			if(_dialog.isShowing()){
				_dialog.dismiss();
			}
			_dialog = null;
		}
		super.onDestroy();
	}

	/**
	 * Async Task that connects to shnaton and parses data
	 * 
	 * @author alonaba
	 * 
	 */
	private class GetShnaton extends AsyncTask<Void, Void, Boolean> {

		private TextView _text;
		private HujiSession _webAccess;

		@Override
		protected Boolean doInBackground(Void... params) {
			// get shnaton year
			String htmlPage = _webAccess.getNonSecurePage(_shnaton);
			int year = getCurrentYear(htmlPage);
			// get shnaton data
			htmlPage = _webAccess.getNonSecurePage(_shnaton, defineQuery(year));
			if (htmlPage == null) {
				return false;
			}
			if (!parseHtml(htmlPage)) {
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			_dialog.dismiss();
			if (!result) {
				GroupsActivity.this.finish();
			} else {
				_adapter = new GroupsAdapter(GroupsActivity.this,
						_lessons);
				_listView.setAdapter(_adapter);
			}
		}

		@Override
		protected void onPreExecute() {
			_dialog = new ProgressDialog(GroupsActivity.this);
			_dialog.show();
			_dialog.setContentView(R.layout.io_progress);
			_dialog.setCancelable(false);
			_text = (TextView) _dialog.findViewById(R.id.io_progress_text);
			_text.setTextAppearance(getApplicationContext(),
					R.style.SpecialText);
			_text.setText(GroupsActivity.this.getString(R.string.sync_shnaton));
			_webAccess = HujiSession.getInstance();
		}

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
				GroupsActivity.this.finish();
				return true;
			}

		});
		return true;
	}

	/*
	 * Method that defines the header of listview
	 * 
	 * @return - the header view
	 */
	private View defineHeader() {
		View header = getLayoutInflater().inflate(
				R.layout.header_one_group, null);
		TextView type = (TextView) header
				.findViewById(R.id.header_one_group_type);
		TextView time = (TextView) header
				.findViewById(R.id.header_one_group_time);
		TextView day = (TextView) header
				.findViewById(R.id.header_one_group_day);
		TextView place = (TextView) header
				.findViewById(R.id.header_one_group_place);
		TextView group = (TextView) header
				.findViewById(R.id.header_one_group_group);
		TextView lecturer = (TextView) header
				.findViewById(R.id.header_one_group_lecturer);

		type.setText(getString(R.string.other_lessons_type_of_lesson));
		time.setText(getString(R.string.other_lessons_hours));
		place.setText(getString(R.string.other_lessons_place));
		group.setText(getString(R.string.other_lessons_group));
		lecturer.setText(getString(R.string.other_lessons_lecturer));
		day.setText(getString(R.string.other_lessons_day));
		return header;
	}

	/*
	 * Method that get html page and retrieve desired data from it
	 * 
	 * @param page - html page
	 * 
	 * @return true, if parse succeed,false otherwise
	 */
	private boolean parseHtml(String page) {
		Whitelist whitelist = Whitelist.none();
		whitelist.addTags("table", "td", "tr", "th");
		whitelist.addAttributes("td", "class");
		page = Jsoup.clean(page, whitelist);
		Document document = Jsoup.parse(page);
		// get name of course
		Elements tdCourse = document.getElementsByAttributeValue("class",
				"courseDet text");
		// if about course no data in shnaton
		if (tdCourse.size() == 0) {
			return false;
		}
		Element trCourse = tdCourse.first().parent();
		Element tableCourse = trCourse.parent();
		// get titles
		int indexPlace = 0;
		int indexDay = 0;
		int indexTime = 0;
		int indexType = 0;
		int indexLecturer = 0;
		int indexGroup = 0;
		Elements titles = tableCourse.getElementsByTag("th");
		for (int i = 0; i < titles.size(); i++) {
			if (titles.get(i).text()
					.equals(getString(R.string.other_lessons_place))) {
				indexPlace = i;
			} else if (titles.get(i).text()
					.equals(getString(R.string.other_lessons_day_week))) {
				indexDay = i;
			} else if (titles.get(i).text()
					.equals(getString(R.string.other_lessons_group))) {
				indexGroup = i;
			} else if (titles.get(i).text()
					.equals(getString(R.string.other_lessons_hours))) {
				indexTime = i;
			} else if (titles.get(i).text()
					.equals(getString(R.string.other_lessons_lecturer_name))) {
				indexLecturer = i;
			} else if (titles.get(i).text()
					.equals(getString(R.string.other_lessons_type_of_lesson))) {
				indexType = i;
			}
		}
		Elements courseRows = tableCourse.getElementsByTag("tr");
		if (courseRows.size() < 2) {
			return false;
		}
		String lecturerName;
		String type;
		String time;
		String day;
		String group;
		String place;

		for (int i = 1; i < courseRows.size() - 1; i++) {
			Elements cells = courseRows.get(i).getElementsByTag("td");
			type = cells.get(indexType).text();
			lecturerName = cells.get(indexLecturer).text();
			time = cells.get(indexTime).text();
			day = cells.get(indexDay).text();
			group = cells.get(indexGroup).text();
			place = cells.get(indexPlace).text();
			DisplayLesson lesson = new DisplayLesson(lecturerName, type, time,
					day, group, place);
			_lessons.add(lesson);
		}
		return true;
	}

	/*
	 * Android define query for shnaton
	 * 
	 * @param year - the shnaton Year
	 * 
	 * @return the query in string format
	 */
	private String defineQuery(int year) {
		Query query = new Query(Argument.PEULA, "Simple");
		query.addDefaultArguments(Argument.STARTING);
		query.addArguments(Argument.YEAR, String.valueOf(year));
		query.addArguments(Argument.COURSE, _courseId);
		query.addDefaultArguments(Argument.FACULCY);
		query.addDefaultArguments(Argument.PRISA);
		query.addDefaultArguments(Argument.WORD);
		query.addDefaultArguments(Argument.OPTION);
		query.addDefaultArguments(Argument.LANGUAGE);
		query.addDefaultArguments(Argument.SHIUR);
		return query.getQuery();
	}

	/*
	 * Get shnton last year from html page
	 * 
	 * @param htmlPage - page to retrieve year from
	 * 
	 * @return
	 */
	private int getCurrentYear(String htmlPage) {
		Whitelist whitelist = Whitelist.none();
		whitelist.addTags("select", "option");
		whitelist.addAttributes("select", "id");
		whitelist.addAttributes("option", "value");
		htmlPage = Jsoup.clean(htmlPage, whitelist);
		Document document = Jsoup.parse(htmlPage);
		Element yearSelect = document.getElementById("year");
		Elements years = yearSelect.children();
		String currentYear = years.get(years.size() - 1).attr("value");
		return Integer.valueOf(currentYear);

	}

	/**
	 * Class of display lesson
	 * 
	 * @author alonaba
	 * 
	 */
	class DisplayLesson {
		private String _lecturerNameLesson;
		private String _typeLesson;
		private String _timeLesson;
		private String _dayLesson;
		private String _groupLesson;
		private String _placeLesson;

		/**
		 * Constructor
		 * 
		 * @param lecturerNameLesson
		 *            - name of lecturer
		 * @param typeLesson
		 *            - type of lesson
		 * @param timeLesson
		 *            - time of lesson
		 * @param dayLesson
		 *            - day of lesson
		 * @param groupLesson
		 *            - group
		 * @param placeLesson
		 *            - place of lesson
		 */
		public DisplayLesson(String lecturerNameLesson, String typeLesson,
				String timeLesson, String dayLesson, String groupLesson,
				String placeLesson) {
			_lecturerNameLesson = lecturerNameLesson;
			_typeLesson = typeLesson;
			_timeLesson = timeLesson;
			_dayLesson = dayLesson;
			_groupLesson = groupLesson;
			_placeLesson = placeLesson;
		}

		/**
		 * Get lecturer name
		 * 
		 * @return name
		 */
		public String getLecturer() {
			return _lecturerNameLesson;
		}

		/**
		 * Get lecturer type
		 * 
		 * @return type
		 */
		public String getType() {
			return _typeLesson;
		}

		/**
		 * Get type of lecturer
		 * 
		 * @return type
		 */
		public String getTime() {
			return _timeLesson;
		}

		/**
		 * Get day of lesson
		 * 
		 * @return day
		 */
		public String getDay() {
			return _dayLesson;
		}

		/**
		 * Get group
		 * 
		 * @return group
		 */
		public String getGroup() {
			return _groupLesson;
		}

		/**
		 * Get lesson place
		 * 
		 * @return place
		 */
		public String getPlace() {
			return _placeLesson;
		}
	}

	/**
	 * Method that displays dialog
	 * 
	 * @param context
	 *            - the current context
	 * @param data
	 *            - the data to write in message
	 */
	public void displayNotification(Context context, String data) {
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialog_notification);
		dialog.setCancelable(true);
		// set the text
		TextView text = (TextView) dialog.findViewById(R.id.dialog_notification_text);
		text.setText(data);
		dialog.show();
		Button cancel = (Button) dialog
				.findViewById(R.id.dialog_notification_cancel_button);
		cancel.setOnClickListener(new OnClickListener() {
			// if cancel pressed, close dialog
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				GroupsActivity.this.finish();
			}
		});
	}
}
