package il.ac.huji.hujime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class DialogLessonEdit extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// define custom title
		setContentView(R.layout.dialog_lesson_edit);
		Intent intent = getIntent();
		final String name = intent.getStringExtra("name");
		final String place = intent.getStringExtra("place");
		final int position = intent.getIntExtra("position", -1);
		// set title of header
		TextView title = (TextView) findViewById(R.id.dialog_lesson_edit_title);
		title.setText("עריכה של " + name);
		TextView text = (TextView) findViewById(R.id.dialog_lesson_edit_attention);
		text.setText(R.string.messages_edit_lesson_notification);
		// on CANCEL button click action
		findViewById(R.id.dialog_lesson_edit_close).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View view) {
						// do nothing
						setResult(RESULT_CANCELED);
						finish();
					}
				});
		// on OK button click action
		findViewById(R.id.dialog_lesson_edit_enter).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View view) {
						EditText newName = (EditText) findViewById(R.id.dialog_lesson_edit_name);
						EditText newPlace = (EditText) findViewById(R.id.dialog_lesson_edit_place);
						String newNameString = newName.getText().toString();
						String newPlaceString = newPlace.getText().toString();
						// check if all the same
						if (newNameString.equals("")
								&& newPlaceString.equals("")) {
							setResult(RESULT_CANCELED);
							finish();
						} else if (newNameString.equals(name)
								&& newPlaceString.equals(place)) {
							setResult(RESULT_CANCELED);
							finish();
						}
						Intent resultIntent = new Intent();
						resultIntent.putExtra("position", position);
						if (newNameString.equals("")) {
							resultIntent.putExtra("new_name", name);
						} else {
							resultIntent.putExtra("new_name", newNameString);
						}
						if (newPlaceString.equals("")) {
							resultIntent.putExtra("new_place", place);
						} else {
							resultIntent.putExtra("new_place", newPlaceString);
						}
						resultIntent.putExtra("old_name", name);
						setResult(RESULT_OK, resultIntent);
						finish();
					}
				});
	}
}
