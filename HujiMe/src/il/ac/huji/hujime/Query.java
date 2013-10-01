package il.ac.huji.hujime;

/**
 * Query object to create queries to huji web site
 * 
 * @author alonaba
 * 
 */
public class Query {
	/**
	 * Arguments
	 * 
	 * @author alonaba
	 * 
	 */
	public enum Argument {
		AUTH_ID("itz_id"), AUTH_PASS("itz_code"), AUTH_CAPTCHA(
				"captcha_session_key"), GRADES_YEAR("yearsafa"), EXAMS_YEAR(
				"yearno"), PEULA("peula"), STARTING("starting"), YEAR("year"), COURSE(
				"course"), FACULCY("faculty"), PRISA("prisa"), WORD("word"), OPTION(
				"option"), LANGUAGE("language"), SHIUR("shiur");
		private String _argumentName;

		/**
		 * Constructor
		 * 
		 * @param argumentName
		 */
		Argument(String argumentName) {
			_argumentName = argumentName;
		}

		/**
		 * Get string representation of argument
		 * 
		 * @return
		 */
		public String getArgument() {
			return _argumentName;
		}

		/**
		 * Get query
		 * 
		 * @param val
		 *            - set value to argument
		 * @return - full query as string
		 */
		public String getQuery(String val) {
			return _argumentName + "=" + val;
		}
	}

	String _query;

	/**
	 * Constructor
	 * 
	 * @param arg
	 *            - argument
	 * @param val
	 *            - value
	 */
	public Query(Argument arg, String val) {
		_query = arg.getQuery(val);
	}

	/**
	 * Add argument to query
	 * 
	 * @param arg
	 *            - argument
	 * @param val
	 *            - value
	 */
	public void addArguments(Argument arg, String val) {
		_query += ("&" + arg.getQuery(val));
	}

	/**
	 * Add argument with default value
	 * 
	 * @param arg
	 *            - argument
	 */
	public void addDefaultArguments(Argument arg) {
		String subQuery = null;
		switch (arg) {
		case PEULA:
			subQuery = arg.getQuery("Simple");
			break;
		case STARTING:
			subQuery = arg.getQuery(String.valueOf(1));
			break;
		case FACULCY:
			subQuery = arg.getQuery(String.valueOf(0));
			break;
		case PRISA:
			subQuery = arg.getQuery(String.valueOf(1));
			break;
		case WORD:
			subQuery = arg.getQuery("");
			break;
		case OPTION:
			subQuery = arg.getQuery(String.valueOf(1));
			break;
		case LANGUAGE:
			subQuery = arg.getQuery("");
			break;
		case SHIUR:
			subQuery = arg.getQuery("");
			break;
		default:
			break;
		}
		if (subQuery != null) {
			_query += ("&" + subQuery);
		}
	}

	/**
	 * Add arguments
	 * 
	 * @param val
	 *            - value
	 */
	public void addArguments(String val) {
		_query += ("&" + val);
	}

	/**
	 * Get query as POST
	 * 
	 * @return query
	 */
	public String getQuery() {
		return _query;
	}

	/**
	 * Get query as GET
	 * 
	 * @return query
	 */
	public String getHttpQuery() {
		return "?" + _query;
	}
}
