package il.ac.huji.hujime;

import il.ac.huji.hujime.Lessons.Semester;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
 * Activity that display the dialog with calendars
 * @author alonaba
 *
 */
public class CalendarDialog extends Activity {
	int _type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_list);
		// get calendars
		Intent intent = getIntent();
		_type = intent.getIntExtra("type", -1);
		final String[] projection = new String[] { "_id", "account_name" };
		final ContentResolver contentResolver = CalendarDialog.this
				.getContentResolver();
		final Cursor result = contentResolver.query(
				Uri.parse("content://com.android.calendar/calendars"),
				projection, null, null, null);
		TextView text = (TextView) findViewById(R.id.dialog_list_text);
		text.setText(R.string.message_select_calendar);
		final String[] calNames = new String[result.getCount()];
		final int[] calIds = new int[result.getCount()];
		if (result.moveToFirst()) {
			for (int i = 0; i < result.getCount(); i++) {
				calIds[i] = result.getInt(result.getColumnIndex("_id"));
				calNames[i] = result.getString(result
						.getColumnIndex("account_name"));
				result.moveToNext();
			}
		}
		result.close();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.one_calendar, calNames);
		final ListView list = (ListView) findViewById(R.id.dialog_list_list);
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		list.setAdapter(adapter);
		final CalendarSync sync = new CalendarSync(this);
		// on CANCEL button click action
		findViewById(R.id.dialog_list_close).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View view) {
						// do nothing
						setResult(RESULT_CANCELED);
						finish();
					}
				});
		// on OK button click action
		findViewById(R.id.dialog_list_enter).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View view) {
						int count = list.getCount();
						SparseBooleanArray sparseBooleanArray = list
								.getCheckedItemPositions();
						for (int i = 0; i < count; i++) {
							if (sparseBooleanArray.get(i)) {
								switch (_type) {
								case 0:
									sync.addExamsToCalendar(calIds[i]);
									sync.addLessonsToCalendar(calIds[i],
											Semester.A);
									sync.addLessonsToCalendar(calIds[i],
											Semester.B);
									break;
								case 1:
									sync.addLessonsToCalendar(calIds[i],
											Semester.A);
									sync.addLessonsToCalendar(calIds[i],
											Semester.B);
									break;
								case 2:
									sync.addExamsToCalendar(calIds[i]);
									break;
								}

							}
						}
						// set result falue and return
						setResult(RESULT_OK);
						finish();
					}
				});
	}
}
