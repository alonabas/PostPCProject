package il.ac.huji.hujime;

import il.ac.huji.hujime.Maps.Campus;
import il.ac.huji.hujime.Maps.Place;

import java.util.ArrayList;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

/**
 * Adapter for autocomplete, defines the filter and display of item in textview authocomplete
 * @author alonaba
 *
 */
public class AutocompleteAdapter extends ArrayAdapter<Place> {

	Context _context;
	ArrayList<Place> _suggestions;
	ArrayList<Place> _allPlaces;
	Campus _campus;
	/**
	 * Constructor
	 * @param context - current context
	 * @param places - the list of places
	 * @param campus - the current campus
	 */
	@SuppressWarnings("unchecked")
	public AutocompleteAdapter(Context context, ArrayList<Place> places,
			Campus campus) {
		super(context, android.R.layout.simple_dropdown_item_1line, places);
		_context = context;
		_suggestions = new ArrayList<Place>();
		_allPlaces = (ArrayList<Place>) places.clone();
		_campus = campus;
	}
	/**
	 * Method that replace the list of place to display
	 * @param places - the new list of places
	 * @param campus - the campus
	 */
	@SuppressWarnings("unchecked")
	public void updateAdapter(ArrayList<Place> places, Campus campus) {
		_suggestions = new ArrayList<Place>();
		_allPlaces = (ArrayList<Place>) places.clone();
		_campus = campus;
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		Place placeName = (Place) getItem(position);
		View view = inflater.inflate(R.layout.autocomplete, null);
		TextView place = (TextView) view.findViewById(R.id.autocomplete_text);
		if (_campus == Campus.Unknown) {
			switch (placeName.getCampus()) {
			case HarHaZofim:
				place.setText(placeName.getPlace() + ", "+_context.getString(R.string.map_name_har_hazofim));
				break;
			case GivatRam:
				place.setText(placeName.getPlace() + ", "+_context.getString(R.string.map_name_givat_ram));
				break;
			case EinKarem:
				place.setText(placeName.getPlace() + ", "+_context.getString(R.string.map_name_ein_karem));
				break;
			case Rehovot:
				place.setText(placeName.getPlace() + ", "+_context.getString(R.string.map_name_rehovot));
				break;
			default:
				place.setText(placeName.getPlace());
				break;
			}
		} else {
			place.setText(placeName.getPlace());
		}
		return view;
	}

	@Override
	public Filter getFilter() {
		Filter nameFilter = new Filter() {
			@Override
			public String convertResultToString(Object resultValue) {
				return ((Place) resultValue).getPlace();
			}

			@SuppressLint("DefaultLocale")
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				if (constraint != null) {
					String constr = constraint.toString().toLowerCase();
					// replace '
					constr = constr.replace("'", "");
					// if user asks for all locations
					if (constr.equals("*")) {
						_suggestions.clear();
						_suggestions.addAll(_allPlaces);
					} else if (constr.length() < 2) {
						_suggestions.clear();
					} else {
						_suggestions.clear();
						// split constrain
						String[] subConstr = constr.split(" ");
						for (String name : subConstr) {
							if (name.length() >= 2) {
								for (Place place : _allPlaces) {
									if (!_suggestions.contains(place)) {
										for (String placeKeyword : place
												.getWords()) {
											if (name.startsWith(placeKeyword)) {
												_suggestions.add(place);
												break;
											} else if (placeKeyword
													.startsWith(name)) {
												_suggestions.add(place);
												break;
											}
										}
									}
								}
							}
							if (_suggestions.size() > 10) {
								break;
							}
						}
					}
					FilterResults filterResults = new FilterResults();
					filterResults.values = _suggestions;
					filterResults.count = _suggestions.size();
					return filterResults;
				} else {
					return new FilterResults();
				}
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				ArrayList<Place> filteredList = (ArrayList<Place>) results.values;
				ArrayList<Place> places = new ArrayList<Place>();
				if (results != null && results.count > 0) {
					clear();
					for (Place c : filteredList) {
						places.add(c);
					}
					Iterator<Place> customerIterator = places.iterator();
					while (customerIterator.hasNext()) {
						Place place = customerIterator.next();
						add(place);
					}
					notifyDataSetChanged();
				}
			}
		};
		return nameFilter;
	}
}