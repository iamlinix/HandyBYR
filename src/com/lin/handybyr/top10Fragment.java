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
import com.lin.Model.MetaModel;
import com.lin.Model.MetaModel.ArticleModel;
import com.lin.Model.MetaModel.ListViewItemModel;
import com.lin.handybyr.util.FragmentDelegate;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class top10Fragment extends Fragment implements FragmentDelegate, CommandDelegate{
	
	JSONArray jArray = null;
	
	private OnItemClickListener listener = new OnItemClickListener() {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			try {
				JSONObject json = jArray.getJSONObject(position);
				progressDlg.showProgressDialog(view.getContext());
				Command.ArticleCommand.threadInfo(json.getString(
						MetaModel.ArticleModel.BOARD), 
						json.getInt(MetaModel.ArticleModel.THREAD_ID));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {	
		Command.registerOnRouter(this);
		return inflater.inflate(R.layout.fragment_top10, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstance) {
		ListView list = (ListView)getActivity().findViewById(R.id.topten);
		list.setOnItemClickListener(listener);
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
	public void OnDataArrive(JSONObject json) {
		// TODO Auto-generated method stub
		try {
			ListView list = (ListView)getActivity().findViewById(R.id.topten);
			JSONArray articles = json.getJSONArray(MetaModel.ToptenResponse.ARTICLE);
			jArray = articles;
			if(list != null)
				list.setAdapter(new SimpleAdapter(getActivity(), formListItemsFromJSONArray(articles), 
					R.layout.listview_item, 
					new String[]{ListViewItemModel.SUBTITLE, ListViewItemModel.TITLE}, 
					new int[] {R.id.listviewitemSubTitle, R.id.listviewitemTitle}));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void OnCommandComplete(JSONObject json) {
		// TODO Auto-generated method stub
		progressDlg.dismissProgresDialog();
		
		Intent i = new Intent(getActivity(), com.lin.handybyr.Reading.class);
		i.putExtra("content", json.toString());
		startActivity(i);
	}

	@Override
	public void OnCommandError(String errMsg) {
		// TODO Auto-generated method stub
		progressDlg.dismissProgresDialog();
		Toast.makeText(getActivity(), errMsg, 1000).show();
	}
	
	
	
}
