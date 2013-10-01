package il.ac.huji.hujime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class DialogLessonDelete extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// define custom title
		setContentView(R.layout.dialog_lesson_delete);
		Intent intent = getIntent();
		final String name = intent.getStringExtra("name");
		final int position = intent.getIntExtra("position", -1);
		// set title of header
		TextView title = (TextView) findViewById(R.id.dialog_lesson_delete_title);
		title.setText("מחיקת " + name);
		// on CANCEL button click action
		TextView text = (TextView) findViewById(R.id.dialog_lesson_delete_attention);
		text.setText(getString(R.string.messages_delete_lesson_notification)
				+ " " + name);

		findViewById(R.id.dialog_lesson_delete_close).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View view) {
						// do nothing
						setResult(RESULT_CANCELED);
						finish();
					}
				});
		// on OK button click action
		findViewById(R.id.dialog_lesson_delete_enter).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent resultIntent = new Intent();
						resultIntent.putExtra("position", position);
						resultIntent.putExtra("name", name);
						setResult(RESULT_OK, resultIntent);
						finish();
					}
				});
	}
}