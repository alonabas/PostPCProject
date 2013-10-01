package il.ac.huji.hujime;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import android.content.Context;

/**
 * Class that connects to HUJI and retrieve exams to DB
 * 
 * @author alonaba
 * 
 */
public class Exams {
	/**
	 * Class that represent Exam object
	 * 
	 * @author alonaba
	 * 
	 */
	public static class Exam {
		private String _name;
		private String _date;
		private String _time;
		private String _place;
		private String _specialPlace;
		private int _moed;
		private String _id;

		/**
		 * Constructor
		 * 
		 * @param id
		 *            - course id
		 * @param name
		 *            - course name
		 * @param moed
		 *            - moed
		 * @param date
		 *            - date of exam
		 * @param time
		 *            - time of exam
		 * @param place
		 *            - place of exam
		 * @param specialPlace
		 *            - special place
		 */
		public Exam(String id, String name, String moed, String date,
				String time, String place, String specialPlace) {
			_id = id;
			_name = name;
			if (moed.equals("א")) {
				_moed = 1;
			} else if (moed.equals("ב")) {
				_moed = 2;
			} else if (moed.equals("ג")) {
				_moed = 3;
			} else {
				_moed = 0;
			}
			_place = place;
			_date = date;
			_id = id;
			_specialPlace = specialPlace;
		}

		/**
		 * Constructor
		 * 
		 * @param id
		 *            - course id
		 * @param name
		 *            - course name
		 * @param moed
		 *            - moed
		 * @param date
		 *            - date of exam
		 * @param time
		 *            - time of exam
		 * @param place
		 *            - place of exam
		 * @param specialPlace
		 *            - special place
		 */
		public Exam(String id, String name, int moed, String date, String time,
				String place, String specialPlace) {
			_id = id;
			_name = name;
			_moed = moed;
			_place = place;
			_date = date;
			_id = id;
			_specialPlace = specialPlace;
		}

		/**
		 * Get course name
		 * 
		 * @return course name
		 */
		public String getName() {
			return _name;
		}

		/**
		 * Get course id
		 * 
		 * @return course id
		 */
		public String getId() {
			return _id;
		}

		/**
		 * Get date of exam
		 * 
		 * @return date of exam
		 */
		public String getDate() {
			return _date;
		}

		/**
		 * Get time of exam
		 * 
		 * @return time of exam
		 */
		public String getTime() {
			return _time;
		}

		/**
		 * Get place of exam
		 * 
		 * @return exam's place
		 */
		public String getPlace() {
			return _place;
		}

		/**
		 * Get special place of exam
		 * 
		 * @return place
		 */
		public String getSpecialPlace() {
			return _specialPlace;
		}

		/**
		 * Get moed of exam
		 * 
		 * @return moed
		 */
		public int getMoed() {
			return _moed;
		}

		@Override
		public boolean equals(Object obj) {
			if (getClass() != obj.getClass()) {
				return false;
			} else if (_id.equals(((Exam) obj)._id)
					&& _moed == ((Exam) obj)._moed) {
				return true;
			}
			return false;
		}
	}

	// strings in html description
	private final static String _COURSE_ID = "קורס";
	private final static String _COURSE_NAME = "שם קורס";
	private final static String _MOED = "מועד";
	private final static String _DATE = "תאריך בחינה";
	private final static String _TIME = "שעה";
	private final static String _PLACE = "אולם";
	private final static String _SPECIAL_PLACE = "אולם מיוחד*";

	private HujiSession _sessionToHuji;
	private ArrayList<Exam> _exams;
	private Whitelist _whitelist;
	private Database _myDB;
	private final static String _ssl_huji = "https://www.huji.ac.il";

	/**
	 * Constructor
	 * 
	 * @param context
	 *            - context
	 */
	public Exams(Context context) {
		_sessionToHuji = HujiSession.getInstance();
		_myDB = Database.getInstance(context);
		_whitelist = Whitelist.none();
		_whitelist.addTags("table", "td", "tr");
		_whitelist.addAttributes("td", "class");
		_exams = new ArrayList<Exam>();
	}

	/**
	 * Method that connects to HUJI get list of years there are exams on them,
	 * retrieve all exams, and parse it to Exam objects
	 * 
	 * @return status of operation, true if succeed
	 */
	public boolean getExams() {
		String session = _sessionToHuji.getSession();
		String path = _ssl_huji + session + Path.PATH_GRADES.getPath();
		String html = _sessionToHuji.getSecureHtml(HujiSession.Method.Get,
				path, null);
		if (html != null) {
			Elements years = getCount(html);
			if (years != null) {
				// for each year get the exams list for it
				for (Element year : years) {
					path = _ssl_huji + session + Path.PATH_EXAMS.getPath();
					Query postQueryGrades = new Query(
							Query.Argument.EXAMS_YEAR, year.val());
					html = _sessionToHuji.getSecureHtml(
							HujiSession.Method.Post, path,
							postQueryGrades.getQuery());
					// if html isn't empty
					if (html != null) {
						html = Jsoup.clean(html, _whitelist);
						Document document = Jsoup.parse(html);
						parseExams(document);
					}
				}

				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Get number of years, that there may be exams for
	 * 
	 * @param htmlPage
	 *            - html page that contain the list of years
	 * @return Elements of each eyar
	 */
	private Elements getCount(String htmlPage) {
		Document document = Jsoup.parse(htmlPage);
		Elements yearsafa = document.select("select[name=yearsafa]");
		if (yearsafa.size() > 0) {
			Elements years = yearsafa.first().getElementsByTag("option");
			return years;
		}
		return null;
	}

	/**
	 * Parse exams from html document
	 * 
	 * @param document
	 *            - the html documents
	 */
	private void parseExams(Document document) {
		List<Element> gradesTables = new ArrayList<Element>();
		// titles in table of grades are of class tableTitle
		Elements titles = document.getElementsByClass("tableTitle");
		if (titles.size() == 0) {
			return;
		}
		for (Element title : titles) {
			// get the table itself
			Element table = title.parent().parent();
			if (!gradesTables.contains(table)) {
				gradesTables.add(table);
			}
		}
		// overeach table
		for (Element table : gradesTables) {
			getGrades(table);
		}
		gradesTables.clear();
	}

	/**
	 * Get grades from html table from huji web site
	 * 
	 * @param table
	 *            - the html table
	 * @return true, if succeded
	 */
	private boolean getGrades(Element table) {
		Elements titleNames = table.getElementsByClass("tableTitle");
		// get indexes in html
		int indexId = 0;
		int indexName = 0;
		int indexMoed = 0;
		int indexDate = 0;
		int indexTime = 0;
		int indexPlace = 0;
		int indexSpecialPlace = 0;
		for (int i = 0; i < titleNames.size(); i++) {
			if (titleNames.get(i).text().equals(_COURSE_ID)) {
				indexId = i;
			} else if (titleNames.get(i).text().equals(_COURSE_NAME)) {
				indexName = i;
			} else if (titleNames.get(i).text().equals(_MOED)) {
				indexMoed = i;
			} else if (titleNames.get(i).text().equals(_DATE)) {
				indexDate = i;
			} else if (titleNames.get(i).text().equals(_TIME)) {
				indexTime = i;
			} else if (titleNames.get(i).text().equals(_PLACE)) {
				indexPlace = i;
			} else if (titleNames.get(i).text().equals(_SPECIAL_PLACE)) {
				indexSpecialPlace = i;
			}
		}
		// run over each row and add desired data
		Elements rows = table.getElementsByTag("tr");
		String id;
		String name;
		String moed;
		String date;
		String time;
		String place;
		String specialPlace;
		Exam cur;
		// no exams
		if (rows.size() < 2) {
			return true;
		}
		for (int i = 1; i < rows.size(); i++) {
			Elements cells = rows.get(i).getElementsByTag("td");
			id = cells.get(indexId).text();
			name = cells.get(indexName).text();
			moed = cells.get(indexMoed).text();
			date = cells.get(indexDate).text();
			time = cells.get(indexTime).text();
			place = cells.get(indexPlace).text();
			specialPlace = cells.get(indexSpecialPlace).text();
			if (!name.equals("") && !moed.equals("") && !date.equals("")
					&& !id.equals("")) {
				cur = new Exam(id, name, moed, date, time, place, specialPlace);
				if (!_exams.contains(cur)) {
					_exams.add(cur);
				}
			}
		}
		return true;
	}

	/**
	 * Write exams to db
	 */
	public void addToDb() {
		_myDB.addExams(_exams);
	}
}
