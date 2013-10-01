package il.ac.huji.hujime;

import il.ac.huji.hujime.Maps.Campus;
import il.ac.huji.hujime.Maps.Location;
import il.ac.huji.hujime.Maps.Place;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * The fragment that display Dynamic Maps
 * @author alonaba
 *
 */
public class DynamicMapsFragment extends Fragment {
	public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	private Context _context;
	private Dialog _dialog;
	AutocompleteAdapter _adapter;
	private ArrayList<Place> _rehovot;
	private ArrayList<Place> _einKarem;
	private ArrayList<Place> _givatRam;
	private ArrayList<Place> _harHazofim;
	private AutoCompleteTextView _view;
	private int _currentCampus;

	/**
	 * The instance of fragment
	 * 
	 * @param type
	 *            - type of fragment
	 * @param context
	 *            - context
	 * @return the fragment object
	 */
	public static final DynamicMapsFragment newInstance(int type,
			Context context) {
		DynamicMapsFragment f = new DynamicMapsFragment();
		Bundle bdl = new Bundle(1);
		bdl.putInt(EXTRA_MESSAGE, type);
		f.setArguments(bdl);
		f._context = context;
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_dynamic_maps, container,
				false);
		Maps maps = Maps.getInstance();
		maps.createLocations(_context);
		_rehovot = maps.getPlacesRehovot();
		_einKarem = maps.getPlacesEinKarem();
		_givatRam = maps.getPlacesGivatRam();
		_harHazofim = maps.getPlacesHarHazofim();

		_adapter = defineAdapter(Campus.Unknown);
		_view = (AutoCompleteTextView) v
				.findViewById(R.id.fragment_dynamic_maps_spinner_autocomplete_text);
		_view.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
		_view.setThreshold(1);
		_view.setAdapter(_adapter);
		Spinner chooser = (Spinner) v
				.findViewById(R.id.fragment_dynamic_maps_spinner);
		chooser.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				switch (arg2) {
				case 0:
					_currentCampus = 0;
					_adapter.updateAdapter(_harHazofim, Campus.HarHaZofim);
					break;
				case 1:
					_currentCampus = 1;
					_adapter.updateAdapter(_givatRam, Campus.GivatRam);
					break;
				case 2:
					_currentCampus = 2;
					_adapter.updateAdapter(_einKarem, Campus.EinKarem);
					break;
				case 3:
					_currentCampus = 3;
					_adapter.updateAdapter(_rehovot, Campus.Rehovot);
					break;
				default:

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// do nothing

			}
		});
		Button toMaps = (Button) v
				.findViewById(R.id.fragment_dynamic_maps_google_maps_button);
		toMaps.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String selected = _view.getText().toString();
				Database db = Database.getInstance(_context);
				Location loc = db.queryForPlace(selected, _currentCampus);
				if (loc == null) {
					displayNotification(_context, "נא בחר מיקום מרשימה");
				} else {
					String label = null;
					switch (loc.getCampus()) {
					case 0:
						label = loc.getPlace() + ", הר הצופים";
						break;
					case 1:
						label = loc.getPlace() + ", גבעת רם";
						break;
					case 2:
						label = loc.getPlace() + ", עין כרם";
						break;
					case 3:
						label = loc.getPlace() + ", רחובות";
						break;

					}
					String uriBegin = "geo:" + loc.getLongitude() + ","
							+ loc.getLatitude();
					String query = loc.getLongitude() + "," + loc.getLatitude()
							+ "(" + label + ")";
					String encodedQuery = Uri.encode(query);
					// if no maps application found open in web
					try {
						String uriString = uriBegin + "?q=" + encodedQuery
								+ "&z=16";
						Uri uri = Uri.parse(uriString);
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(intent);
					} catch (ActivityNotFoundException e) {
						String webQuery = "https://maps.google.com/maps?q="
								+ encodedQuery + "&z=15";
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(webQuery));
						startActivity(i);
					}
				}
			}

		});

		return v;
	}

	/**
	 * Method that add all the locations in all campuses to adapter
	 * 
	 * @param campus
	 *            - the
	 * @return adapter
	 */
	private AutocompleteAdapter defineAdapter(Campus campus) {
		ArrayList<Place> all = new ArrayList<Place>();
		all.addAll(_einKarem);
		all.addAll(_rehovot);
		all.addAll(_givatRam);
		all.addAll(_harHazofim);
		AutocompleteAdapter adapter = new AutocompleteAdapter(_context, all,
				campus);
		return adapter;
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
			}
		});
	}
}
