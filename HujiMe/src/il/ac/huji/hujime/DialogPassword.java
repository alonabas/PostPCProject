package il.ac.huji.hujime;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DialogPassword extends Activity {
	Dialog _dialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_password);
		final SessionManager session = new SessionManager(
				getApplicationContext());
		Button enter = (Button) findViewById(R.id.dialog_password_enter);
		enter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent resultIntent = new Intent();
				EditText password = (EditText) findViewById(R.id.dialog_password_text);
				String passwordString = password.getText().toString();
				if (passwordString.equals(session.getCode())) {
					setResult(RESULT_OK, resultIntent);
					finish();
				}
				// authenticate
				else {
					displayNotification(DialogPassword.this,
							getString(R.string.message_illegal_code));
				}
			}

		});
		Button close = (Button) findViewById(R.id.dialog_password_close);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}

		});
	}

	@Override
	protected void onDestroy() {
		// if dialog is opened close it
		if (_dialog != null) {
			if (_dialog.isShowing()) {
				_dialog.dismiss();
			}
			_dialog = null;
		}
		super.onDestroy();
	}

	/**
	 * Method that displays the notification
	 * 
	 * @param context
	 *            - the context
	 * @param data
	 *            - the string to display
	 */
	public void displayNotification(Context context, String data) {
		_dialog = new Dialog(context);
		_dialog.setContentView(R.layout.dialog_notification);
		_dialog.setCancelable(true);
		// set the text
		TextView text = (TextView) _dialog
				.findViewById(R.id.dialog_notification_text);
		text.setText(data);
		_dialog.show();
		Button cancel = (Button) _dialog
				.findViewById(R.id.dialog_notification_cancel_button);
		cancel.setOnClickListener(new OnClickListener() {
			// if cancel pressed, close dialog
			@Override
			public void onClick(View v) {
				_dialog.dismiss();
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}
}
