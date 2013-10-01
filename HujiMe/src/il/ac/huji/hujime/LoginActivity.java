package il.ac.huji.hujime;

import il.ac.huji.hujime.HujiSync.Sync;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends Activity {
	Context _thisContext;
	HujiSession _sessionToHuji;
	private SessionManager _session;
	private static final boolean API_LEVEL_11 = android.os.Build.VERSION.SDK_INT > 11;
	private String idString;
	private Dialog _dialog;
	private String codeString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_login);
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(0);
		actionBar.setSubtitle(getString(R.string.title_authentication));
		actionBar.show();

		// allow internet access
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		CookieSyncManager.createInstance(this);
		_session = new SessionManager(getApplicationContext());
		// check internet, get captcha and display it
		_thisContext = this;
		_sessionToHuji = HujiSession.getInstance();
		// get captcha
		getCaptcha();
		Button registerHuji = (Button) findViewById(R.id.activity_login_enter_button);
		registerHuji.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkInternet()) {
					EditText id = (EditText) findViewById(R.id.activity_login_id);
					idString = id.getText().toString();
					EditText code = (EditText) findViewById(R.id.activity_login_pass);
					codeString = code.getText().toString();
					EditText captcha = (EditText) findViewById(R.id.activity_login_capcha);
					String captchaString = captcha.getText().toString();
					// check if all data exists
					if (idString.equals("") || codeString.equals("")
							|| captchaString.equals("")) {
						displayNotification(_thisContext
								.getString(R.string.message_login_no_data));
					} else {
						if (!_sessionToHuji.authenticate(idString, codeString,
								captchaString)) {
							displayNotification(_sessionToHuji.getLastMessage());
							getCaptcha();
							return;
						}
						HujiSyncronization htmlGet = new HujiSyncronization();
						HujiSync sync = new HujiSync(_thisContext);
						Sync[] syncs = new Sync[] { sync.gradesSync,
								sync.lessonsSync, sync.examsSync };
						if (API_LEVEL_11) {
							htmlGet.executeOnExecutor(
									AsyncTask.THREAD_POOL_EXECUTOR, syncs);
						} else {
							htmlGet.execute(syncs);
						}
					}
				}
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.no_menu, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		if (_dialog != null) {
			if (_dialog.isShowing()) {
				_dialog.dismiss();
			}
			_dialog = null;
		}
		super.onDestroy();
	}

	/**
	 * Async task that gets data from huji web site
	 * 
	 * @author alonaba
	 * 
	 */
	private class HujiSyncronization extends AsyncTask<Sync, Void, Boolean> {
		private int[] _texts = { R.string.sync_grades, R.string.sync_lessons,
				R.string.sync_exams };
		private int _currentAction;
		private ProgressDialog _dialog;
		private TextView _text;

		@Override
		protected Boolean doInBackground(Sync... params) {
			for (Sync sync : params) {
				if (!sync.sync()) {
					return false;
				}
				publishProgress();
			}
			// get maps
			for (int i = 0; i < 4; i++) {
				GetImageThread image = new GetImageThread(i);
				image.run();
				try {
					image.join();
				} catch (InterruptedException e) {
				}
			}

			return true;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			_dialog.dismiss();
			if (!result) {
				displayNotification(_sessionToHuji.getLastMessage());
				getCaptcha();
			} else {
				// create locations Db
				Maps.getInstance().createLocations(_thisContext);
				_session.createLoginSession(idString, codeString);
				startActivity(new Intent(_thisContext, LessonsActivity.class));
				finish();
			}
		}

		@Override
		protected void onPreExecute() {
			_dialog = new ProgressDialog(LoginActivity.this);
			_dialog.show();
			_dialog.setContentView(R.layout.io_progress);
			_currentAction = 0;
			_dialog.setCancelable(false);
			_text = (TextView) _dialog.findViewById(R.id.io_progress_text);
			_text.setText(getString(_texts[_currentAction]));
			Database.getInstance(_thisContext).createIfNotExists();
		}

		@Override
		protected void onProgressUpdate(Void... texta) {
			switch (_currentAction) {
			case 0:
				_currentAction++;
				_text.setText(getString(_texts[_currentAction]));
				break;
			case 1:
				_currentAction++;
				_text.setText(getString(_texts[_currentAction]));
				break;
			case 2:
				break;
			default:
				// do nothing
			}
		}
	}

	/**
	 * Class that extends thread and downloads maps from internet
	 * 
	 * @author alonaba
	 * 
	 */
	@SuppressLint("SdCardPath")
	private class GetImageThread extends Thread {
		String _url;
		String _name;
		final static String _location = "/data/data/il.ac.huji.hujime/files";

		public GetImageThread(int type) {
			switch (type) {
			case 0:
				_url = "http://studean.huji.ac.il/.upload/mt_scopus2009.jpg";
				_name = getString(R.string.static_map_HZ);
				break;
			case 1:
				_url = "http://studean.huji.ac.il/.upload/givaram2009.jpg";
				_name = getString(R.string.static_map_GR);
				break;
			case 2:
				_url = "http://studean.huji.ac.il/.upload/ein.jpg";
				_name = getString(R.string.static_map_EK);
				break;
			case 3:
				_url = "http://studean.huji.ac.il/.upload/rehovot2oo9.jpg";
				_name = getString(R.string.static_map_RH);
				break;
			}
		}

		@Override
		public void run() {
			File mFolder = new File(_location);
			if (!mFolder.exists()) {
				mFolder.mkdir();
			}
			Bitmap image = _sessionToHuji.getImage(_url);
			if (image == null) {
				return;
			}
			try {
				File file = new File(mFolder.getAbsolutePath(), _name);
				FileOutputStream fOut = new FileOutputStream(file);
				// Here path is either sdcard or internal storage
				image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
				fOut.flush();
				fOut.close();
			} catch (IOException e) {
			}

		}
	};

	/**
	 * Method that gets the captcha
	 */
	private void getCaptcha() {
		if (checkInternet()) {
			Bitmap image = _sessionToHuji.getCaptcha();
			if (image == null) {
				displayNotification(getString(R.string.message_huji_connection_error));
			} else {
				ImageView captcha = (ImageView) findViewById(R.id.activity_login_capcha_image);
				captcha.setImageBitmap(image);
			}
		}
	}

	/**
	 * Method that checks Internet connection
	 * 
	 * @return true if there is Internet access, false otherwise
	 */
	private boolean checkInternet() {
		if (!checkInternetConnection()) {
			displayNotification(getString(R.string.message_no_intenet_error));
			return false;
		}
		if (!_sessionToHuji.checkInternetAccess()) {
			displayNotification(getString(R.string.message_some_internet_error));
			return false;
		}
		return true;
	}

	/*
	 * Method that check that one network adapter at least is connected
	 * 
	 * @return true, if yes, false otherwise
	 */
	private boolean checkInternetConnection() {
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Method that displays notification
	 * 
	 * @param data
	 *            - data to display
	 */
	private void displayNotification(String data) {
		_dialog = new Dialog(this);
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
			}

		});
	}
}
