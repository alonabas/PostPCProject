package il.ac.huji.hujime;

import il.ac.huji.hujime.Exams.Exam;
import il.ac.huji.hujime.Grades.Grade;
import il.ac.huji.hujime.Lessons.Lesson;
import il.ac.huji.hujime.Lessons.Semester;
import il.ac.huji.hujime.Maps.Campus;
import il.ac.huji.hujime.Maps.Location;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database class
 * 
 * @author alonaba
 * 
 */
public class Database extends SQLiteOpenHelper {
	private static Database _instance = null;

	private static final String DATABASE_NAME = "huji_data.db";

	// grades table
	private static final String DATABASE_TABLE_GRADES = "grades";
	private static final String DATABASE_TABLE_EXAMS = "exams";
	private static final String DATABASE_TABLE_LESSONS_A = "lessons_a";
	private static final String DATABASE_TABLE_LESSONS_B = "lessons_b";
	private static final String DATABASE_DATES = "dates";
	private static final String DATABASE_LOCATIONS = "locations";

	private static final String ID = "_id";
	private static final String NAME = "_course_name";
	private static final String GRADE = "_grade";
	private static final String POINTS = "_points";
	private static final String START_TIME = "_start_time";
	private static final String PLACE = "_place";
	private static final String MOED = "_moed";
	private static final String SPECIAL_PLACE = "_special_place";
	private static final String TIME = "_time";
	private static final String DAY = "_day";
	private static final String DATE = "_date";
	private static final String TYPE = "_type";

	private static final String MONTH = "_month";
	private static final String YEAR = "_YEAR";

	private static final String LONGITUDE = "_longitude";
	private static final String LATITUDE = "_latitude";
	private static final String CAMPUS = "_campus";
	private static final String LOCATION_NAME = "_location_name";

	private static final int DATABASE_VERSION = 1;

	private final static String _CREATE_EXAMS = "create table if not exists "
			+ DATABASE_TABLE_EXAMS + " (" + ID + " text, " + MOED + " integer,"
			+ NAME + " text, " + DATE + " text, " + TIME + " text, " + PLACE
			+ " text," + SPECIAL_PLACE + " text,UNIQUE(" + ID + ", " + MOED
			+ ") ON CONFLICT REPLACE);";
	private final static String _CREATE_GRADES = "create table if not exists "
			+ DATABASE_TABLE_GRADES + " (" + ID + " text primary key, " + NAME
			+ " text, " + POINTS + " integer, " + YEAR + " integer, " + GRADE
			+ " integer);";
	private final static String _CREATE_LESSONS_A = "create table if not exists "
			+ DATABASE_TABLE_LESSONS_A
			+ " ("
			+ ID
			+ " text, "
			+ NAME
			+ " text, "
			+ START_TIME
			+ " integer, "
			+ PLACE
			+ " text, "
			+ DAY
			+ " integer,"
			+ TYPE
			+ " integer,UNIQUE("
			+ ID
			+ ", "
			+ START_TIME
			+ ", "
			+ TYPE
			+ ", "
			+ DAY
			+ ", "
			+ PLACE
			+ ") ON CONFLICT REPLACE);";
	private final static String _CREATE_LESSONS_B = "create table if not exists "
			+ DATABASE_TABLE_LESSONS_B
			+ " ("
			+ ID
			+ " text, "
			+ NAME
			+ " text, "
			+ START_TIME
			+ " integer, "
			+ PLACE
			+ " text, "
			+ DAY
			+ " integer,"
			+ TYPE
			+ " integer,UNIQUE("
			+ ID
			+ ", "
			+ START_TIME
			+ ", "
			+ TYPE
			+ ", "
			+ DAY
			+ ", "
			+ PLACE
			+ ") ON CONFLICT REPLACE);";
	private final static String _CREATE_DATES = "create table if not exists "
			+ DATABASE_DATES + " (" + NAME + " text primary key, " + YEAR
			+ " integer, " + MONTH + " integer, " + DAY + " integer);";
	private final static String _CREATE_LOCATIONS = "create table if not exists "
			+ DATABASE_LOCATIONS
			+ " ("
			+ LONGITUDE
			+ " real, "
			+ LATITUDE
			+ " real, "
			+ CAMPUS
			+ " integer, "
			+ LOCATION_NAME
			+ " text,UNIQUE("
			+ LOCATION_NAME
			+ ", "
			+ CAMPUS
			+ ") ON CONFLICT REPLACE);";

	/**
	 * Constructor
	 * 
	 * @param context
	 *            - the context
	 */
	private Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Singleton
	 * 
	 * @param context
	 *            - context
	 * @return - return unique object of database
	 */
	public static Database getInstance(Context context) {

		// Use the application context, which will ensure that you
		// don't accidentally leak an Activity's context.
		// See this article for more information: http://bit.ly/6LRzfx
		if (_instance == null) {
			_instance = new Database(context.getApplicationContext());
		}
		return _instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// create table of grades
		db.execSQL(_CREATE_GRADES);
		db.execSQL(_CREATE_EXAMS);

		db.execSQL(_CREATE_LESSONS_A);
		db.execSQL(_CREATE_LESSONS_B);
		db.execSQL(_CREATE_DATES);
		db.execSQL(_CREATE_LOCATIONS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_GRADES);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_EXAMS);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_LESSONS_A);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_LESSONS_B);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_DATES);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_LOCATIONS);
		onCreate(db);
	}

	/**
	 * Method that creates tables if they doesn't exists
	 */
	public void createIfNotExists() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(_CREATE_GRADES);
		db.execSQL(_CREATE_EXAMS);
		db.execSQL(_CREATE_LESSONS_A);
		db.execSQL(_CREATE_LESSONS_B);
		db.execSQL(_CREATE_DATES);
		db.execSQL(_CREATE_LOCATIONS);
		db.close();

	}

	/**
	 * Method that deletes tables
	 */
	public void deleteDbs() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_GRADES);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_EXAMS);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_LESSONS_A);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_LESSONS_B);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_DATES);
		db.close();
	}

	/**
	 * Add locations to database
	 * 
	 * @param locations
	 *            - the list of locations
	 */
	public void addLocations(ArrayList<Location> locations) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(_CREATE_LOCATIONS);
		ContentValues values = new ContentValues();
		for (Location location : locations) {
			values.put(LOCATION_NAME, location.getPlace());
			values.put(CAMPUS, location.getCampus());
			values.put(LATITUDE, location.getLatitude());
			values.put(LONGITUDE, location.getLongitude());
			db.insertWithOnConflict(DATABASE_LOCATIONS, null, values,
					SQLiteDatabase.CONFLICT_REPLACE);
		}
		db.close();
	}

	/**
	 * Get the location of building from db according to it's name and campus
	 * 
	 * @param name
	 *            - name of building
	 * @param campus
	 *            - campus
	 * @return Location object if found or null
	 */
	public Location queryForPlace(String name, int campus) {
		String selectAll = "SELECT  * FROM " + DATABASE_LOCATIONS + " WHERE "
				+ LOCATION_NAME + " = '" + name + "' AND " + CAMPUS + " = "
				+ campus;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectAll, null);
		Location location = null;
		if (cursor.moveToFirst()) {
			do {
				location = new Location(cursor.getString(cursor
						.getColumnIndex(LOCATION_NAME)),
						Campus.values()[cursor.getInt(cursor
								.getColumnIndex(CAMPUS))],
						cursor.getDouble(cursor.getColumnIndex(LONGITUDE)),
						cursor.getDouble(cursor.getColumnIndex(LATITUDE)));
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return location;
	}

	/**
	 * Method that adds grades to DB
	 * 
	 * @param grades
	 *            - list of grades
	 */
	public void addGrades(ArrayList<Grade> grades) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(_CREATE_GRADES);
		ContentValues values = new ContentValues();
		for (Grade grade : grades) {
			values.put(ID, grade.getId());
			values.put(NAME, grade.getName());
			values.put(POINTS, grade.getPoints());
			values.put(GRADE, grade.getGrade());
			values.put(YEAR, grade.getYear());
			db.insertWithOnConflict(DATABASE_TABLE_GRADES, null, values,
					SQLiteDatabase.CONFLICT_REPLACE);
		}
		db.close(); // Closing database connection
	}

	/**
	 * Method that adds critic days to DB, as start and end of semesters
	 * 
	 * @param dates
	 *            - 2D array of dates
	 */
	public void addDates(int[][] dates) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(_CREATE_DATES);
		ContentValues values = new ContentValues();
		for (int i = 0; i < dates.length; i++) {
			switch (i) {
			case 0:
				values.put(NAME, "start_first_semester");
				break;
			case 1:
				values.put(NAME, "end_first_semester");
				break;
			case 2:
				values.put(NAME, "start_second_semester");
				break;
			case 3:
				values.put(NAME, "end_second_semester");
				break;
			}
			values.put(YEAR, dates[i][2]);
			values.put(MONTH, dates[i][1]);
			values.put(DAY, dates[i][0]);
			db.insertWithOnConflict(DATABASE_DATES, null, values,
					SQLiteDatabase.CONFLICT_REPLACE);
		}
		db.close(); // Closing database connection
	}

	/**
	 * Method that retrieve specific date from database
	 * 
	 * @param type
	 *            - the type of date, start first semester, end of first
	 *            semester and so on
	 * @return return the calendar object with asked date
	 */
	public Calendar getDate(String type) {
		String selectAll = "SELECT  * FROM " + DATABASE_DATES + " WHERE "
				+ NAME + " = '" + type + "'";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectAll, null);
		Calendar cal = Calendar.getInstance();
		cal = (Calendar) cal.clone();
		if (cursor.moveToFirst()) {
			int year = cursor.getInt(cursor.getColumnIndex(YEAR));
			int month = cursor.getInt(cursor.getColumnIndex(MONTH));
			int day = cursor.getInt(cursor.getColumnIndex(DAY));
			cal.set(year, month - 1, day);
		}
		db.close();
		return cal;
	}

	/**
	 * Method that adds exams to db
	 * 
	 * @param exams
	 *            - the list of exams
	 */
	public void addExams(ArrayList<Exam> exams) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(_CREATE_EXAMS);
		ContentValues values = new ContentValues();
		String time;
		String place;
		String specialPlace;
		for (Exam exam : exams) {
			values.put(ID, exam.getId());
			values.put(NAME, exam.getName());
			values.put(MOED, exam.getMoed());
			values.put(DATE, exam.getDate());
			time = exam.getTime();
			if (time != null) {
				values.put(TIME, time);
			} else {
				values.putNull(TIME);
			}
			place = exam.getPlace();
			if (place != null) {
				values.put(PLACE, place);
			} else {
				values.putNull(PLACE);
			}
			specialPlace = exam.getSpecialPlace();
			if (specialPlace != null) {
				values.put(SPECIAL_PLACE, specialPlace);
			} else {
				values.putNull(SPECIAL_PLACE);
			}
			db.insert(DATABASE_TABLE_EXAMS, null, values);
		}
		db.close(); // Closing database connection
	}

	/**
	 * Get exams from db
	 * 
	 * @return - the list of exams
	 */
	public ArrayList<Exam> getExams() {
		ArrayList<Exam> examsList = new ArrayList<Exam>();
		String selectAll = "SELECT  * FROM " + DATABASE_TABLE_EXAMS;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectAll, null);
		if (cursor.moveToFirst()) {
			do {
				Exam exam = new Exam(
						cursor.getString(cursor.getColumnIndex(ID)),
						cursor.getString(cursor.getColumnIndex(NAME)),
						cursor.getInt(cursor.getColumnIndex(MOED)),
						cursor.getString(cursor.getColumnIndex(DATE)),
						cursor.getString(cursor.getColumnIndex(TIME)),
						cursor.getString(cursor.getColumnIndex(PLACE)),
						cursor.getString(cursor.getColumnIndex(SPECIAL_PLACE)));
				examsList.add(exam);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return examsList;

	}

	/**
	 * Method that retrieve list of grades from db
	 * 
	 * @return - list of grades
	 */
	public ArrayList<Grade> getGrades() {
		ArrayList<Grade> gradesList = new ArrayList<Grade>();
		String selectAll = "SELECT  * FROM " + DATABASE_TABLE_GRADES;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectAll, null);
		if (cursor.moveToFirst()) {
			do {
				Grade grade = new Grade(cursor.getString(cursor
						.getColumnIndex(NAME)), cursor.getInt(cursor
						.getColumnIndex(GRADE)), cursor.getInt(cursor
						.getColumnIndex(POINTS)), cursor.getString(cursor
						.getColumnIndex(ID)), cursor.getInt(cursor
						.getColumnIndex(YEAR)));
				gradesList.add(grade);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return gradesList;
	}

	/**
	 * Method that adds lessons to db
	 * 
	 * @param _lessons
	 *            - the 2d array of lessons
	 * @param semester
	 *            - semester
	 */
	public void addLessons(Lesson[][] _lessons, Semester semester) {
		String table = null;
		SQLiteDatabase db = this.getWritableDatabase();
		switch (semester) {
		case A:
			table = DATABASE_TABLE_LESSONS_A;
			db.execSQL(_CREATE_LESSONS_A);
			break;
		case B:
			table = DATABASE_TABLE_LESSONS_B;
			db.execSQL(_CREATE_LESSONS_A);
			break;
		}
		if (table == null) {
			db.close();
			return;
		}

		ContentValues values = new ContentValues();
		int duration;
		for (Lesson[] dayLessons : _lessons) {
			for (Lesson lesson : dayLessons) {
				if (lesson != null) {
					duration = lesson.getHours();
					for (int hour = 0; hour < duration; hour++) {
						values.put(ID, lesson.getId());
						values.put(NAME, lesson.getName());
						values.put(START_TIME, lesson.getStartHour() + hour);
						values.put(PLACE, lesson.getPlace());
						values.put(TYPE, lesson.getType());
						values.put(DAY, lesson.getDay());
						db.insert(table, null, values);
					}

				}
			}
		}
		db.close(); // Closing database connection
	}

	/**
	 * Update lesson in DB
	 * 
	 * @param semester
	 *            - the semester
	 * @param day
	 *            - day of lesson
	 * @param time
	 *            - time of lesson
	 * @param oldName
	 *            - the old name of lesson, as it was before change
	 * @param newName
	 *            - the new name of lesson
	 * @param place
	 *            - the new place of lesson
	 * @return true if update successful
	 */
	public boolean updateLesson(Semester semester, int day, int time,
			String oldName, String newName, String place) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues args = new ContentValues();
		args.put(NAME, newName);
		args.put(PLACE, place);
		int result;
		switch (semester) {
		case A:
			result = db.update(DATABASE_TABLE_LESSONS_A, args, DAY + "='" + day
					+ "' AND " + START_TIME + "='" + time + "' AND " + NAME
					+ "='" + oldName + "'", null);
			if (result == 0) {
				db.close();
				return false;
			}
			db.close();
			return true;
		case B:
			result = db.update(DATABASE_TABLE_LESSONS_B, args, DAY + "='" + day
					+ "' AND " + START_TIME + "='" + time + "' AND " + NAME
					+ "='" + oldName + "'", null);
			if (result == 0) {
				db.close();
				return false;
			}
			db.close();
			return true;
		}
		db.close();
		return false;
	}

	/**
	 * Method that removes lesson from database
	 * 
	 * @param semester
	 *            - semester
	 * @param day
	 *            - day of lesson
	 * @param time
	 *            - time of lesson
	 * @param name
	 *            - name of lesson
	 * @return true, if remove succeed
	 */
	public boolean removeLesson(Semester semester, int day, int time,
			String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		int result;
		switch (semester) {
		case A:
			result = db.delete(DATABASE_TABLE_LESSONS_A, DAY + "=" + day
					+ " AND " + START_TIME + "=" + time + " AND " + NAME + "='"
					+ name + "'", null);

			if (result == 0) {
				db.close();
				return false;
			}
			db.close();
			return true;
		case B:
			result = db.delete(DATABASE_TABLE_LESSONS_B, DAY + "=" + day
					+ " AND " + START_TIME + "=" + time + " AND " + NAME + "='"
					+ name + "'", null);
			if (result == 0) {
				db.close();
				return false;
			}
			db.close();
			return true;
		}
		db.close();
		return false;
	}

	/**
	 * Method that retrieves the list of lessons per semester
	 * 
	 * @param semester
	 *            - the semester
	 * @return list of lessons
	 */
	public ArrayList<Lesson> getLessons(Semester semester) {
		ArrayList<Lesson> lessonsList = new ArrayList<Lesson>();
		String selectAll = null;
		switch (semester) {
		case A:
			selectAll = "SELECT  * FROM " + DATABASE_TABLE_LESSONS_A
					+ " ORDER BY " + DAY;
			break;
		case B:
			selectAll = "SELECT  * FROM " + DATABASE_TABLE_LESSONS_B
					+ " ORDER BY " + DAY;
			break;
		}
		if (selectAll == null) {
			return null;
		}
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectAll, null);
		if (cursor.moveToFirst()) {
			do {
				Lesson lesson = new Lesson(cursor.getString(cursor
						.getColumnIndex(NAME)), cursor.getString(cursor
						.getColumnIndex(ID)), cursor.getInt(cursor
						.getColumnIndex(START_TIME)), cursor.getInt(cursor
						.getColumnIndex(TYPE)), cursor.getInt(cursor
						.getColumnIndex(DAY)), cursor.getString(cursor
						.getColumnIndex(PLACE)));
				lessonsList.add(lesson);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return lessonsList;
	}

	/**
	 * Method that gets the array of lessons per day
	 * 
	 * @param semester
	 *            - semester
	 * @param day
	 *            - day to find lessons for
	 * @return array of lessons
	 */
	public Lesson[] getLessonsDay(Semester semester, int day) {
		Lesson[] lessonsList = new Lesson[14];
		String selectAll = null;
		switch (semester) {
		case A:
			selectAll = "SELECT  * FROM " + DATABASE_TABLE_LESSONS_A
					+ " WHERE " + DAY + " = " + day + " ORDER BY " + START_TIME;
			break;
		case B:
			selectAll = "SELECT  * FROM " + DATABASE_TABLE_LESSONS_B
					+ " WHERE " + DAY + " = " + day + " ORDER BY " + START_TIME;
			break;
		}
		if (selectAll == null) {
			return null;
		}
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectAll, null);
		if (cursor.moveToFirst()) {
			do {
				int startHour = cursor
						.getInt(cursor.getColumnIndex(START_TIME));
				Lesson lesson = new Lesson(cursor.getString(cursor
						.getColumnIndex(NAME)), cursor.getString(cursor
						.getColumnIndex(ID)), startHour, cursor.getInt(cursor
						.getColumnIndex(TYPE)), day, cursor.getString(cursor
						.getColumnIndex(PLACE)));
				lessonsList[startHour - 8] = lesson;
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return lessonsList;
	}

	/**
	 * Method that gets the years, that there are grades for these years
	 * 
	 * @return ArrayList of years
	 */
	public ArrayList<Integer> getGradesYears() {
		String selectAll = "SELECT  " + YEAR + " FROM " + DATABASE_TABLE_GRADES;
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<Integer> years = new ArrayList<Integer>();
		Cursor cursor = db.rawQuery(selectAll, null);
		if (cursor.moveToFirst()) {
			do {
				int year = cursor.getInt(0);
				if (!years.contains(year)) {
					years.add(year);
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return years;
	}

	/**
	 * Method that retrieves grades by year
	 * 
	 * @param year
	 *            - the year to retrieve grades
	 * @return - the list of grades
	 */
	public ArrayList<Grade> getGradesByYear(int year) {
		ArrayList<Grade> gradesList = new ArrayList<Grade>();
		String selectAll = "SELECT  * FROM " + DATABASE_TABLE_GRADES
				+ " WHERE " + YEAR + " = " + year;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectAll, null);
		if (cursor.moveToFirst()) {
			do {
				Grade grade = new Grade(cursor.getString(cursor
						.getColumnIndex(NAME)), cursor.getInt(cursor
						.getColumnIndex(GRADE)), cursor.getInt(cursor
						.getColumnIndex(POINTS)), cursor.getString(cursor
						.getColumnIndex(ID)), cursor.getInt(cursor
						.getColumnIndex(YEAR)));
				gradesList.add(grade);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return gradesList;
	}

	/**
	 * Method that gets course id, according to it's name
	 * 
	 * @param name
	 *            - course name
	 * @return course id
	 */
	public String queryForCourseId(String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		String id = null;
		String selectAll = "SELECT " + ID + " FROM " + DATABASE_TABLE_LESSONS_A
				+ " WHERE " + NAME + " = '" + name + "'";
		Cursor cursor = db.rawQuery(selectAll, null);
		if (cursor.moveToFirst()) {
			id = cursor.getString(0);
		} else {
			selectAll = "SELECT  " + ID + " FROM " + DATABASE_TABLE_LESSONS_B
					+ " WHERE " + NAME + " = '" + name + "'";
			cursor = db.rawQuery(selectAll, null);
			if (cursor.moveToFirst()) {
				id = cursor.getString(0);
			}

		}
		cursor.close();
		db.close();
		return id;
	}
}
