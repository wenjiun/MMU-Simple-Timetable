package my.edu.mmu.timetable;

import java.util.ArrayList;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainFragment extends ListFragment {

	ArrayList<Item> allItems = new ArrayList<Item>();
	ArrayList<Item> items = new ArrayList<Item>();
	int position;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		position = args.getInt("position");
		allItems = args.getParcelableArrayList("items");		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		items.clear();
		for(Item item:allItems) {
			if(item.getDay().equals(MainActivity.DAYS[position])) {
				items.add(item);
			}
		}
		setListAdapter(new CustomAdapter());
	}


	public static Fragment newInstance(ArrayList<Item> items, int position) {
		Fragment f = new MainFragment();		
		Bundle args = new Bundle();
		args.putParcelableArrayList("items", items);
		args.putInt("position", position);
		f.setArguments(args);
		return f;
	}
	
	
	class CustomAdapter extends ArrayAdapter<Item> {

		public CustomAdapter() {
			super(getActivity(), android.R.layout.simple_list_item_1, items);			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if(view==null) {
				view = getActivity().getLayoutInflater().inflate(R.layout.row, null, false);
			}
			TextView textViewTime = (TextView)view.findViewById(R.id.textViewTime);
			TextView textViewSubject = (TextView)view.findViewById(R.id.textViewSubject);
			TextView textViewVenue = (TextView)view.findViewById(R.id.textViewVenue);
			textViewTime.setText(items.get(position).getTime());
			textViewSubject.setText(items.get(position).getSubject());
			textViewVenue.setText(items.get(position).getVenue());
			Typeface roboto_bold = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-BoldCondensed.ttf");
			textViewTime.setTypeface(roboto_bold);
			Typeface roboto = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Condensed.ttf");
			textViewSubject.setTypeface(roboto);
			textViewVenue.setTypeface(roboto);
			return view;
		}
		
	}
	
}
