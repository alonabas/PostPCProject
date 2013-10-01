package il.ac.huji.hujime;


/**
 * Path in huji secure site
 * 
 * @author alonaba
 * 
 */
public enum Path {
	PATH_GRADES("TAL-ZIYUNIM?safa=H"), PATH_LESSONS_SEMESTER_A(
			"TAL-LUACHSHAOT?semester=1"), PATH_LESSONS_SEMESTER_B(
			"TAL-LUACHSHAOT?semester=2"), PATH_EXAMS("TAL-LUACHBCHINOT");
	private String _path;

	/**
	 * constructor
	 * 
	 * @param path
	 */
	Path(String path) {
		_path = path;
	}

	/**
	 * Get string of path
	 * 
	 * @return path
	 */
	public String getPath() {
		return _path;
	}
}
