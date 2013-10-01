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
 * Class that connects to HUJI and retrieve grades to DB
 * 
 * @author alonaba
 * 
 */
public class Grades {
	/**
	 * Grade object that represent grade
	 * 
	 * @author alonaba
	 * 
	 */
	public static class Grade {
		private String _name;
		private int _grade;
		private int _points;
		private String _id;
		private int _year;

		/**
		 * Constructor
		 * 
		 * @param name
		 *            - name of course
		 * @param grade
		 *            - grade in course
		 * @param points
		 *            - number of points
		 * @param id
		 *            - id of course
		 * @param year
		 *            - year
		 */
		public Grade(String name, int grade, int points, String id, int year) {
			_name = name;
			_grade = grade;
			_points = points;
			_id = id;
			_year = year;
		}

		/**
		 * Get grade
		 * 
		 * @return grade
		 */
		public int getGrade() {
			return _grade;
		}

		/**
		 * Get year
		 * 
		 * @return year
		 */
		public int getYear() {
			return _year;
		}

		/**
		 * Get points
		 * 
		 * @return points
		 */
		public int getPoints() {
			return _points;
		}

		/**
		 * Get id
		 * 
		 * @return ID
		 */
		public String getId() {
			return _id;
		}

		/**
		 * Get name
		 * 
		 * @return name
		 */
		public String getName() {
			return _name;
		}

		@Override
		public boolean equals(Object obj) {
			if (getClass() != obj.getClass()) {
				return false;
			} else if (_id.equals(((Grade) obj)._id)) {
				return true;
			}
			return false;
		}
	}

	// strings in html description
	private final static String _COURSE_ID = "סמל קורס";
	private final static String _COURSE_NAME = "קורס";
	private final static String _GRADE = "ציון";
	private final static String _POINTS = "נקודות זכות";

	private HujiSession _sessionToHuji;
	private ArrayList<Grade> _grades;
	private Whitelist _whitelist;
	private Database _myDB;
	private final static String _ssl_huji = "https://www.huji.ac.il";

	/**
	 * Constructor
	 * 
	 * @param context
	 *            - context
	 */
	public Grades(Context context) {
		_sessionToHuji = HujiSession.getInstance();
		_myDB = Database.getInstance(context);
		_whitelist = Whitelist.none();
		_whitelist.addTags("table", "td", "tr");
		_whitelist.addAttributes("td", "class");
		_grades = new ArrayList<Grade>();
	}

	/**
	 * Method that connects huji and retrieves all the grades
	 * 
	 * @return status, true if action succeed
	 */
	public boolean getGrades() {
		String session = _sessionToHuji.getSession();
		String path = _ssl_huji + session + Path.PATH_GRADES.getPath();
		// get page that contain all years there are grades for them
		String html = _sessionToHuji.getSecureHtml(HujiSession.Method.Get,
				path, null);
		if (html != null) {
			Elements years = getCount(html);
			if (years != null) {
				for (Element year : years) {
					// build path for each year and get grades
					path = _ssl_huji + session + Path.PATH_GRADES.getPath();
					Query postQueryGrades = new Query(
							Query.Argument.GRADES_YEAR, year.val());
					html = _sessionToHuji.getSecureHtml(
							HujiSession.Method.Post, path,
							postQueryGrades.getQuery());
					if (html != null) {
						html = Jsoup.clean(html, _whitelist);
						Document document = Jsoup.parse(html);
						parseGrades(document, year.val().replace("H", ""));
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
	 * Get all years that contain grades information
	 * 
	 * @param htmlPage
	 *            - the page contain desired data
	 * @return
	 */
	private Elements getCount(String htmlPage) {
		Document document = Jsoup.parse(htmlPage);
		// select item that are years in html
		Elements yearsafa = document.select("select[name=yearsafa]");
		if (yearsafa.size() > 0) {
			Elements years = yearsafa.first().getElementsByTag("option");
			return years;
		}
		return null;
	}

	/**
	 * Parse the html page with grades to arraylist of grades
	 * 
	 * @param document
	 *            - the html page
	 * @param year
	 *            - the year of each grade on this page
	 */
	private void parseGrades(Document document, String year) {
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
		// over each grade in table
		for (Element table : gradesTables) {
			getGrades(table, year);
		}
		gradesTables.clear();
	}

	/**
	 * Get grades from grades html table
	 * 
	 * @param table
	 *            the html table
	 * @param year
	 *            - the year of grades in this table
	 * @return true, if action succeeded
	 */
	private boolean getGrades(Element table, String year) {
		Elements titleNames = table.getElementsByClass("tableTitle");
		// get indexes in html
		int indexName = 0;
		int indexId = 0;
		int indexGrade = 0;
		int indexPoints = 0;
		for (int i = 0; i < titleNames.size(); i++) {
			if (titleNames.get(i).text().equals(_COURSE_ID)) {
				indexId = i;
			} else if (titleNames.get(i).text().equals(_GRADE)) {
				indexGrade = i;
			} else if (titleNames.get(i).text().equals(_COURSE_NAME)) {
				indexName = i;
			} else if (titleNames.get(i).text().equals(_POINTS)) {
				indexPoints = i;
			}
		}
		// run over each row and add desired data
		Elements rows = table.getElementsByTag("tr");
		String name;
		String id;
		String stringGrade;
		int grade;
		String stringPoints;
		int points;
		Grade cur;
		// no grades
		if (rows.size() < 2) {
			return true;
		}
		for (int i = 1; i < rows.size(); i++) {
			Elements cells = rows.get(i).getElementsByTag("td");
			name = cells.get(indexName).text();
			id = cells.get(indexId).text();
			stringGrade = cells.get(indexGrade).text();
			stringPoints = cells.get(indexPoints).text();
			if (!(name.matches("") || id.matches("") || stringGrade.matches("") || stringPoints
					.matches(""))) {
				try {
					grade = Integer.valueOf(stringGrade);
					points = Double.valueOf(stringPoints).intValue();
					cur = new Grade(name, grade, points, id,
							Integer.parseInt(year));
					if (!_grades.contains(cur)) {
						_grades.add(cur);
					}
				} catch (NumberFormatException e) {
					// if no grades or points
				}
			}

		}
		return true;
	}

	public void addToDb() {
		_myDB.addGrades(_grades);
	}
}
