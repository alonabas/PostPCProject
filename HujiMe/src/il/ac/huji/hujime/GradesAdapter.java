package il.ac.huji.hujime;

import il.ac.huji.hujime.Grades.Grade;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adapter for each page of grades
 * 
 * @author alonaba
 * 
 */
public class GradesAdapter extends ArrayAdapter<Grade> {
	Activity _activity;

	public GradesAdapter(Activity activity, ArrayList<Grade> grades) {
		super(activity, android.R.layout.simple_list_item_1, grades);
		_activity = activity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Grade grade = (Grade) getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.one_grade, null);
		TextView gradeName = (TextView) view.findViewById(R.id.one_grade_name);
		TextView gradeId = (TextView) view.findViewById(R.id.one_grade_id);
		TextView gradeGrade = (TextView) view
				.findViewById(R.id.one_grade_grade);
		TextView gradePoints = (TextView) view
				.findViewById(R.id.one_grade_points);
		gradeName.setText(grade.getName());
		gradeId.setText(grade.getId());
		gradeGrade.setText(String.valueOf(grade.getGrade()));
		gradePoints.setText(String.valueOf(grade.getPoints()));
		return view;
	}

}
