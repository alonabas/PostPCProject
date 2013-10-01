package il.ac.huji.hujime;

import il.ac.huji.hujime.GroupsActivity.DisplayLesson;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adapter to display other groups
 * 
 * @author alonaba
 * 
 */
public class GroupsAdapter extends ArrayAdapter<DisplayLesson> {
	GroupsActivity _activity;

	public GroupsAdapter(GroupsActivity activity,
			ArrayList<DisplayLesson> lessons) {
		super(activity, android.R.layout.simple_list_item_1, lessons);
		_activity = activity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DisplayLesson lesson = (DisplayLesson) getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.one_group, null);
		TextView type = (TextView) view
				.findViewById(R.id.lesson_other_group_type);
		TextView time = (TextView) view
				.findViewById(R.id.lesson_other_group_time);
		TextView place = (TextView) view
				.findViewById(R.id.lesson_other_group_place);
		TextView group = (TextView) view
				.findViewById(R.id.lesson_other_group_group);
		TextView lecturer = (TextView) view
				.findViewById(R.id.lesson_other_group_lecturer);
		TextView day = (TextView) view
				.findViewById(R.id.lesson_other_group_day);

		type.setText(lesson.getType());
		time.setText(lesson.getTime());
		place.setText(lesson.getPlace());
		group.setText(lesson.getGroup());
		lecturer.setText(lesson.getLecturer());
		day.setText(lesson.getDay());
		return view;
	}

}