package il.ac.huji.hujime;

import il.ac.huji.hujime.Lessons.Lesson;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
/**
 * Lessons Adapter 
 * @author alonaba
 *
 */
public class LessonsAdapter extends ArrayAdapter<Lesson> {
	Activity _activity;
	Lesson[] _lessons;
	/**
	 * Constructor
	 * @param activity
	 * @param lessons
	 */
	public LessonsAdapter(Activity activity, Lesson[] lessons) {
		super(activity, android.R.layout.simple_list_item_1, lessons);
		_activity = activity;
		_lessons = lessons;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Lesson lesson = (Lesson) getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.one_lesson, null);
		TextView lessonName = (TextView) view
				.findViewById(R.id.one_lesson_name);
		TextView lessonType = (TextView) view
				.findViewById(R.id.one_lesson_type);
		TextView lessonPlace = (TextView) view
				.findViewById(R.id.one_lesson_place);
		TextView hours = (TextView) view.findViewById(R.id.one_lesson_hour);
		hours.setText((position + 8) + ":00");
		if (lesson == null) {
			return view;
		}
		lessonName.setText(lesson.getName());
		lessonPlace.setText(lesson.getPlace());
		switch (lesson.getType()) {
		case 0:
			lessonType.setText(_activity.getString(R.string.lesson_type_shiur));
			break;
		case 1:
			lessonType.setText(_activity.getString(R.string.lesson_type_maabada));
			break;
		case 2:
			lessonType.setText(_activity.getString(R.string.lesson_type_tirgul));
			break;
		case 3:
			lessonType.setText(_activity.getString(R.string.lesson_type_hadraha));
			break;
		case 4:
			lessonType.setText(_activity.getString(R.string.lesson_type_mehina));
			break;
		case 5:
			lessonType.setText(_activity.getString(R.string.lesson_type_sadna));
			break;
		case 6:
			lessonType.setText(_activity.getString(R.string.lesson_type_seminarion));
			break;
		case 7:
			lessonType.setText(_activity.getString(R.string.lesson_type_matala));
			break;
		case 8:
			lessonType.setText(_activity.getString(R.string.lesson_type_siur));
			break;
		case 9:
			lessonType.setText(_activity.getString(R.string.lesson_type_shiur_seminarion));
			break;
		default:
			lessonType.setText("");
			break;
		}
		return view;
	}

	@Override
	public Lesson getItem(int position) {
		return _lessons[position];

	}
}
