package il.ac.huji.hujime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * Class that get lessons table from huji web site, parse them to objects and
 * add to db
 * 
 * @author alonaba
 * 
 */
public class Lessons {

	private Lesson[][] _lessons;
	private Whitelist _whitelist;
	private Database _myDB;
	private HujiSession _sessionToHuji;
	private final static String _ssl_huji = "https://www.huji.ac.il";
	private final static String _TAB_SPACE = "&nbsp;&nbsp;&nbsp;&nbsp;";
	private final static String _SAME_LESSON = "כנ&quot;ל";
	private final static String _SHIUR = "שעור";
	private final static String _TIRGUL = "תרג";
	private final static String _MAABADA = "מעב";
	private final static String _HADRAHA = "הדר";
	private final static String _MEHINA = "מכי";
	private final static String _SADNA = "סדנה";
	private final static String _SEMINARION = "סמ";
	private final static String _MATALA = "מטלה";
	private final static String _SIUR = "סיור";
	private final static String _SHOS = "שוס";

	private int[][] _dates;

	public static enum Type {
		Shiur, Maabada, Tirgul, Hadraha, Mehina, Sadna, Seminarion, Matala, Siur, SiurSeminarion, None
	}

	public static enum Day {
		Thursday(4), Wednesday(3), Tuesday(2), Monday(1), Sunday(0);
		int _number;

		Day(int number) {
			_number = number;
		}

		public int getDayInteger() {
			return _number;
		}
	}

	public static enum Semester {
		A, B
	}

	/**
	 * Lesson object
	 * 
	 * @author alonaba
	 * 
	 */
	public static class Lesson {
		private String _name;
		private String _id;
		private Day _day;
		private int _startHour;
		private int _hours;
		private String _url;
		private String _place;
		private Type _type;

		/**
		 * Change name of Lesson
		 * 
		 * @param name
		 *            new name of lesson
		 */
		public void changeName(String name) {
			if (name == null) {
				return;
			}
			if (!name.equals("")) {
				_name = name;
			}
		}

		/**
		 * Change place of Lesson
		 * 
		 * @param place
		 *            - the new place
		 */
		public void changePlace(String place) {
			if (place == null) {
				return;
			}
			if (!place.equals("")) {
				_place = place;
			}
		}

		/**
		 * Constructor
		 * 
		 * @param name
		 *            - lesson name
		 * @param id
		 *            - course id
		 * @param startTime
		 *            - start time of lesson
		 * @param type
		 *            - type of lesson
		 * @param day
		 *            - day of lesson
		 * @param place
		 *            - place of lesson
		 */
		public Lesson(String name, String id, int startTime, int type, int day,
				String place) {
			_name = name;
			_place = place;
			_id = id;
			_type = Type.values()[type];
			_day = Day.values()[4 - day];
			_startHour = startTime;
		}

		/**
		 * Clone constructor
		 * 
		 * @param otherLesson
		 *            - Lesson to clone from
		 */
		public Lesson(Lesson otherLesson) {
			_name = otherLesson._name;
			_place = otherLesson._place;
			_id = otherLesson._id;
			_type = otherLesson._type;
			_day = otherLesson._day;
			_startHour = otherLesson._startHour;
		}

		/**
		 * Constructor
		 * 
		 * @param name
		 *            - name of lesson
		 * @param url
		 *            - url in shnaton
		 * @param place
		 *            - place of lesson
		 * @param type
		 *            - type of lesson
		 * @param day
		 *            - day of lesson
		 * @param startHour
		 *            - start hour of lesson
		 */
		public Lesson(String name, String url, String place, int type, int day,
				int startHour) {
			_name = name;
			_place = place;
			_url = url;
			_type = Type.values()[type];
			_day = Day.values()[day];
			_startHour = startHour + 8;
			_hours = 1;
		}

		/**
		 * Set id of lesson
		 * 
		 * @param id
		 *            - the id
		 */
		public void setId(String id) {
			_id = id;
		}

		/**
		 * Set duration of lesson
		 * 
		 * @param hours
		 *            - number of hours
		 */
		public void setHours(int hours) {
			_hours = hours;
		}

		/**
		 * Get Course id
		 * 
		 * @return id
		 */
		public String getId() {
			return _id;
		}

		/**
		 * Get name of lesson
		 * 
		 * @return name
		 */
		public String getName() {
			return _name;
		}

		/**
		 * Get type of lesson
		 * 
		 * @return type
		 */
		public int getType() {
			return _type.ordinal();
		}

		/**
		 * Get place of lesson
		 * 
		 * @return place
		 */
		public String getPlace() {
			return _place;
		}

		/**
		 * Get start hour of lesson
		 * 
		 * @return start hour
		 */
		public int getStartHour() {
			return _startHour;
		}

		/**
		 * Get duration of lesson
		 * 
		 * @return duration
		 */
		public int getHours() {
			return _hours;
		}

		/**
		 * Get day lesson is
		 * 
		 * @return day
		 */
		public int getDay() {
			return _day.getDayInteger();
		}

		@Override
		public boolean equals(Object obj) {
			if (getClass() != obj.getClass()) {
				return false;
			} else if (_name.equals(((Lesson)obj)._name)
					&& _id.equals(((Lesson) obj)._id)
					&& _type == ((Lesson) obj)._type
					&& _place.equals(((Lesson) obj)._place)
					&& _day == ((Lesson) obj)._day) {
				return true;
			}
			return false;
		}
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 *            - context
	 */
	public Lessons(Context context) {
		_whitelist = Whitelist.none();
		_whitelist.addTags("table", "td", "tr", "a", "br");
		_whitelist.addAttributes("td", "class");
		_whitelist.addAttributes("a", "href");
		_lessons = new Lesson[6][13];
		_sessionToHuji = HujiSession.getInstance();
		_myDB = Database.getInstance(context);
		_dates = null;
	}

	/**
	 * Get lessons from Huji session and parse them to objects
	 * 
	 * @param semester
	 *            - the semester to retrieve lessons for
	 * @return status, if action succeeded true
	 */
	public boolean getLessons(Semester semester) {
		String session = _sessionToHuji.getSession();
		String path = null;
		switch (semester) {
		case A:
			path = _ssl_huji + session + Path.PATH_LESSONS_SEMESTER_A.getPath();
			break;
		case B:
			path = _ssl_huji + session + Path.PATH_LESSONS_SEMESTER_B.getPath();
			break;
		}
		String html = _sessionToHuji.getSecureHtml(HujiSession.Method.Get,
				path, null);
		if (html != null) {
			html = Jsoup.clean(html, _whitelist);
			Document document = Jsoup.parse(html);
			for (int i = 0; i < _lessons.length; i++) {
				Arrays.fill(_lessons[i], null);
			}
			parseLessons(document);
			return true;
		}
		return false;
	}

	/**
	 * Parse lessons from html documents
	 * 
	 * @param document
	 *            - the html document
	 */
	private void parseLessons(Document document) {
		Elements titles = document.getElementsByClass("tableTitle");
		Element table = titles.first().parent().parent();
		Elements rows = table.getElementsByTag("tr");
		// remove titles line
		rows.remove(0);
		rows.remove(0);
		ArrayList<int[]> hasLesson = new ArrayList<int[]>();
		Element cell;
		// run over table of grades
		for (int hours = 0; hours < rows.size(); hours++) {
			Elements columns = rows.get(hours).getElementsByTag("td");
			for (int days = 0; days < columns.size() - 1; days++) {
				cell = columns.get(days);
				if (parseCell(cell, days, hours)) {
					int[] myIndex = { days, hours };
					hasLesson.add(myIndex);
				}
			}
		}
	}

	/**
	 * Parse cell in html table, that contain one lesson, or empty if no lesson
	 * 
	 * @param cell
	 *            - the cell to parse
	 * @param days
	 *            - the column in table, that represent day
	 * @param hours
	 *            - the row in table, that represent time interval
	 * @return
	 */
	private boolean parseCell(Element cell, int days, int hours) {
		Pattern pattern = Pattern.compile("(\'.+\')");
		String data = cell.html();
		Matcher matcher;
		Whitelist whitelist = new Whitelist();
		data = Jsoup.clean(data, whitelist);
		// if is same lesson
		if (data.equals(_SAME_LESSON)) {
			int count = 1;
			for (int curHour = hours; curHour >= 0; curHour--) {
				if (_lessons[days][curHour] == null) {
					count++;
				} else {
					_lessons[days][curHour].setHours(count);
					break;
				}
			}
			return false;
		} else if (data.equals(_TAB_SPACE)) {
			return false;
		} else {
			String href = cell.getElementsByTag("a").first().attr("href");
			matcher = pattern.matcher(href);
			if (matcher.find()) {
				String[] placeVsName = data.split(_TAB_SPACE);
				String nameType = null;
				String place = null;
				// is always of length 2
				if (placeVsName.length == 2) {
					place = placeVsName[1];
					nameType = placeVsName[0];
				} else if (placeVsName.length == 1) {
					place = "";
					nameType = placeVsName[0];
				} else {
					return false;
				}
				href = href.substring(matcher.start() + 1, matcher.end() - 1);
				Lesson cur;
				// get the type of lesson
				if (nameType.endsWith(_MAABADA)) {
					String name = nameType.substring(0,
							(nameType.length() - _MAABADA.length()));
					cur = new Lesson(name, href, place, Type.Maabada.ordinal(),
							days, hours);
				} else if (nameType.endsWith(_SHIUR)) {
					String name = nameType.replace(_SHIUR, "");
					cur = new Lesson(name, href, place, Type.Shiur.ordinal(),
							days, hours);
				} else if (nameType.endsWith(_HADRAHA)) {
					String name = nameType.substring(0,
							(nameType.length() - _HADRAHA.length()));
					cur = new Lesson(name, href, place, Type.Hadraha.ordinal(),
							days, hours);
				} else if (nameType.endsWith(_TIRGUL)) {
					String name = nameType.substring(0,
							(nameType.length() - _TIRGUL.length()));
					cur = new Lesson(name, href, place, Type.Tirgul.ordinal(),
							days, hours);
				} else if (nameType.endsWith(_MEHINA)) {
					String name = nameType.substring(0,
							(nameType.length() - _MEHINA.length()));
					cur = new Lesson(name, href, place, Type.Mehina.ordinal(),
							days, hours);
				} else if (nameType.endsWith(_SADNA)) {
					String name = nameType.substring(0,
							(nameType.length() - _SADNA.length()));
					cur = new Lesson(name, href, place, Type.Sadna.ordinal(),
							days, hours);
				} else if (nameType.endsWith(_SEMINARION)) {
					String name = nameType.substring(0,
							(nameType.length() - _SEMINARION.length()));
					cur = new Lesson(name, href, place,
							Type.Seminarion.ordinal(), days, hours);
				} else if (nameType.endsWith(_MATALA)) {
					String name = nameType.substring(0,
							(nameType.length() - _MATALA.length()));
					cur = new Lesson(name, href, place, Type.Matala.ordinal(),
							days, hours);
				} else if (nameType.endsWith(_SIUR)) {
					String name = nameType.substring(0,
							(nameType.length() - _SIUR.length()));
					cur = new Lesson(name, href, place, Type.Siur.ordinal(),
							days, hours);
				} else if (nameType.endsWith(_SHOS)) {
					String name = nameType.substring(0,
							(nameType.length() - _SHOS.length()));
					cur = new Lesson(name, href, place,
							Type.SiurSeminarion.ordinal(), days, hours);
				} else {
					cur = new Lesson(nameType, href, place,
							Type.None.ordinal(), days, hours);
				}
				// get name and id
				cur._name = Jsoup.parse(cur._name).text();
				// get course id from url
				String[] urlParse = cur._url.split("/");
				// get course id
				cur._id = urlParse[urlParse.length - 1];
				_lessons[days][hours] = cur;
				return true;

			}
		}
		return false;

	}

	/**
	 * Get the dates in Academic Year
	 */
	@SuppressLint("SimpleDateFormat")
	public void getDates() {
		_dates = new int[4][3];
		HujiSession webAccess = HujiSession.getInstance();
		String page = webAccess
				.getNonSecurePage("http://academic-secretary.huji.ac.il/?cmd=calendar.605");
		Whitelist whitelist = Whitelist.none();
		whitelist.addTags("div");
		whitelist.addAttributes("div", "id");
		page = Jsoup.clean(page, whitelist);
		Document document = Jsoup.parse(page);
		Element titles = document.getElementById("artContent");
		Elements events = titles.getElementsByTag("div");
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		boolean foundFirst = false;
		for (int i = 1; i < events.size(); i++) {
			if (events.get(i).text().contains("פתיחת שנת הלימודים")
					&& !foundFirst) {
				String[] text = events.get(i).text().split(" ");
				Date startA;
				try {
					startA = df.parse(text[text.length - 1]);
					String[] data = df.format(startA).split("/");
					_dates[0][0] = Integer.parseInt(data[0]);
					_dates[0][1] = Integer.parseInt(data[1]);
					_dates[0][2] = Integer.parseInt(data[2]);
					foundFirst = true;
				} catch (ParseException e) {
				}
			} else if (events.get(i).text().contains("סיום סמסטר א'")) {
				String[] text = events.get(i).text().split(" ");
				Date endA;
				try {
					endA = df.parse(text[text.length - 1]);
					String[] data = df.format(endA).split("/");
					_dates[1][0] = Integer.parseInt(data[0]);
					_dates[1][1] = Integer.parseInt(data[1]);
					_dates[1][2] = Integer.parseInt(data[2]);
				} catch (ParseException e) {
				}
			} else if (events.get(i).text().contains("פתיחת סמסטר ב'")) {
				String[] text = events.get(i).text().split(" ");
				Date startB;
				try {
					startB = df.parse(text[text.length - 1]);
					String[] data = df.format(startB).split("/");
					_dates[2][0] = Integer.parseInt(data[0]);
					_dates[2][1] = Integer.parseInt(data[1]);
					_dates[2][2] = Integer.parseInt(data[2]);
				} catch (ParseException e) {
				}
			} else if (events.get(i).text().contains("סיום סמסטר ב'")) {
				String[] text = events.get(i).text().split(" ");
				Date endB;
				try {
					endB = df.parse(text[text.length - 1]);
					String[] data = df.format(endB).split("/");
					_dates[3][0] = Integer.parseInt(data[0]);
					_dates[3][1] = Integer.parseInt(data[1]);
					_dates[3][2] = Integer.parseInt(data[2]);
				} catch (ParseException e) {
				}
			}
		}
	}

	public void addToDb(Semester semester) {
		_myDB.addLessons(_lessons, semester);
		if (_dates != null) {
			_myDB.addDates(_dates);
		}
	}
}
