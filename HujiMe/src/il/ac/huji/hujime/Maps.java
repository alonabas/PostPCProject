package il.ac.huji.hujime;

import java.util.ArrayList;

import android.content.Context;

/**
 * Class that defines maps
 * 
 * @author alonaba
 * 
 */
public class Maps {

	enum Campus {
		HarHaZofim, GivatRam, EinKarem, Rehovot, Unknown
	}

	/**
	 * Place object
	 * 
	 * @author alonaba
	 * 
	 */
	public class Place {
		private Campus _campus;
		private String _place;
		private String[] _key_words;

		/**
		 * Constructor
		 * 
		 * @param place
		 *            - place name
		 */
		public Place(String place) {
			_place = place;
			_campus = Campus.Unknown;

		}

		/**
		 * Constructor
		 * 
		 * @param place
		 *            - place
		 * @param campus
		 *            - campus
		 * @param words
		 *            - words to search
		 */
		public Place(String place, Campus campus, String[] words) {
			_place = place;
			_campus = campus;
			_key_words = words;
		}

		/**
		 * Get place
		 * 
		 * @return place
		 */
		public String getPlace() {
			return _place;
		}

		/**
		 * Get key words for search
		 * 
		 * @return key words
		 */
		public String[] getWords() {
			return _key_words;
		}

		/**
		 * Get campus
		 * 
		 * @return campus
		 */
		public Campus getCampus() {
			return _campus;
		}

	}

	ArrayList<Place> _key_words_har_hazofim;
	ArrayList<Place> _key_words_givat_ram;
	ArrayList<Place> _key_words_ein_karem;
	ArrayList<Place> _key_words_rehovot;
	private static Maps _maps;

	/**
	 * Get instance of maps, singleton
	 * 
	 * @return instance of Maps object
	 */
	public static Maps getInstance() {
		if (_maps == null) {
			_maps = new Maps();
			_maps.getPatternLocation();
		}
		return _maps;
	}

	// Constructor
	private Maps() {
		// [0] - har hazofim, [1] - givatram,[2] - einkarem, [3] - rehovot,
		_key_words_har_hazofim = new ArrayList<Place>();
		_key_words_givat_ram = new ArrayList<Place>();
		_key_words_ein_karem = new ArrayList<Place>();
		_key_words_rehovot = new ArrayList<Place>();

	}

	/**
	 * Get places and kewords in rehovot
	 * 
	 * @return list of rehovot places
	 */
	public ArrayList<Place> getPlacesRehovot() {
		return _key_words_rehovot;
	}

	/**
	 * Get places and kewords in ein karem
	 * 
	 * @return list of ein karem places
	 */
	public ArrayList<Place> getPlacesEinKarem() {
		return _key_words_ein_karem;
	}

	/**
	 * Get places and kewords in givat ram
	 * 
	 * @return list of givat ram places
	 */
	public ArrayList<Place> getPlacesGivatRam() {
		return _key_words_givat_ram;
	}

	/**
	 * Get places and kewords in har hazofim
	 * 
	 * @return list of har hazofim places
	 */
	public ArrayList<Place> getPlacesHarHazofim() {
		return _key_words_har_hazofim;
	}

	/**
	 * Define Locations
	 */
	public void getPatternLocation() {
		_key_words_har_hazofim.add(new Place("פקולטה למדעי רוח",
				Campus.HarHaZofim, new String[] { "רוח" }));
		_key_words_har_hazofim.add(new Place("אוסטרליה", Campus.HarHaZofim,
				new String[] { "אוסטרליה" }));
		_key_words_har_hazofim.add(new Place("חוות מחשבים", Campus.HarHaZofim,
				new String[] { "מחשבים", "מחשב" }));
		_key_words_har_hazofim.add(new Place("אולם עצמעות מקסיקו",
				Campus.HarHaZofim, new String[] { "מקסיקו", "עצמאות" }));
		_key_words_har_hazofim.add(new Place("אולם 300", Campus.HarHaZofim,
				new String[] { "אולם 300", "300" }));
		_key_words_har_hazofim.add(new Place("אקדמניה בצלאל",
				Campus.HarHaZofim, new String[] { "בצלאל" }));
		_key_words_har_hazofim.add(new Place("אולמות הרצאה מדעי טבע",
				Campus.HarHaZofim, new String[] { "טבע" }));
		_key_words_har_hazofim.add(new Place("מכון ליהדות זמננו אברהם הרמן",
				Campus.HarHaZofim, new String[] { "יהדות", "זמננו", "הרמן" }));
		_key_words_har_hazofim.add(new Place("בית ספר לתלמידים מחול",
				Campus.HarHaZofim, new String[] { "תלמידים מחול", "חול" }));
		_key_words_har_hazofim.add(new Place("מכון טרומן", Campus.HarHaZofim,
				new String[] { "טרומן" }));
		_key_words_har_hazofim.add(new Place("בובר-רוסי", Campus.HarHaZofim,
				new String[] { "בובר-רוסי" }));
		_key_words_har_hazofim.add(new Place("אודיטוריום הנדלר",
				Campus.HarHaZofim, new String[] { "הנדלר" }));
		_key_words_har_hazofim.add(new Place("מכון מגיד", Campus.HarHaZofim,
				new String[] { "מגיד" }));
		_key_words_har_hazofim.add(new Place("מכון לארכיאולוגיה",
				Campus.HarHaZofim, new String[] { "ארכאולוגיה" }));
		_key_words_har_hazofim.add(new Place("בית הילל", Campus.HarHaZofim,
				new String[] { "הילל" }));
		_key_words_har_hazofim.add(new Place("מכון למדעי יהדות בניין רבין",
				Campus.HarHaZofim, new String[] { "יהדות", "רבין" }));
		_key_words_har_hazofim.add(new Place("פקולטה למשפטים",
				Campus.HarHaZofim, new String[] { "משפטים" }));
		_key_words_har_hazofim.add(new Place("אולם הסנאט", Campus.HarHaZofim,
				new String[] { "סנאט" }));
		_key_words_har_hazofim.add(new Place("מנהל עסקים ומרכז החישובים",
				Campus.HarHaZofim,
				new String[] { "מנהל עסקים", "מרכז חישובים" }));
		_key_words_har_hazofim.add(new Place("בית ספר לעבודה סוציאלית",
				Campus.HarHaZofim, new String[] { "עבודה סוציאלית" }));
		_key_words_har_hazofim.add(new Place("בית ספר לחינוך",
				Campus.HarHaZofim, new String[] { "חינוך" }));
		_key_words_har_hazofim.add(new Place("בית ספר לריפוי בעיסוק",
				Campus.HarHaZofim, new String[] { "ריפוי בעיסוק" }));
		_key_words_har_hazofim.add(new Place("בית ספר הדסה", Campus.HarHaZofim,
				new String[] { "הדסה" }));
		_key_words_har_hazofim.add(new Place("בית מאירסדורף",
				Campus.HarHaZofim, new String[] { "מאירסדורף" }));
		_key_words_har_hazofim.add(new Place("ספריה המרכזית למדעי הרוח והחברה",
				Campus.HarHaZofim, new String[] { "ספריה", "רוח וחברה" }));
		_key_words_har_hazofim.add(new Place("מחשבה ישראלית מקרא והעברית",
				Campus.HarHaZofim, new String[] { "מחשבה ישראל", "מקרא",
						"עברית" }));
		_key_words_har_hazofim.add(new Place("מכון למדעי אסיה ואפריקה",
				Campus.HarHaZofim, new String[] { "אסיה", "אפריקה" }));
		_key_words_har_hazofim.add(new Place(
				"דיקנט הפקולטה למדעי רוח ומזכירות", Campus.HarHaZofim,
				new String[] { "דיקנט", "מזכירות" }));
		_key_words_har_hazofim.add(new Place("היסטוריה ופילוסופיה",
				Campus.HarHaZofim, new String[] { "היסטוריה", "פילוסופיה" }));
		_key_words_har_hazofim.add(new Place("חוג לאנגלית", Campus.HarHaZofim,
				new String[] { "אנגלית" }));
		_key_words_har_hazofim.add(new Place("חוג לצרפתית", Campus.HarHaZofim,
				new String[] { "צרפתית" }));
		_key_words_har_hazofim.add(new Place("מכון לחקר הטיפוח לחינוך",
				Campus.HarHaZofim, new String[] { "חינוך" }));
		_key_words_har_hazofim.add(new Place("מרכז וולפסון לחינוך",
				Campus.HarHaZofim, new String[] { "חינוך", "וולפסון" }));
		_key_words_har_hazofim.add(new Place("ספריה לחינוך זלמן ארן",
				Campus.HarHaZofim, new String[] { "ספריה", "חינוך", "זלמן",
						"ארן" }));
		_key_words_har_hazofim.add(new Place("מכינה קדם אקדמאית",
				Campus.HarHaZofim, new String[] { "מכינה קדם אקדמאית" }));
		_key_words_har_hazofim.add(new Place("אמפיתיאטרון רוטברג",
				Campus.HarHaZofim, new String[] { "רוטברג" }));
		_key_words_har_hazofim.add(new Place("אגודת סטודנטים דיקנט סטודנטים",
				Campus.HarHaZofim,
				new String[] { "אגודה", "דיקנט", "סטודנטים" }));
		_key_words_har_hazofim.add(new Place("הנהלת האוניברסיטה",
				Campus.HarHaZofim, new String[] { "הנהלה", "הנהלת" }));
		_key_words_har_hazofim.add(new Place("כלכלה ומכון פאלק",
				Campus.HarHaZofim, new String[] { "כלכלה", "פאלק" }));
		_key_words_har_hazofim.add(new Place("יחבל ומדע המדינה",
				Campus.HarHaZofim, new String[] { "יחבל", "מדע המדינה" }));
		_key_words_har_hazofim.add(new Place(
				"סטטיסטיקה מדעי אכלוסיה וקומוניקציה", Campus.HarHaZofim,
				new String[] { "סטטיסטיקה", "מדעי אכלוסיה", "קומוניקציה" }));
		_key_words_har_hazofim
				.add(new Place("פסיכולוגיה וסוציאולוגיה", Campus.HarHaZofim,
						new String[] { "פסיכולוגיה", "סוציאולוגיה" }));
		_key_words_har_hazofim.add(new Place("גאוגרפיה", Campus.HarHaZofim,
				new String[] { "גאוגרפיה" }));
		_key_words_har_hazofim.add(new Place("דיקנט פקולטה למדעי חברה",
				Campus.HarHaZofim, new String[] { "דיקנט מדעי חברה",
						"מדעי חברה" }));

		_key_words_givat_ram.add(new Place("בניין המנהלה שרמן",
				Campus.GivatRam, new String[] { "שרמן", "מנהלה" }));
		_key_words_givat_ram.add(new Place("אודיטוריום וייז", Campus.GivatRam,
				new String[] { "וייז" }));
		_key_words_givat_ram.add(new Place("בניין פעלדמן", Campus.GivatRam,
				new String[] { "פעלדמן", "פלדמן" }));
		_key_words_givat_ram.add(new Place(
				"המרכז לחקר הרציונליות והחלטות האינטראקטיביות",
				Campus.GivatRam, new String[] { "חקר הרציונליות",
						"החלטות אינטרקטיביות" }));
		_key_words_givat_ram.add(new Place("מרכז חינור לחקר תולדות ישראל",
				Campus.GivatRam, new String[] { "תולדת ישראל" }));
		_key_words_givat_ram.add(new Place("המכון ללימודים מתקדמים",
				Campus.GivatRam, new String[] { "לימודים מתקדמים" }));
		_key_words_givat_ram.add(new Place("קשרי אוניברסיטה קהילה",
				Campus.GivatRam, new String[] { "קשר קהילה" }));
		_key_words_givat_ram.add(new Place("פיסיקה", Campus.GivatRam,
				new String[] { "פיסיקה", "פיזיקה" }));
		_key_words_givat_ram.add(new Place("מרכז בינתחומי", Campus.GivatRam,
				new String[] { "בינתחומי" }));
		_key_words_givat_ram.add(new Place("בניין שפרינצק", Campus.GivatRam,
				new String[] { "שפרינצק" }));
		_key_words_givat_ram.add(new Place("בניין ברמן", Campus.GivatRam,
				new String[] { "ברמן" }));
		_key_words_givat_ram.add(new Place(
				"עשבייה, מעבדות בוטניקה, מאגרי מידע גיאגרפיים",
				Campus.GivatRam,
				new String[] { "עשבייה", "בוטניקה", "גאוגרפיה" }));
		_key_words_givat_ram.add(new Place("בניין לובין", Campus.GivatRam,
				new String[] { "לובין" }));
		_key_words_givat_ram.add(new Place("בניין לוי, מעבדת מחשבים",
				Campus.GivatRam, new String[] { "לוי", "מחשבים" }));
		_key_words_givat_ram.add(new Place(
				"דיקנט הפקולטה למתמטיקה ולמדעי הטבע מזכירות הוראה ותלמידים",
				Campus.GivatRam,
				new String[] { "דיקנט", "מזכירות", "מדעי טבע" }));
		_key_words_givat_ram.add(new Place("ארכיואנות ומדע", Campus.GivatRam,
				new String[] { "ארכיון", "ארכיונות" }));
		_key_words_givat_ram.add(new Place("מעבדת מחשבים אקואריום",
				Campus.GivatRam, new String[] { "אקואריום", "מחשבים" }));
		_key_words_givat_ram.add(new Place("בניין קפלן, אולם רוטברג",
				Campus.GivatRam, new String[] { "קפלן", "רוטברג" }));
		_key_words_givat_ram.add(new Place(
				"בניין רוס, בית ספר להנדסה למדעי המחשב", Campus.GivatRam,
				new String[] { "רוס","מדעי מחשב","הנדסה" }));
		_key_words_givat_ram.add(new Place("בית בלגיה", Campus.GivatRam,
				new String[] { "בלגיה" }));
		_key_words_givat_ram.add(new Place("מכון קזאלי לכימיה יישומית",
				Campus.GivatRam, new String[] { "קזאלי", "כימיה יישומית" }));
		_key_words_givat_ram.add(new Place("מכון חיים וייצמן לכימיה",
				Campus.GivatRam, new String[] { "וייצמן", "כימיה" }));
		_key_words_givat_ram.add(new Place("לוס אנג'לס", Campus.GivatRam,
				new String[] { "לוס אנגלס", "לוס אנג'לס" }));
		_key_words_givat_ram.add(new Place("ארונברג", Campus.GivatRam,
				new String[] { "ארונברג" }));
		_key_words_givat_ram.add(new Place("וכטל", Campus.GivatRam,
				new String[] { "וכטל" }));
		_key_words_givat_ram.add(new Place("אלברמן", Campus.GivatRam,
				new String[] { "אלברמן" }));
		_key_words_givat_ram.add(new Place("בריקמן", Campus.GivatRam,
				new String[] { "בריקמן" }));
		_key_words_givat_ram.add(new Place("פילדלפיה", Campus.GivatRam,
				new String[] { "פילדלפיה" }));
		_key_words_givat_ram.add(new Place("מכון סילברמן למדעי החיים",
				Campus.GivatRam, new String[] { "סילברמן", "מדעי החיים" }));
		_key_words_givat_ram.add(new Place("בניין וולפסון", Campus.GivatRam,
				new String[] { "וולפסון" }));
		_key_words_givat_ram.add(new Place("המרכז לביולוגיה מבנית",
				Campus.GivatRam, new String[] { "ביולוגיה מבנית" }));
		_key_words_givat_ram.add(new Place("לאון", Campus.GivatRam,
				new String[] { "לאון" }));
		_key_words_givat_ram.add(new Place("המכון למדעי כדור הארץ",
				Campus.GivatRam, new String[] { "מדעי כדור הארץ" }));
		_key_words_givat_ram.add(new Place("ספריית המכון למדעי כדור הארץ",
				Campus.GivatRam, new String[] { "ספריה", "מדעי כדור הארץ" }));
		_key_words_givat_ram.add(new Place("בית מלאכה לניפוח זכוכית",
				Campus.GivatRam, new String[] { "בית מלאכה", "ניפוח זכוכית" }));
		_key_words_givat_ram.add(new Place("בניין לב", Campus.GivatRam,
				new String[] { "לב" }));
		_key_words_givat_ram.add(new Place("האקדמיה ללשון העברית",
				Campus.GivatRam, new String[] { "לשון עברית" }));
		_key_words_givat_ram.add(new Place("מרקס", Campus.GivatRam,
				new String[] { "מרקס" }));
		_key_words_givat_ram.add(new Place("בניין דנציגר א'", Campus.GivatRam,
				new String[] { "דנציגר א" }));
		_key_words_givat_ram.add(new Place("דנציגר ב'", Campus.GivatRam,
				new String[] { "דנציגר ב" }));
		_key_words_givat_ram.add(new Place(
				"מכון איינשטיין למתמטיקה בניין מנצ'סטר", Campus.GivatRam,
				new String[] { "מנצ'סטר", "מתמטיקה", "מטמטיקה", "מנצסטר" }));
		_key_words_givat_ram.add(new Place("מכון רקח לפיסיקה בניין קפלון",
				Campus.GivatRam, new String[] { "רקח", "פיסיקה", "פיזיקה",
						"קפלון" }));
		_key_words_givat_ram.add(new Place("בניין לוין מכון רקח לפיסיקה הנהלה",
				Campus.GivatRam, new String[] { "לוין", "לווין", "רקח",
						"פיסיקה", "פיזיקה" }));
		_key_words_givat_ram.add(new Place("אולם קנדה", Campus.GivatRam,
				new String[] { "קנדה" }));
		_key_words_givat_ram.add(new Place("מרכז מעבדות למדעים בלמונטה",
				Campus.GivatRam, new String[] { "בלומנטה" }));
		_key_words_givat_ram.add(new Place("היחידה הקריאוגנית",
				Campus.GivatRam, new String[] { "קריאוגני" }));
		_key_words_givat_ram.add(new Place("מעבדת דארוף לאיזוטופים",
				Campus.GivatRam, new String[] { "דארוף", "איזוטופ" }));
		_key_words_givat_ram.add(new Place("בית צרפת", Campus.GivatRam,
				new String[] { "צרפת" }));
		_key_words_givat_ram.add(new Place("דבורסקי", Campus.GivatRam,
				new String[] { "דבורסקי" }));
		_key_words_givat_ram.add(new Place("בית ספר למדע יישומי הרמן",
				Campus.GivatRam, new String[] { "הרמן", "מדע" }));
		_key_words_givat_ram.add(new Place("בניין ברגמן", Campus.GivatRam,
				new String[] { "ברגמן" }));
		_key_words_givat_ram.add(new Place("ספריית הרמן למדעי הטבע",
				Campus.GivatRam, new String[] { "הרמן", "ספריה" }));

		// ein karem
		_key_words_ein_karem.add(new Place("בית ספר לאחיות", Campus.EinKarem,
				new String[] { "אחות", "אחיות" }));
		_key_words_ein_karem.add(new Place("בית ספר לבריאות הציבור",
				Campus.EinKarem, new String[] { "בריאות הציבור", "ציבור" }));
		_key_words_ein_karem.add(new Place("המרכז הרפואי הדסה עין כרם",
				Campus.EinKarem,
				new String[] { "מרכז רפואי", "הדסה", "עין כרם" }));
		_key_words_ein_karem.add(new Place("מרכז לאם ולילד", Campus.EinKarem,
				new String[] { "אם וילד", "ילד", "אם" }));
		_key_words_ein_karem.add(new Place("המחלקה לסיווג רקמות",
				Campus.EinKarem,
				new String[] { "סיווג רקמות", "סיווג", "רקמות" }));
		_key_words_ein_karem.add(new Place("בית ספר לרקחות", Campus.EinKarem,
				new String[] { "רקחות", "רוקח" }));
		_key_words_ein_karem.add(new Place("הפקולטה לרפואת שיניים",
				Campus.EinKarem, new String[] { "רפואת שיניים", "שיניים" }));
		_key_words_ein_karem.add(new Place("אודיטוריום מגיד", Campus.EinKarem,
				new String[] { "מגיד" }));
		_key_words_ein_karem.add(new Place("ספרייה רפואית", Campus.EinKarem,
				new String[] { "ספרייה", "ספריה" }));
		_key_words_ein_karem.add(new Place("הנהלת הפקולטה", Campus.EinKarem,
				new String[] { "הנהלה" }));
		_key_words_ein_karem.add(new Place("מעבדות מחקר", Campus.EinKarem,
				new String[] { "מעבדה", "מחקר" }));
		_key_words_ein_karem.add(new Place("הפקולטה לרפואה", Campus.EinKarem,
				new String[] { "פקולטה", "רפואה" }));
		_key_words_ein_karem.add(new Place("מרכז הסטודנט פורשהיימר",
				Campus.EinKarem, new String[] { "פורשהיימר", "סטודנט",
						"מרכז הסטודנט" }));
		_key_words_ein_karem.add(new Place("בניין גודלסקי", Campus.EinKarem,
				new String[] { "גודלסקי" }));
		_key_words_ein_karem.add(new Place("מעבדות הוראה", Campus.EinKarem,
				new String[] { "מעבדה", "הוראה" }));
		_key_words_ein_karem.add(new Place(
				"הבניין הבינלאומי, מעבדות מחקר והוראה", Campus.EinKarem,
				new String[] { "בינליאומי", "בינ-ליאומי", "מעבדה", "מעבדות",
						"מחקר", "הוראה" }));
		_key_words_ein_karem.add(new Place("מרכז בוטנר", Campus.EinKarem,
				new String[] { "בוטנר" }));
		// rehovot

		_key_words_rehovot
				.add(new Place("מרכז לחקר דבורים טריווקס", Campus.EinKarem,
						new String[] { "דבורים", "מחקר", "טריוויקס" }));
		_key_words_rehovot.add(new Place("מדור ללימודי חוץ", Campus.EinKarem,
				new String[] { "לימודי חוץ", "חוץ" }));
		_key_words_rehovot.add(new Place("מרכז יקא", Campus.EinKarem,
				new String[] { "יקא" }));
		_key_words_rehovot.add(new Place(
				"מרכז אוטו ורבורג לביוטכנולוגיה חקלאית", Campus.EinKarem,
				new String[] { "אוטו", "ורברג", "ביוטכנולוגיה" }));
		_key_words_rehovot.add(new Place("בניין אהרונסון", Campus.EinKarem,
				new String[] { "אהרונסון" }));
		_key_words_rehovot.add(new Place(
				"מנהלה, מזכירות לענייני ההוראה,לשכת דיקן", Campus.EinKarem,
				new String[] { "מנהלה", "מזכירות", "דיקן" }));
		_key_words_rehovot.add(new Place("כיתה 84", Campus.EinKarem,
				new String[] { "כיתה 84" }));
		_key_words_rehovot.add(new Place("בתי מלאכה", Campus.EinKarem,
				new String[] { "מלאכה" }));
		_key_words_rehovot.add(new Place("מרכז קנדי לי ב'", Campus.EinKarem,
				new String[] { "קנדי לי ב", "קנדי", "לי", "קנדי ב" }));
		_key_words_rehovot.add(new Place("בניין קנדי לי ג'", Campus.EinKarem,
				new String[] { "קנדי", "לי", "קנדי לי ג", "קנדי ג" }));
		_key_words_rehovot.add(new Place("פיטוטרון", Campus.EinKarem,
				new String[] { "פיטוטרון" }));
		_key_words_rehovot.add(new Place("מרכז בוטנרבניין פרנקלין אלפר",
				Campus.EinKarem, new String[] { "פרנקלין אלפר", "פרנקלין",
						"אלפר" }));
		_key_words_rehovot.add(new Place("חממות מחקר הוראה", Campus.EinKarem,
				new String[] { "חממות", "הוראה" }));
		_key_words_rehovot.add(new Place("בניין מוסקונה", Campus.EinKarem,
				new String[] { "מוסקונה" }));
		_key_words_rehovot.add(new Place("בניין אריוביץ ג'", Campus.EinKarem,
				new String[] { "אריוביץ ג", "אריוביץ" }));
		_key_words_rehovot.add(new Place("מרכז קנדי לי א'", Campus.EinKarem,
				new String[] { "קנדי", "לי", "קנדי לי א", "קנדי א" }));
		_key_words_rehovot.add(new Place("מעבדות מחקר מרכז וולקני",
				Campus.EinKarem, new String[] { "וולקוני", "ולקוני", "מעבדה",
						"מחקר", "מעבדות" }));
		_key_words_rehovot.add(new Place("בניין לאוטרמן", Campus.EinKarem,
				new String[] { "לאוטרמן" }));
		_key_words_rehovot.add(new Place("בניין איזנברג", Campus.EinKarem,
				new String[] { "איזנברג" }));
		_key_words_rehovot.add(new Place("בניין לובל", Campus.EinKarem,
				new String[] { "לובל" }));
		_key_words_rehovot.add(new Place("בניין אריוביץ ב'", Campus.EinKarem,
				new String[] { "אריוביץ ב", "אריוביץ" }));
		_key_words_rehovot.add(new Place("וירולוגיה", Campus.EinKarem,
				new String[] { "וירולוגיה" }));
		_key_words_rehovot.add(new Place("בניין וולקני", Campus.EinKarem,
				new String[] { "וולקוני", "ולקוני" }));
		_key_words_rehovot.add(new Place("אודיטוריום אריוביץ'",
				Campus.EinKarem, new String[] { "אריוביץ" }));
		_key_words_rehovot
				.add(new Place("שירותי יעוץ לסטודנט", Campus.EinKarem,
						new String[] { "יעוץ", "סטודנטים", "סטודנט" }));
		_key_words_rehovot
				.add(new Place("בית ספר לוטרינריה על שם קורט", Campus.EinKarem,
						new String[] { "קורט", "וטרינטריה", "וטרינר" }));
	}

	/**
	 * Create locations and add to db
	 * 
	 * @param context
	 *            - context
	 */
	public void createLocations(Context context) {
		ArrayList<Location> _locations = new ArrayList<Location>();
		// har hazofim
		_locations.add(new Location("פקולטה למדעי רוח", Campus.HarHaZofim,
				31.791968, 35.243325));
		_locations.add(new Location("אוסטרליה", Campus.HarHaZofim, 31.795493,
				35.241587));
		_locations.add(new Location("חוות מחשבים", Campus.HarHaZofim,
				31.792739, 35.243148));
		_locations.add(new Location("אולם עצמעות מקסיקו", Campus.HarHaZofim,
				31.792418, 35.243483));
		_locations.add(new Location("אולם 300", Campus.HarHaZofim, 31.792105,
				35.243765));
		_locations.add(new Location("אקדמניה בצלאל", Campus.HarHaZofim,
				31.793108, 35.24729));
		_locations.add(new Location("אולמות הרצאה מדעי טבע", Campus.HarHaZofim,
				31.792479, 35.246962));
		_locations.add(new Location("מכון ליהדות זמננו אברהם הרמן",
				Campus.HarHaZofim, 31.792538, 35.24627));
		_locations.add(new Location("בית ספר לתלמידים מחול", Campus.HarHaZofim,
				31.792944, 35.246217));
		_locations.add(new Location("מכון טרומן", Campus.HarHaZofim, 31.79154,
				35.24641));
		_locations.add(new Location("בובר-רוסי", Campus.HarHaZofim, 31.791458,
				35.245798));
		_locations.add(new Location("אודיטוריום הנדלר", Campus.HarHaZofim,
				31.791353, 35.246614));
		_locations.add(new Location("מכון מגיד", Campus.HarHaZofim, 31.791025,
				35.245621));
		_locations.add(new Location("מכון לארכיאולוגיה", Campus.HarHaZofim,
				31.791745, 35.245562));
		_locations.add(new Location("בית הילל", Campus.HarHaZofim, 31.792087,
				35.2459));
		_locations.add(new Location("מכון למדעי יהדות בניין רבין",
				Campus.HarHaZofim, 31.793614, 35.245605));
		_locations.add(new Location("פקולטה למשפטים", Campus.HarHaZofim,
				31.792753, 35.244318));
		_locations.add(new Location("אולם הסנאט", Campus.HarHaZofim, 31.793464,
				35.243888));
		_locations.add(new Location("מנהל עסקים ומרכז החישובים",
				Campus.HarHaZofim, 31.793715, 35.243524));
		_locations.add(new Location("בית ספר לעבודה סוציאלית",
				Campus.HarHaZofim, 31.791339, 35.244666));
		_locations.add(new Location("בית ספר לחינוך", Campus.HarHaZofim,
				31.79097, 35.245122));
		_locations.add(new Location("בית ספר לריפוי בעיסוק", Campus.HarHaZofim,
				31.796227, 35.24214));
		_locations.add(new Location("בית ספר הדסה", Campus.HarHaZofim,
				31.797435, 35.24236));
		_locations.add(new Location("בית מאירסדורף", Campus.HarHaZofim,
				31.791736, 35.244441));

		_locations.add(new Location("ספריה המרכזית למדעי הרוח והחברה",
				Campus.HarHaZofim, 31.792958, 35.242842));
		_locations.add(new Location("מחשבה ישראלית מקרא והעברית",
				Campus.HarHaZofim, 31.792415, 35.242445));
		_locations.add(new Location("מכון למדעי אסיה ואפריקה",
				Campus.HarHaZofim, 31.792155, 35.242467));
		_locations.add(new Location("דיקנט הפקולטה למדעי רוח ומזכירות",
				Campus.HarHaZofim, 31.792069, 35.242977));
		_locations.add(new Location("היסטוריה ופילוסופיה", Campus.HarHaZofim,
				31.79195, 35.243009));
		_locations.add(new Location("חוג לאנגלית", Campus.HarHaZofim,
				31.791968, 35.243443));
		_locations.add(new Location("חוג לצרפתית", Campus.HarHaZofim,
				31.791918, 35.244114));
		_locations.add(new Location("מכון לחקר הטיפוח לחינוך",
				Campus.HarHaZofim, 31.790724, 35.245396));
		_locations.add(new Location("מרכז וולפסון לחינוך", Campus.HarHaZofim,
				31.790655, 35.245605));
		_locations.add(new Location("ספריה לחינוך זלמן ארן", Campus.HarHaZofim,
				31.790509, 35.245669));
		_locations.add(new Location("מכינה קדם אקדמאית", Campus.HarHaZofim,
				31.791271, 35.245884));
		_locations.add(new Location("פקולטה למדעי רוח", Campus.HarHaZofim,
				31.792534, 35.24564));

		_locations.add(new Location("אגודת סטודנטים דיקנט סטודנטים",
				Campus.HarHaZofim, 31.792534, 35.24564));
		_locations.add(new Location("הנהלת האוניברסיטה", Campus.HarHaZofim,
				31.793252, 35.243795));
		_locations.add(new Location("כלכלה ומכון פאלק", Campus.HarHaZofim,
				31.793963, 35.243411));
		_locations.add(new Location("יחבל ומדע המדינה", Campus.HarHaZofim,
				31.794141, 35.243205));
		_locations.add(new Location("פקולטה למדעי רוח", Campus.HarHaZofim,
				31.794346, 35.242971));
		_locations.add(new Location("פסיכולוגיה וסוציאולוגיה",
				Campus.HarHaZofim, 31.794378, 35.242405));
		_locations.add(new Location("גאוגרפיה", Campus.HarHaZofim, 31.794378,
				35.242405));
		_locations.add(new Location("דיקנט פקולטה למדעי חברה",
				Campus.HarHaZofim, 31.794134, 35.242655));

		// givat ram
		_locations.add(new Location("בניין המנהלה שרמן", Campus.GivatRam,
				31.778904, 35.196674));
		_locations.add(new Location("אודיטוריום וייז", Campus.GivatRam,
				31.778619, 35.196376));
		_locations.add(new Location("בניין פעלדמן", Campus.GivatRam, 31.778232,
				35.196226));
		_locations.add(new Location(
				"המרכז לחקר הרציונליות והחלטות האינטראקטיביות",
				Campus.GivatRam, 31.778118, 35.196403));
		_locations.add(new Location("מרכז חינור לחקר תולדות ישראל",
				Campus.GivatRam, 31.778145, 35.196142));
		_locations.add(new Location("המכון ללימודים מתקדמים", Campus.GivatRam,
				31.778084, 35.195979));
		_locations.add(new Location("קשרי אוניברסיטה קהילה", Campus.GivatRam,
				31.77791, 35.196311));
		_locations.add(new Location("פיסיקה", Campus.GivatRam, 31.777883,
				35.196151));
		_locations.add(new Location("מרכז בינתחומי", Campus.GivatRam,
				31.777917, 35.195971));
		_locations.add(new Location("בניין שפרינצק", Campus.GivatRam,
				31.777803, 35.19633));
		_locations.add(new Location("בניין ברמן", Campus.GivatRam, 31.777477,
				35.196333));
		_locations.add(new Location(
				"עשבייה, מעבדות בוטניקה, מאגרי מידע גיאגרפיים",
				Campus.GivatRam, 31.777265, 35.196207));
		_locations.add(new Location("בניין לובין", Campus.GivatRam, 31.777083,
				35.196185));
		_locations.add(new Location("בניין לוי, מעבדת מחשבים", Campus.GivatRam,
				31.776928, 35.196242));
		_locations.add(new Location(
				"דיקנט הפקולטה למתמטיקה ולמדעי הטבע מזכירות הוראה ותלמידים",
				Campus.GivatRam, 31.77677, 35.196336));
		_locations.add(new Location("ארכיואנות ומדע", Campus.GivatRam,
				31.776786, 35.195941));
		_locations.add(new Location("מעבדת מחשבים אקואריום", Campus.GivatRam,
				31.776629, 35.196124));
		_locations.add(new Location("בניין קפלן, אולם רוטברג", Campus.GivatRam,
				31.776259, 35.196129));
		_locations.add(new Location("בניין רוס, בית ספר להנדסה למדעי המחשב",
				Campus.GivatRam, 31.775087, 35.196872));
		_locations.add(new Location("בית בלגיה", Campus.GivatRam, 31.77485,
				35.196261));
		_locations.add(new Location("מכון קזאלי לכימיה יישומית",
				Campus.GivatRam, 31.77428, 35.196531));
		_locations.add(new Location("מכון חיים וייצמן לכימיה", Campus.GivatRam,
				31.774488, 35.197256));
		_locations.add(new Location("לוס אנג'לס", Campus.GivatRam, 31.774292,
				35.197213));
		_locations.add(new Location("ארונברג", Campus.GivatRam, 31.774045,
				35.19717));
		_locations.add(new Location("וכטל", Campus.GivatRam, 31.774025,
				35.196784));
		_locations.add(new Location("אלברמן", Campus.GivatRam, 31.773753,
				35.197001));
		_locations.add(new Location("בריקמן", Campus.GivatRam, 31.773551,
				35.197221));
		_locations.add(new Location("פילדלפיה", Campus.GivatRam, 31.773197,
				35.197154));
		_locations.add(new Location("מכון סילברמן למדעי החיים",
				Campus.GivatRam, 31.772481, 35.197274));
		_locations.add(new Location("בניין וולפסון", Campus.GivatRam, 31.772,
				35.19684));
		_locations.add(new Location("המרכז לביולוגיה מבנית", Campus.GivatRam,
				31.771761, 35.196947));
		_locations.add(new Location("לאון", Campus.GivatRam, 31.770705,
				35.197218));
		_locations.add(new Location("המכון למדעי כדור הארץ", Campus.GivatRam,
				31.770497, 35.197261));
		_locations.add(new Location("ספריית המכון למדעי כדור הארץ",
				Campus.GivatRam, 31.770506, 35.197508));
		_locations.add(new Location("בית מלאכה לניפוח זכוכית", Campus.GivatRam,
				31.771396, 35.198779));
		_locations.add(new Location("בניין לב", Campus.GivatRam, 31.771482,
				35.198843));
		_locations.add(new Location("האקדמיה ללשון העברית", Campus.GivatRam,
				31.77213, 35.198264));
		_locations.add(new Location("מרקס", Campus.GivatRam, 31.772907,
				35.198873));
		_locations.add(new Location("בניין דנציגר א'", Campus.GivatRam,
				31.773421, 35.198996));
		_locations.add(new Location("דנציגר ב'", Campus.GivatRam, 31.774613,
				35.198656));
		_locations.add(new Location("מכון איינשטיין למתמטיקה בניין מנצ'סטר",
				Campus.GivatRam, 31.774661, 35.198068));
		_locations.add(new Location("מכון רקח לפיסיקה בניין קפלון",
				Campus.GivatRam, 31.775021, 35.198007));
		_locations.add(new Location("בניין לוין מכון רקח לפיסיקה הנהלה",
				Campus.GivatRam, 31.775352, 35.197961));
		_locations.add(new Location("אולם קנדה", Campus.GivatRam, 31.778097,
				35.198041));
		_locations.add(new Location("מרכז מעבדות למדעים בלמונטה",
				Campus.GivatRam, 31.778788, 35.19887));
		_locations.add(new Location("היחידה הקריאוגנית", Campus.GivatRam,
				31.774736, 35.199061));
		_locations.add(new Location("מעבדת דארוף לאיזוטופים", Campus.GivatRam,
				31.774342, 35.199321));
		_locations.add(new Location("בית צרפת", Campus.GivatRam, 31.770251,
				35.199082));
		_locations.add(new Location("דבורסקי", Campus.GivatRam, 31.769672,
				35.198806));
		_locations.add(new Location("בית ספר למדע יישומי הרמן",
				Campus.GivatRam, 31.772565, 35.199611));
		_locations.add(new Location("בניין ברגמן", Campus.GivatRam, 31.772757,
				35.199739));
		_locations.add(new Location("ספריית הרמן למדעי הטבע", Campus.GivatRam,
				31.776433, 35.196252));

		// ein karem
		_locations.add(new Location("בית ספר לאחיות", Campus.EinKarem,
				31.765836, 35.148278));
		_locations.add(new Location("בית ספר לבריאות הציבור", Campus.EinKarem,
				31.765756, 35.1483));
		_locations.add(new Location("המרכז הרפואי הדסה עין כרם",
				Campus.EinKarem, 31.765622, 35.149668));
		_locations.add(new Location("מרכז לאם ולילד", Campus.EinKarem,
				31.765271, 35.148222));
		_locations.add(new Location("המחלקה לסיווג רקמות", Campus.EinKarem,
				31.764495, 35.148619));
		_locations.add(new Location("בית ספר לרקחות", Campus.EinKarem,
				31.764165, 35.148775));
		_locations.add(new Location("הפקולטה לרפואת שיניים", Campus.EinKarem,
				31.764409, 35.149196));
		_locations.add(new Location("אודיטוריום מגיד", Campus.EinKarem,
				31.764053, 35.149504));
		_locations.add(new Location("ספרייה רפואית", Campus.EinKarem,
				31.764108, 35.14977));
		_locations.add(new Location("הנהלת הפקולטה", Campus.EinKarem,
				31.764299, 35.149649));
		_locations.add(new Location("מעבדות מחקר", Campus.EinKarem, 31.764664,
				35.149432));
		_locations.add(new Location("הפקולטה לרפואה", Campus.EinKarem,
				31.764771, 35.149931));
		_locations.add(new Location("מרכז הסטודנט פורשהיימר", Campus.EinKarem,
				31.763553, 35.149491));
		_locations.add(new Location("בניין גודלסקי", Campus.EinKarem,
				31.763236, 35.149402));
		_locations.add(new Location("מעבדות הוראה", Campus.EinKarem, 31.763106,
				35.149456));
		_locations.add(new Location("הבניין הבינלאומי, מעבדות מחקר והוראה",
				Campus.EinKarem, 31.76343, 35.150043));
		_locations.add(new Location("מרכז בוטנר", Campus.EinKarem, 31.76359,
				35.148992));
		// rehovot

		_locations.add(new Location("מרכז לחקר דבורים טריווקס", Campus.Rehovot,
				31.907409, 34.80414));
		_locations.add(new Location("מדור ללימודי חוץ", Campus.Rehovot,
				31.90678, 34.805093));
		_locations.add(new Location("מרכז יקא", Campus.Rehovot, 31.906675,
				34.805363));
		_locations.add(new Location("מרכז אוטו ורבורג לביוטכנולוגיה חקלאית",
				Campus.Rehovot, 31.906357, 34.804867));
		_locations.add(new Location("בניין אהרונסון", Campus.Rehovot,
				31.906195, 34.805058));
		_locations.add(new Location("מנהלה, מזכירות לענייני ההוראה,לשכת דיקן",
				Campus.Rehovot, 31.906357, 34.804226));
		_locations.add(new Location("כיתה 84", Campus.Rehovot, 31.906477,
				34.803859));
		_locations.add(new Location("בתי מלאכה", Campus.Rehovot, 31.906445,
				34.802563));
		_locations.add(new Location("מרכז קנדי לי ב'", Campus.Rehovot,
				31.906049, 34.803341));
		_locations.add(new Location("בניין קנדי לי ג'", Campus.Rehovot,
				31.90553, 34.802362));
		_locations.add(new Location("פיטוטרון", Campus.Rehovot, 31.905724,
				34.801673));
		_locations.add(new Location("מרכז בוטנרבניין פרנקלין אלפר",
				Campus.Rehovot, 31.905225, 34.802638));
		_locations.add(new Location("חממות מחקר הוראה", Campus.Rehovot,
				31.904553, 34.801171));
		_locations.add(new Location("בניין מוסקונה", Campus.Rehovot, 31.904838,
				34.803228));
		_locations.add(new Location("בניין אריוביץ ג'", Campus.Rehovot,
				31.904972, 34.803797));
		_locations.add(new Location("מרכז קנדי לי א'", Campus.Rehovot,
				31.905346, 34.804114));
		_locations.add(new Location("מעבדות מחקר מרכז וולקני", Campus.Rehovot,
				31.905056, 34.804315));
		_locations.add(new Location("בניין לאוטרמן", Campus.Rehovot, 31.904597,
				34.804513));
		_locations.add(new Location("בניין איזנברג", Campus.Rehovot, 31.904745,
				34.804964));
		_locations.add(new Location("בניין לובל", Campus.Rehovot, 31.904988,
				34.804873));
		_locations.add(new Location("בניין אריוביץ ב'", Campus.Rehovot,
				31.905375, 34.80516));
		_locations.add(new Location("וירולוגיה", Campus.Rehovot, 31.904726,
				34.805607));
		_locations.add(new Location("בניין וולקני", Campus.Rehovot, 31.905368,
				34.805693));
		_locations.add(new Location("אודיטוריום אריוביץ'", Campus.Rehovot,
				31.905644, 34.805388));
		_locations.add(new Location("שירותי יעוץ לסטודנט", Campus.Rehovot,
				31.903943, 34.804637));
		_locations.add(new Location("בית ספר לוטרינריה על שם קורט",
				Campus.Rehovot, 31.904874, 34.804092));
		Database.getInstance(context).addLocations(_locations);

	}

	/**
	 * Location object
	 * 
	 * @author alonaba
	 * 
	 */
	public static class Location {
		private Campus _campus;
		private String _place;
		private double _longitude;
		private double _latitude;

		/**
		 * Constructor
		 * 
		 * @param place
		 *            - place
		 * @param campus
		 *            - campus
		 * @param longitude
		 *            - the longituda
		 * @param latitude
		 *            - the latitude
		 */
		public Location(String place, Campus campus, double longitude,
				double latitude) {
			_place = place;
			_campus = campus;
			_longitude = longitude;
			_latitude = latitude;
		}

		/**
		 * Get longitude
		 * 
		 * @return longitude
		 */
		public double getLongitude() {
			return _longitude;
		}

		/**
		 * Get latitude
		 * 
		 * @return latitude
		 */
		public double getLatitude() {
			return _latitude;
		}

		/**
		 * get Place name
		 * 
		 * @return place name
		 */
		public String getPlace() {
			return _place;
		}

		/**
		 * Get campus
		 * 
		 * @return campus
		 */
		public int getCampus() {
			return _campus.ordinal();
		}
	}

}
