package il.ac.huji.hujime;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

public class StaticMapsFragment extends Fragment {
	public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	@SuppressLint("SdCardPath")
	final static String _location = "/data/data/il.ac.huji.hujime/files/";

	public static final StaticMapsFragment newInstance(int type) {
		StaticMapsFragment f = new StaticMapsFragment();
		Bundle bdl = new Bundle(1);
		bdl.putInt(EXTRA_MESSAGE, type);
		f.setArguments(bdl);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_static_maps, container,
				false);
		Spinner chooser = (Spinner) v
				.findViewById(R.id.fragment_static_maps_campus_choose);
		final WebView image = (WebView) v
				.findViewById(R.id.fragment_static_maps_campus_image);
		chooser.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String imageFile = "<html>" + "<body>" + "<img src=";
				switch (arg2) {
				case 0:
					imageFile += _location + getString(R.string.static_map_HZ);
					break;
				case 1:
					imageFile += _location + getString(R.string.static_map_GR);
					break;
				case 2:
					imageFile += _location + getString(R.string.static_map_EK);
					break;
				case 3:
					imageFile += _location + getString(R.string.static_map_RH);
					break;
				}
				imageFile += " />" + " </body>" + "</html>";
				image.loadDataWithBaseURL("file://" + _location, imageFile,
						"text/html", "windows-1252", "");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// do nothing

			}
		});
		return v;
	}
}