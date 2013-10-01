package il.ac.huji.hujime;

import il.ac.huji.hujime.Exams.Exam;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adapter for exams
 * 
 * @author alonaba
 * 
 */
public class ExamsAdapter extends ArrayAdapter<Exam> {
	Activity _activity;

	public ExamsAdapter(Activity activity, ArrayList<Exam> exams) {
		super(activity, android.R.layout.simple_list_item_1, exams);
		_activity = activity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Exam exam = (Exam) getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.one_exam, null);
		TextView examNameId = (TextView) view
				.findViewById(R.id.one_exam_name_id);
		TextView examDateTime = (TextView) view
				.findViewById(R.id.one_exam_date_time);
		TextView examPlace = (TextView) view.findViewById(R.id.one_exam_place);
		TextView examMoed = (TextView) view.findViewById(R.id.one_exam_moed);
		examNameId.setText(exam.getId() + "\n" + exam.getName());
		if (exam.getTime() == null) {
			examDateTime.setText(exam.getDate());
		} else {
			examDateTime.setText(exam.getDate() + "\n" + exam.getTime());
		}
		examPlace.setText(exam.getPlace());
		switch (exam.getMoed()) {
		case 1:
			examMoed.setText(R.string.moed_a);
			break;
		case 2:
			examMoed.setText(R.string.moed_b);
			break;
		case 3:
			examMoed.setText(R.string.moed_c);
			break;
		}
		return view;
	}

}
