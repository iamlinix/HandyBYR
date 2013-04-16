package com.lin.handybyr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lin.Factory.Command;
import com.lin.Factory.CommandDelegate;
import com.lin.Factory.CommandName;
import com.lin.Model.MetaModel;
import com.lin.Model.MetaModel.ArticleModel;
import com.lin.Model.MetaModel.ListViewItemModel;
import com.lin.Model.MetaModel.RecommendResponse;
import com.lin.Model.MetaModel.TabTag;
import com.lin.Model.MetaModel.ToptenResponse;
import com.lin.handybyr.util.FragmentDelegate;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class HomepageActivity extends FragmentActivity implements CommandDelegate{

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current tab position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private static Map<Long, FragmentDelegate> tabs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Command.registerOnRouter(this);
		tabs = new HashMap<Long, FragmentDelegate>();
		setContentView(R.layout.activity_homepage);

		// Set up the action bar to show tabs.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// Show the Up button in the action bar.
		actionBar.setDisplayHomeAsUpEnabled(true);

		Bundle bundle = new Bundle();
		// For each of the sections in the app, add a tab to the action bar.
		bundle.putLong("id", R.id.topten);
		actionBar.addTab(actionBar.newTab().setText(R.string.top10).setTag(
				TabTag.TOPTEN_TAG)
				.setTabListener(new TabListener<top10Fragment>(this, 
						TabTag.TOPTEN_TAG, top10Fragment.class, bundle)));
		
		bundle.putLong("id", R.id.recommends);
		actionBar.addTab(actionBar.newTab().setText(R.string.recommend).setTag(
				TabTag.RECOMMEND_TAG)
				.setTabListener(new TabListener<recommendsFragment>(this, 
						TabTag.RECOMMEND_TAG, recommendsFragment.class, bundle)));
		
		bundle.putLong("id", R.id.sections);
		actionBar.addTab(actionBar.newTab().setText(R.string.sections).setTag(
				TabTag.SECTION_TAG)
				.setTabListener(new TabListener<sectionsFragment>(this, 
						TabTag.SECTION_TAG, sectionsFragment.class, bundle)));
		
		bundle.putLong("id", R.id.lvAboutType);
		actionBar.addTab(actionBar.newTab().setText(R.string.aboutMe).setTag(
				TabTag.ABOUTME_TAG)
				.setTabListener(new TabListener<aboutmeFragment>(this, 
						TabTag.ABOUTME_TAG, aboutmeFragment.class, bundle)));
		
		actionBar.addTab(actionBar.newTab().setText(R.string.more).setTag(
				TabTag.MORE_TAG)
				.setTabListener(new TabListener<recommendsFragment>(this, 
						TabTag.MORE_TAG, recommendsFragment.class, bundle)));
		
		
		
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current tab position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current tab position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_homepage, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static class TabListener<T extends Fragment> implements ActionBar.TabListener {

		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;
		private final Bundle mArgs;
		private android.app.Fragment mFragment;
		
		TabListener(Activity activity, String tag, Class<T> cls) {
			this(activity, tag, cls, null);
		}
		
		TabListener(Activity activity, String tag, Class<T> cls, Bundle args) {
			mActivity = activity;
			mTag = tag;
			mClass = cls;
			mArgs = (Bundle) args.clone();
			
			mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
			if(mFragment != null && !mFragment.isDetached()) {
				tabs.put(mArgs.getLong("id"), (FragmentDelegate)mFragment);
				FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
				ft.detach(mFragment);
				ft.commit();
			}
		}
		
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			Toast.makeText(mActivity, "Reselected", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			if(mFragment == null) {
				mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
				tabs.put(mArgs.getLong("id"), (FragmentDelegate)mFragment);
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				ft.attach(mFragment);
			}	
			progressDlg.showProgressDialog(mActivity);
			if(tab.getTag() == TabTag.TOPTEN_TAG) {
				Command.WidgetCommand.toptenList();
			} else if(tab.getTag() == TabTag.SECTION_TAG) {
				Command.SectionCommand.rootSections(); 
			} else if(tab.getTag() == TabTag.RECOMMEND_TAG) {
				Command.WidgetCommand.recommendList();
			} else if(tab.getTag() == TabTag.ABOUTME_TAG) {
				Command.ReferCommand.referInfo();
			}
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			if(mFragment != null) {
				ft.detach(mFragment);
			}
		}
	
	}

	@Override
	public void OnCommandComplete(JSONObject json) {
		// TODO Auto-generated method stub
		
		
		try {
			String rawCmd = json.getString(CommandName.CommandIndicator);
			if(rawCmd == CommandName.WidgetCommand.TOPTENLIST) {
				fillList(json, R.id.topten);
			} else if(rawCmd == CommandName.WidgetCommand.RECOMMENDDLIST) {
				fillList(json, R.id.recommends);
			} else if(rawCmd == CommandName.SectionCommandName.ROOTSECTIONS) {
				fillList(json, R.id.sections);
			} else if(rawCmd == CommandName.ReferCommandName.REFERINFO) {
				fillList(json, R.id.lvAboutType);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}
		 
	}
	
	//for legacy reasons
	public void fillList(JSONObject json, long viewId) {
		FragmentDelegate fd = tabs.get(viewId);
		fd.OnDataArrive(json);
	}
	
	public List<Map<String, Object>> formListItemsFromJSONArray(JSONArray array) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < array.length(); i ++) {
			JSONObject item;
			try {
				item = array.getJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(ListViewItemModel.SUBTITLE, item.getString(ArticleModel.BOARD));
				map.put(ListViewItemModel.TITLE, item.getString(ArticleModel.TITLE));
				list.add(map);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public void OnCommandError(String errMsg) {
		// TODO Auto-generated method stub
		Toast.makeText(this, errMsg, 1000).show();
	}
}
