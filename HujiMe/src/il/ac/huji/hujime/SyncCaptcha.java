package il.ac.huji.hujime;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SyncCaptcha extends Activity {
	private final String _huji_personal_data = "https://www.huji.ac.il/dataj/controller/student/?";
	HujiSession _sessionToHuji;
	private SessionManager _session;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_captcha);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		_sessionToHuji = HujiSession.getInstance();
		_session = new SessionManager(getApplicationContext());
		// check for internet
		if (!StaticMethods.checkInternetConnection(this)) {
			displayDialog(this,
					getString(R.string.message_no_intenet_error));
		} else if (!StaticMethods.checkInternetAccess(_huji_personal_data)) {
			displayDialog(this, getString(R.string.message_some_internet_error));
		}
		Bitmap image = _sessionToHuji.getCaptcha();
		if (image == null) {
			displayDialog(this,getString(R.string.message_huji_connection_error));
		} else {
			ImageView captcha = (ImageView) findViewById(R.id.captcha_image);
			captcha.setImageBitmap(image);
		}
		Button enter = (Button) findViewById(R.id.captcha_enter);
		enter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent resultIntent = new Intent();
				EditText captcha = (EditText) findViewById(R.id.captcha_text);
				String captchaString = captcha.getText().toString();
				if (captchaString.equals("")) {
					displayDialog(SyncCaptcha.this,getString(R.string.message_login_no_data));
				}
				// authenticate
				else {
					if (!_sessionToHuji.authenticate(_session.getID(),
							_session.getCode(), captchaString)) {
						displayDialog(SyncCaptcha.this,
								_sessionToHuji.getLastMessage());
					} else {
						setResult(RESULT_OK, resultIntent);
						finish();
					}
				}
			}

		});
		Button close = (Button) findViewById(R.id.captcha_close);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}

		});
	}

	public void displayDialog(Context context, String data) {
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialog_notification);
		dialog.setCancelable(true);
		// set the text
		TextView text = (TextView) dialog
				.findViewById(R.id.dialog_notification_text);
		text.setText(data);
		dialog.show();
		Button cancel = (Button) dialog
				.findViewById(R.id.dialog_notification_cancel_button);
		cancel.setOnClickListener(new OnClickListener() {
			// if cancel pressed, close dialog
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}
}
