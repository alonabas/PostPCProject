package il.ac.huji.hujime;

import il.ac.huji.hujime.Grades.Grade;

import java.util.ArrayList;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * Adapter for grades in select list
 * 
 * @author alonaba
 * 
 */
public class GradesSelectAdapter extends BaseAdapter {
	Context _context;
	LayoutInflater _inflater;
	SparseBooleanArray _sparseBooleanArray;
	ArrayList<Grade> _grades;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            - context
	 * @param grades
	 *            - list of grades
	 */
	public GradesSelectAdapter(Context context, ArrayList<Grade> grades) {
		this._context = context;
		_inflater = LayoutInflater.from(_context);
		_sparseBooleanArray = new SparseBooleanArray();
		// fill all with true
		for (int i = 0; i < grades.size(); i++) {
			_sparseBooleanArray.put(i, true);
		}
		_grades = grades;
	}

	/**
	 * Get list of selected items in list
	 * 
	 * @return list of grades, was selected by user
	 */
	public ArrayList<Grade> getCheckedItems() {
		ArrayList<Grade> selectedList = new ArrayList<Grade>();
		for (int i = 0; i < _grades.size(); i++) {
			if (_sparseBooleanArray.get(i)) {
				selectedList.add(_grades.get(i));
			}
		}
		return selectedList;
	}

	@Override
	public int getCount() {
		return _grades.size();
	}

	@Override
	public Grade getItem(int position) {
		return _grades.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = _inflater.inflate(R.layout.one_grade_choose, null);
		}
		Grade grade = _grades.get(position);
		TextView gradeName = (TextView) convertView
				.findViewById(R.id.one_grade_choose_name);
		gradeName.setText(grade.getName());
		TextView gradeGrade = (TextView) convertView
				.findViewById(R.id.one_grade_choose_grade);
		gradeGrade.setText(String.valueOf(grade.getGrade()));
		TextView gradePoints = (TextView) convertView
				.findViewById(R.id.one_grade_choose_points);
		gradePoints.setText(String.valueOf(grade.getPoints()));
		CheckBox checkBox = (CheckBox) convertView
				.findViewById(R.id.one_grade_choose_checkbox);
		checkBox.setTag(position);
		checkBox.setChecked(_sparseBooleanArray.get(position));
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				int key = (Integer) buttonView.getTag();
				_sparseBooleanArray.put(key, isChecked);
			}
		});
		return convertView;
	}
}