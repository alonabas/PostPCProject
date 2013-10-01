package il.ac.huji.hujime;

import il.ac.huji.hujime.Exams.Exam;
import il.ac.huji.hujime.Lessons.Lesson;
import il.ac.huji.hujime.Lessons.Semester;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * Class the define sync with calendar Add items to calendar and delete them
 * 
 * @author alonaba
 * 
 */
public class CalendarSync {
	Context _context;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            - The context
	 */
	public CalendarSync(Context context) {
		_context = context;
	}

	/**
	 * Method that defines calendar objects from Exams in DB and adds them to
	 * calendar
	 * 
	 * @param id
	 *            - the id of calendar
	 * @return true
	 */
	public boolean addExamsToCalendar(int id) {
		Database instance = Database.getInstance(_context);
		ArrayList<Exam> exams = instance.getExams();
		boolean status = true;
		for (Exam exam : exams) {
			ContentValues cv = new ContentValues();
			Calendar beginTime = Calendar.getInstance();
			String[] date = exam.getDate().split("/");
			if (exam.getTime() != null) {
				String[] time = exam.getTime().split(":");
				beginTime.set(Integer.valueOf(date[2]),
						Integer.valueOf(date[1]) - 1, Integer.valueOf(date[0]),
						Integer.valueOf(time[0]), Integer.valueOf(time[1]));
			} else {
				beginTime.set(Integer.valueOf(date[2]),
						Integer.valueOf(date[1]) - 1, Integer.valueOf(date[0]),
						9, 0);
			}
			String moed = null;
			switch (exam.getMoed()) {
			case 1:
				moed = _context.getString(R.string.moed_a);
				break;
			case 2:
				moed = _context.getString(R.string.moed_b);
				break;
			case 3:
				moed = _context.getString(R.string.moed_c);
				break;
			}
			String title = "מועד " + moed + " ב" + exam.getName();
			// to identify the entries
			String description = "added by hujiMe Exams";
			cv.put("title", title);
			removeExamsIfExists(title, description, id,
					beginTime.getTimeInMillis());
			cv.put("calendar_id", id);
			cv.put("description", description);
			if (exam.getPlace() != null) {
				cv.put("eventLocation", exam.getPlace());
			}
			cv.put("eventTimezone", TimeZone.getDefault().getDisplayName());
			cv.put("dtstart", beginTime.getTimeInMillis());
			cv.put("duration", "PT3H");
			cv.put("allDay", 0); // true = 1, false = 0
			cv.put("hasAlarm", 1);
			// once desired fields are set, insert it into the table
			Uri l_uri = _context.getContentResolver().insert(
					Uri.parse("content://com.android.calendar/events"), cv);
			if (l_uri != null) {
				ContentValues reminders = new ContentValues();
				reminders.put("event_id",
						Long.parseLong(l_uri.getLastPathSegment()));
				reminders.put("method", 1);
				reminders.put("minutes", 60 * 12);
				Uri uri2 = _context.getContentResolver().insert(
						Uri.parse("content://com.android.calendar/reminders"),
						reminders);
				if (uri2 == null) {
					status = false;
				}
			} else {
				status = false;

			}

		}
		return status;
	}

	/**
	 * Get the first day of lesson, according to semester's start and day in
	 * week when lesson occurs
	 * 
	 * @param start
	 *            - start date of semester
	 * @param day
	 *            - day that with receive the date of first lesson
	 * @param i
	 *            - the day in week of lesson
	 */
	private void getFirstDay(Calendar start, Calendar day, int i) {
		Log.d("start day in start of function",day.get(Calendar.DAY_OF_MONTH)+" "+day.get(Calendar.MONTH)+" "+day.get(Calendar.YEAR));
		if (start.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			day.set(Calendar.DAY_OF_WEEK, i + 1);
			Log.d("start day",day.get(Calendar.DAY_OF_MONTH)+" "+day.get(Calendar.MONTH)+" "+day.get(Calendar.YEAR));
		} else if (start.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
			if (i == 0) {
				day.add(Calendar.WEEK_OF_MONTH, 1);
			}
			day.set(Calendar.DAY_OF_WEEK, i + 1);
		} else if (start.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
			if (i == 0 || i == 1) {
				day.add(Calendar.WEEK_OF_MONTH, 1);
			}
			day.set(Calendar.DAY_OF_WEEK, i + 1);
		} else if (start.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
			if (i == 0 || i == 1 || i == 2) {
				day.add(Calendar.WEEK_OF_MONTH, 1);
			}
			day.set(Calendar.DAY_OF_WEEK, i + 1);
		} else if (start.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
			if (i == 0 || i == 1 || i == 2 || i == 3) {
				day.add(Calendar.WEEK_OF_MONTH, 1);
			}
			day.set(Calendar.DAY_OF_WEEK, i + 1);
		} else if (start.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY
				|| start.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			day.add(Calendar.WEEK_OF_MONTH, 1);
			day.set(Calendar.DAY_OF_WEEK, i + 1);
		}
	}

	/**
	 * Method that get lessons from local DB and adds them to calendar
	 * 
	 * @param id
	 *            - the id of calendar
	 * @param semester
	 *            - the semester
	 * @return true, if
	 */
	public boolean addLessonsToCalendar(int id, Semester semester) {
		Database instance = Database.getInstance(_context);
		Calendar start = null;
		Calendar end = null;
		switch (semester) {
		case A:
			start = instance.getDate("start_first_semester");
			end = instance.getDate("end_first_semester");
			break;
		case B:
			start = instance.getDate("start_second_semester");
			end = instance.getDate("end_second_semester");
			break;
		}
		boolean status = true;
		Calendar startDay = (Calendar) start.clone();
		for (int i = 0; i < 5; i++) {
			Lesson[] lessons = null;
			switch (semester) {
			case A:
				lessons = instance.getLessonsDay(Semester.A, i);
				break;
			case B:
				lessons = instance.getLessonsDay(Semester.B, i);
				break;
			}
			Log.d("start semester",start.get(Calendar.DAY_OF_MONTH)+" "+start.get(Calendar.MONTH)+" "+start.get(Calendar.YEAR));
			getFirstDay(start, startDay, i);
			Log.d("start day",startDay.get(Calendar.DAY_OF_MONTH)+" "+startDay.get(Calendar.MONTH)+" "+startDay.get(Calendar.YEAR));
			for (int j = 0; j < lessons.length; j++) {
				int hours = 0;
				if (lessons[j] != null) {
					while (lessons[j + hours] != null
							&& lessons[j + hours].equals(lessons[j])) {
						hours++;
					}
					ContentValues cv = new ContentValues();
					String title = null;
					switch (lessons[j].getType()) {
					case 0:
						title = "שיעור ב" + lessons[j].getName();
						break;
					case 1:
						title = "מעבדה ב" + lessons[j].getName();
						break;
					case 2:
						title = "תרגול ב" + lessons[j].getName();
						break;
					case 3:
						title = "הדרכה ב" + lessons[j].getName();
						break;
					case 4:
						title = "מכינה ב" + lessons[j].getName();
						break;
					case 5:
						title = "סדנה ב" + lessons[j].getName();
						break;
					case 6:
						title = "סמינריון ב" + lessons[j].getName();
						break;
					case 7:
						title = "מטלה ב" + lessons[j].getName();
						break;
					case 8:
						title = "סיור ב" + lessons[j].getName();
						break;
					case 9:
						title = "שיעור-סמינריון ב" + lessons[j].getName();
						break;
					default:
						title = lessons[j].getName();
					}
					int hourStart = lessons[j].getStartHour();
					startDay.set(Calendar.HOUR_OF_DAY, hourStart);
					startDay.set(Calendar.MINUTE, 0);
					String description = "added by hujiMe Lessons";
					removeLessonsIfExists(title, description, id,
							startDay.getTimeInMillis());
					cv.put("title", title);
					cv.put("calendar_id", id);
					cv.put("description", description);
					cv.put("eventLocation", lessons[j].getPlace());
					cv.put("eventTimezone", TimeZone.getDefault()
							.getDisplayName());
					// set start time
					cv.put("dtstart", startDay.getTimeInMillis());
					cv.put("duration", "PT" + hours + "H");
					cv.put("allDay", 0); // true = 1, false = 0
					cv.put("hasAlarm", 0);
					int month = end.get(Calendar.MONTH) + 1;
					String monthStr = String.valueOf(month);
					if (monthStr.length() == 1) {
						monthStr = "0" + monthStr;
					}
					int day = end.get(Calendar.DAY_OF_MONTH) + 1;
					String dayStr = String.valueOf(day);
					if (dayStr.length() == 1) {
						dayStr = "0" + dayStr;
					}
					cv.put("rrule",
							"FREQ=WEEKLY;UNTIL=" + end.get(Calendar.YEAR) + ""
									+ monthStr + "" + dayStr);
					// once desired fields are set, insert it into the table
					Uri l_uri = _context.getContentResolver().insert(
							Uri.parse("content://com.android.calendar/events"),
							cv);
					if (l_uri != null) {
						status = false;
					}
					j = j + hours - 1;
				}

			}
		}
		return status;
	}

	/**
	 * Delete Exams from calendar deletes all by description
	 * "Added by HujiMe Exams"
	 */
	public void deleteExams(int id) {
		_context.getContentResolver().delete(
				Uri.parse("content://com.android.calendar/events"),
				"calendar_id=? and description=?",
				new String[] { String.valueOf(id), "added by hujiMe Exams" });
	}

	/**
	 * Delete Lessons from calendar deletes all by description
	 * "Added by HujiMe Lessons"
	 */
	public void deleteLessons(int id) {
		_context.getContentResolver().delete(
				Uri.parse("content://com.android.calendar/events"),
				"calendar_id=? and description=?",
				new String[] { String.valueOf(id), "added by hujiMe Lessons" });
	}

	/**
	 * Check if exam with supplied name and start time already in calendar
	 * 
	 * @param title
	 *            - exam's title
	 * @param description
	 *            - the description
	 * @param calId
	 *            - ID of calendar
	 * @param dtstart
	 *            - start time
	 */
	@SuppressWarnings("deprecation")
	public void removeExamsIfExists(String title, String description,
			int calId, long dtstart) {

		Cursor cursor = _context.getContentResolver().query(
				Uri.parse("content://com.android.calendar/events"),
				new String[] { "_id", "dtstart" },
				"calendar_id=? and description=? and title=?",
				new String[] { String.valueOf(calId), description, title },
				null);
		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {
				Date date = new Date(cursor.getLong(cursor
						.getColumnIndex("dtstart")));
				Date newDate = new Date(dtstart);
				// check if is same date if yes delete
				if (date.getYear() == newDate.getYear()
						&& date.getMonth() == newDate.getMonth()
						&& date.getDay() == newDate.getDay()) {
					// delete occurance
					_context.getContentResolver().delete(
							Uri.parse("content://com.android.calendar/events"),
							"calendar_id=? and _id=?",
							new String[] {
									String.valueOf(calId),
									String.valueOf(cursor.getInt(cursor
											.getColumnIndex("_id"))) });

				}
				cursor.moveToNext();
			}
			cursor.close();
			return;
		}
	}

	/**
	 * Check if lesson with supplied name and start time already in calendar
	 * 
	 * @param title
	 *            - lesson's title
	 * @param description
	 *            - the description
	 * @param calId
	 *            - ID of calendar
	 * @param dtstart
	 *            - start time
	 */
	@SuppressWarnings("deprecation")
	public void removeLessonsIfExists(String title, String description,
			int calId, long dtstart) {

		Cursor cursor = _context.getContentResolver().query(
				Uri.parse("content://com.android.calendar/events"),
				new String[] { "_id", "dtstart" },
				"calendar_id=? and description=? and title=?",
				new String[] { String.valueOf(calId), description, title },
				null);
		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {
				// check if is same date if yes delete
				Date curDate = new Date(dtstart);
				Date existingDate = new Date(cursor.getLong(cursor
						.getColumnIndex("dtstart")));
				if (curDate.getYear() == existingDate.getYear()
						&& curDate.getMonth() == existingDate.getMonth()
						&& curDate.getDay() == existingDate.getDay()
						&& curDate.getHours() == existingDate.getHours()) {
					// delete occurance
					_context.getContentResolver().delete(
							Uri.parse("content://com.android.calendar/events"),
							"calendar_id=? and _id=?",
							new String[] {
									String.valueOf(calId),
									String.valueOf(cursor.getInt(cursor
											.getColumnIndex("_id"))) });

				}
				cursor.moveToNext();
			}
			cursor.close();
			return;
		}
	}
}
