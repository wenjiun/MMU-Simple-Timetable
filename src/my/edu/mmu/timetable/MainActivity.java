package my.edu.mmu.timetable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<ArrayList<Item>> {

	ViewPager pager;
	PagerTitleStrip strip;
	public static final String[] DAYS = { "Monday", "Tuesday", "Wednesday",
			"Thursday", "Friday" };
	ArrayList<Item> items = new ArrayList<Item>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pager = (ViewPager) findViewById(R.id.pager);
		strip = (PagerTitleStrip)findViewById(R.id.titlestrip);
		strip.setTextColor(Color.DKGRAY);
		getSupportLoaderManager().initLoader(0, null, this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(pager.getChildCount() > 0) {
			Calendar calendar = Calendar.getInstance();
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			if(dayOfWeek>1 && dayOfWeek<7) {
				pager.setCurrentItem(dayOfWeek - 2);
			} else {
				pager.setCurrentItem(0);
			}
		}
	}
	
	@Override
	public Loader<ArrayList<Item>> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new AssetLoader(this);
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<Item>> loader,
			final ArrayList<Item> items) {
		pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return DAYS.length;
			}

			@Override
			public Fragment getItem(int position) {
				Fragment f = MainFragment.newInstance(items, position);
				return f;
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return DAYS[position];
			}
		});

		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if(dayOfWeek>1 && dayOfWeek<7) {
			pager.setCurrentItem(dayOfWeek - 2);
		} else {
			pager.setCurrentItem(0);
		}
		
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String session = prefs.getString("session", "");
		String trimester = prefs.getString("trimester", "");
		String group = prefs.getString("group", "");
		if(!session.equals("")) {
			setTitle("Tri " + trimester + " " + session + " Group " + group);
		}
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<Item>> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_info:
			new AlertDialog.Builder(MainActivity.this)
			.setTitle("MMU Simple Timetable")
			.setMessage("Project source code on Github https://github.com/wenjiun/MMU-Simple-Timetable.\n\n" +
					"Modify timetable.json in the Assets folder to suit your needs.\n\nCreated by JYKam & WJYap")
			.setPositiveButton("OK", null)
			.setNegativeButton("Github", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Uri uri = Uri.parse("https://github.com/wenjiun/MMU-Simple-Timetable");
					Intent i = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(i);
				}
			})
		    .show();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	static class AssetLoader extends AsyncTaskLoader<ArrayList<Item>> {

		Context context;
		ArrayList<Item> items = new ArrayList<Item>();
		
		public AssetLoader(Context context) {
			super(context);
			this.context = context;
			// TODO Auto-generated constructor stub
		}

		@Override
		public ArrayList<Item> loadInBackground() {

			try {
				InputStream is = context.getAssets().open("timetable.json");
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader reader = new BufferedReader(isr);
				StringBuilder builder = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line + "\n");
				}
				JSONObject jObject = new JSONObject(builder.toString());
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
				Editor editor = prefs.edit();
				editor.putString("session", jObject.getString("session"));
				editor.putString("trimester", jObject.getString("trimester"));
				editor.putString("group", jObject.getString("group"));
				editor.commit();				
				JSONArray jDays = jObject.getJSONArray("days");
				for (int i = 0; i < jDays.length(); i++) {
					JSONObject jDay = jDays.getJSONObject(i);
					String day = jDay.getString("day");
					JSONArray jClasses = jDay.getJSONArray("class");
					for (int j = 0; j < jClasses.length(); j++) {
						JSONObject jClass = jClasses.getJSONObject(j);
						String time = jClass.getString("time");
						String subject = jClass.getString("subject");
						String venue = jClass.getString("venue");
						Item item = new Item(day, time, subject, venue);
						items.add(item);
					}

				}
				return items;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onStartLoading() {
			if (!items.isEmpty()) {
				deliverResult(items);
			}

			if (takeContentChanged() || items.isEmpty()) {
				forceLoad();
			}
		}

	}

}
