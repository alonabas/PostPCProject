package il.ac.huji.hujime;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Fragment of grades, fragment per year
 * 
 * @author alonaba
 * 
 */
public class GradesFragment extends Fragment {
	public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	private Database _db;
	private ListView _listView;
	private LayoutInflater _inflater;
	private GradesAdapter _gradesAdapter;

	/**
	 * Get instance of fragment
	 * 
	 * @param year
	 *            - the year
	 * @param context
	 *            - the context
	 * @return
	 */
	public static final GradesFragment newInstance(int year, Context context) {
		GradesFragment f = new GradesFragment();
		Bundle bdl = new Bundle(1);
		bdl.putInt(EXTRA_MESSAGE, year);
		f.setArguments(bdl);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		_inflater = inflater;
		_db = Database.getInstance(getActivity());
		int year = getArguments().getInt(EXTRA_MESSAGE);
		View v = inflater.inflate(R.layout.general_list_view, container, false);
		_listView = (ListView) v.findViewById(R.id.general_list_view);
		_gradesAdapter = new GradesAdapter(getActivity(),
				_db.getGradesByYear(year));
		_listView.addHeaderView(defineHeader(), null, false);
		_listView.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_RIGHT);
		_listView.setAdapter(_gradesAdapter);
		return v;
	}

	/**
	 * Method that defines header of the list view exams
	 * 
	 * @return - View of header
	 */
	private View defineHeader() {
		View header = _inflater.inflate(R.layout.header_one_grade, null);
		TextView grade = (TextView) header
				.findViewById(R.id.header_one_grade_grade);
		TextView points = (TextView) header
				.findViewById(R.id.header_one_grade_points);
		TextView name = (TextView) header
				.findViewById(R.id.header_one_grade_name);
		TextView id = (TextView) header.findViewById(R.id.header_one_grade_id);
		grade.setText(R.string.header_grade_grade);
		points.setText(R.string.header_grade_points);
		name.setText(R.string.header_grade_name);
		id.setText(R.string.header_grade_id);
		return header;

	}
}