package il.ac.huji.hujime;

import il.ac.huji.hujime.HujiSync.Sync;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Preferences activity
 * 
 * @author alonaba
 * 
 */
@SuppressLint("NewApi")
public class Preferences extends PreferenceActivity {
	HujiSession _sessionToHuji;
	Context _thisContext;
	Dialog _dialog;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(0);
		actionBar.setSubtitle(getString(R.string.title_settings));
		actionBar.show();
		_thisContext = this;
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		_sessionToHuji = HujiSession.getInstance();
		// setContentView(R.layout.preference_text);
		addPreferencesFromResource(R.xml.settings);
		getListView()
				.setSelector(
						android.support.v7.appcompat.R.drawable.abc_list_selector_background_transition_holo_light);
		((Preference) findPreference("settings_resync"))
				.setOnPreferenceClickListener(resyncer);
		((Preference) findPreference("settings_exams_resync"))
				.setOnPreferenceClickListener(resyncer);
		((Preference) findPreference("settings_lessons_resync"))
				.setOnPreferenceClickListener(resyncer);
		((Preference) findPreference("settings_grades_resync"))
				.setOnPreferenceClickListener(resyncer);

		((Preference) findPreference("settings_calendar"))
				.setOnPreferenceClickListener(resyncer);
		((Preference) findPreference("settings_lessons_calendar"))
				.setOnPreferenceClickListener(resyncer);
		((Preference) findPreference("settings_exams_calendar"))
				.setOnPreferenceClickListener(resyncer);

		((Preference) findPreference("settings_calendar_clear"))
				.setOnPreferenceClickListener(resyncer);
		((Preference) findPreference("settings_login_screen"))
				.setOnPreferenceClickListener(resyncer);
		((Preference) findPreference("settings_login_screen_clear"))
				.setOnPreferenceClickListener(resyncer);
	}

	/**
	 * Listener to onclick events
	 */
	public OnPreferenceClickListener resyncer = new OnPreferenceClickListener() {
		@SuppressLint("NewApi")
		public boolean onPreferenceClick(Preference pref) {
			if (pref.getKey().equals("settings_resync")) {
				Intent intent = new Intent(Preferences.this, SyncCaptcha.class);
				startActivityForResult(intent, 1);
			} else if (pref.getKey().equals("settings_exams_resync")) {
				Intent intent = new Intent(Preferences.this, SyncCaptcha.class);
				startActivityForResult(intent, 2);

			} else if (pref.getKey().equals("settings_lessons_resync")) {
				Intent intent = new Intent(Preferences.this, SyncCaptcha.class);
				startActivityForResult(intent, 3);

			} else if (pref.getKey().equals("settings_grades_resync")) {
				Intent intent = new Intent(Preferences.this, SyncCaptcha.class);
				startActivityForResult(intent, 4);
			}
			// calendar access
			else if (pref.getKey().equals("settings_calendar")) {
				int[] cals = checkForCalendars();
				if (cals.length == 0) {
					displayDialog(getString(R.string.message_no_calendars));
				} else {
					Intent intent = new Intent(_thisContext,
							CalendarDialog.class);
					intent.putExtra("type", 0);
					startActivityForResult(intent, 5);
				}
			} else if (pref.getKey().equals("settings_lessons_calendar")) {
				int[] cals = checkForCalendars();
				if (cals.length == 0) {
					displayDialog(getString(R.string.message_no_calendars));
				} else {
					Intent intent = new Intent(_thisContext,
							CalendarDialog.class);
					intent.putExtra("type", 1);
					startActivityForResult(intent, 6);
				}
			} else if (pref.getKey().equals("settings_exams_calendar")) {
				int[] cals = checkForCalendars();
				if (cals.length == 0) {
					displayDialog(getString(R.string.message_no_calendars));
				} else {
					Intent intent = new Intent(_thisContext,
							CalendarDialog.class);
					intent.putExtra("type", 2);
					startActivityForResult(intent, 7);
				}

			}

			else if (pref.getKey().equals("settings_calendar_clear")) {
				clearCalendars();
			} else if (pref.getKey().equals("settings_login_screen")) {
				logout();
			} else if (pref.getKey().equals("settings_login_screen_clear")) {
				clearCalendars();
				logout();
			}

			return false;
		}

	};

	/**
	 * Clear data from calendars
	 */
	private void clearCalendars() {
		CalendarSync sync = new CalendarSync(_thisContext);
		// get all calendars and remove from all
		int[] calendars = checkForCalendars();
		for (int calId : calendars) {
			sync.deleteExams(calId);
			sync.deleteLessons(calId);
		}
		displayDialog(getString(R.string.message_operation_succeed));
	}

	/**
	 * Logout from app
	 */
	private void logout() {
		SessionManager session = new SessionManager(_thisContext);
		session.logoutUser();
		Database db = Database.getInstance(_thisContext);
		db.deleteDbs();
		Intent intent = new Intent(_thisContext, LessonsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
		finish();
	}

	/**
	 * Check for calendars exist in system
	 * 
	 * @return
	 */
	private int[] checkForCalendars() {
		final String[] projection = new String[] { "_id", };
		final ContentResolver contentResolver = _thisContext
				.getContentResolver();
		final Cursor result = contentResolver.query(
				Uri.parse("content://com.android.calendar/calendars"),
				projection, null, null, null);
		final int[] calIds = new int[result.getCount()];
		if (result.moveToFirst()) {
			for (int i = 0; i < result.getCount(); i++) {
				calIds[i] = result.getInt(0);
				result.moveToNext();
			}
		}
		result.close();
		return calIds;
	}

	@Override
	protected void onDestroy() {
		if (_dialog != null) {
			_dialog.dismiss();
			_dialog = null;
		}
		super.onDestroy();
	}

	/**
	 * Display dialog
	 * 
	 * @param data
	 *            - the string to diaplay
	 */
	public void displayDialog(String data) {
		_dialog = new Dialog(this);
		_dialog.setContentView(R.layout.dialog_notification);
		_dialog.setCancelable(true);
		// set the text
		TextView text = (TextView) _dialog
				.findViewById(R.id.dialog_notification_text);
		text.setText(data);
		_dialog.show();
		Button cancel = (Button) _dialog
				.findViewById(R.id.dialog_notification_cancel_button);
		cancel.setOnClickListener(new OnClickListener() {
			// if cancel pressed, close dialog
			@Override
			public void onClick(View v) {
				_dialog.dismiss();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem grades = menu.findItem(R.id.menu_grades);
		MenuItem exams = menu.findItem(R.id.menu_exams);
		MenuItem maps = menu.findItem(R.id.menu_maps);
		MenuItem lessons = menu.findItem(R.id.menu_lessons);
		menu.removeItem(R.id.menu_settings);
		grades.setIntent(new Intent(this, GradesActivity.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK));
		exams.setIntent(new Intent(this, ExamsActivity.class)
		.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK));
		maps.setIntent(new Intent(this, MapsActivity.class)
		.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK));
		lessons.setIntent(new Intent(this, LessonsActivity.class)
		.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK));
		return true;
	}

	@Override
	public void onActivityResult(int reqCode, int resCode, Intent data) {
		if (reqCode == 1 && resCode == Activity.RESULT_OK) {
			SyncTask syncTask = new SyncTask();
			HujiSync sync = new HujiSync(this);
			Sync[] syncs = new Sync[] { sync.gradesSync, sync.lessonsSync,
					sync.examsSync };
			syncTask.execute(syncs);
		} else if (reqCode == 2 && resCode == Activity.RESULT_OK) {
			SyncTask syncTask = new SyncTask();
			HujiSync sync = new HujiSync(this);
			syncTask.execute(sync.examsSync);
		} else if (reqCode == 3 && resCode == Activity.RESULT_OK) {
			SyncTask syncTask = new SyncTask();
			HujiSync sync = new HujiSync(this);
			syncTask.execute(sync.lessonsSync);
		} else if (reqCode == 4 && resCode == Activity.RESULT_OK) {
			SyncTask syncTask = new SyncTask();
			HujiSync sync = new HujiSync(this);
			syncTask.execute(sync.gradesSync);
		} else if ((reqCode == 5 || reqCode == 6 || reqCode == 7)
				&& resCode == Activity.RESULT_OK) {
			displayDialog(_thisContext
					.getString(R.string.message_operation_succeed));
		}
	}

	class SyncTask extends AsyncTask<Sync, Void, Boolean> {
		private ProgressDialog _dialog;
		private TextView _text;

		@Override
		protected Boolean doInBackground(Sync... params) {
			for (Sync param : params) {
				if (!param.sync()) {
					return false;
				}
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			_dialog.dismiss();
			if (!result) {
				displayDialog(_sessionToHuji.getLastMessage());
			} else {
				displayDialog(getString(R.string.message_sync_succed));
			}

		}

		@Override
		protected void onPreExecute() {
			_dialog = new ProgressDialog(Preferences.this);
			_dialog.show();
			_dialog.setContentView(R.layout.io_progress);
			_dialog.setCancelable(false);
			_text = (TextView) _dialog.findViewById(R.id.io_progress_text);
			_text.setTextAppearance(getApplicationContext(),
					R.style.SpecialText);
			_text.setText(getString(R.string.message_sync));
		}
	}

}