package com.lin.handybyr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lin.Controller.ExpandableListViewController;
import com.lin.Factory.Command;
import com.lin.Factory.CommandDelegate;
import com.lin.Factory.CommandName;
import com.lin.Model.MetaModel;
import com.lin.Model.MetaModel.ArticleModel;
import com.lin.Model.MetaModel.ListViewItemModel;
import com.lin.Model.MetaModel.SectionModel;
import com.lin.handybyr.util.FragmentDelegate;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class sectionsFragment extends Fragment implements FragmentDelegate, CommandDelegate{
	
	private List<ExpandableListViewController> root
	= new ArrayList<ExpandableListViewController>();
	
	private List<ExpandableListViewController> nodes
	= new ArrayList<ExpandableListViewController>();
	
	private TreeViewAdapter treeViewAdapter;
	
	private int clickedLevel;
	private String clickedListViewItemId;
	private int clickedListViewItemIndex;

	private OnItemClickListener listener = new OnItemClickListener() {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ExpandableListViewController row = root.get(position);
			clickedLevel = row.getLevel();
			clickedListViewItemId = row.getId();
			clickedListViewItemIndex = position;
			
			progressDlg.showProgressDialog(getActivity());

			if(row.hasChild()) {
				if(!row.getExpanded() && !row.hasChildrenDownloaded()) 
					Command.SectionCommand.sectionList(row.getId());
				else 
					UpdateListData();
			} else {
				
				Command.BoardCommand.articleList(row.getId());
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		treeViewAdapter = new TreeViewAdapter(getActivity(), R.layout.expandable_list_view_item, root);
		Command.registerOnRouter(this);
		return inflater.inflate(R.layout.fragment_sections, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstance) {
		ListView list = (ListView)getActivity().findViewById(R.id.sections);
	//	list.setAdapter(treeViewAdapter);
		list.setOnItemClickListener(listener);
	}
	
	private void UpdateListData() {
		progressDlg.dismissProgresDialog();
		ExpandableListViewController row = root.get(clickedListViewItemIndex);
		if(!row.hasChild()) {
			return;
		} else {
			if(row.getExpanded()) {
				row.setExpanded(false);
				List<ExpandableListViewController> temp = 
						new ArrayList<ExpandableListViewController>();
				for(int i = clickedListViewItemIndex + 1; 
						i < root.size(); i ++) {
					if(row.getLevel() >= root.get(i).getLevel()) {
						break; 
					}
					temp.add(root.get(i));	
				}
				root.removeAll(temp);
				treeViewAdapter.notifyDataSetChanged();
			} else {
				row.setExpanded(true);
				int curLevel = row.getLevel();
				int nextLevel = curLevel + 1;
				
				for(ExpandableListViewController item : nodes) {
					int pivot = 1;
					if(item.getParent() == row.getId())	{
						item.setLevel(nextLevel);
						item.setExpanded(false);
						root.add(pivot + clickedListViewItemIndex, item);
						++pivot;
					}
				}
				treeViewAdapter.notifyDataSetChanged();
			}
		}
	}
	
	private Boolean isItemExists(String itemId) {
		for(ExpandableListViewController row : nodes) {
			if(row.getId().compareTo(itemId) == 0)
				return true;
		}
		return false;
	}
	
	@Override
	public void OnCommandComplete(JSONObject json) {
		// TODO Auto-generated method stub
		progressDlg.dismissProgresDialog();
		try {
			String rawCmd = json.getString(CommandName.CommandIndicator);
			if(rawCmd == CommandName.SectionCommandName.SECTIONLIST) {
				//sub sections
				if(json.has(MetaModel.SectionResponse.SUB_SECTIONS)) {
					JSONArray sections = 
							json.getJSONArray(MetaModel.SectionResponse.SUB_SECTIONS);
					for(int i = 0; i < sections.length(); i ++) {
						String section = sections.getString(i);
						ExpandableListViewController temp = 
								ExpandableListViewController.Builder().setLevel(clickedLevel + 1).
								setHasChild(true).setExpanded(false).setId(section).
								setChildrenDownloaded(false).setTitle(section).
								setSubtitle("").
								setParent(clickedListViewItemId);
						if(!isItemExists(section))
							nodes.add(temp);
					}
				}
				
				//boards
				if(json.has(MetaModel.SectionResponse.BOARDS)) {
					JSONArray boards = 
							json.getJSONArray(MetaModel.SectionResponse.BOARDS);
					for(int i = 0; i < boards.length(); i ++) {
						JSONObject board = boards.getJSONObject(i);
						ExpandableListViewController temp = 
								ExpandableListViewController.Builder().setLevel(clickedLevel + 1).
								setHasChild(false).setExpanded(false).setChildrenDownloaded(false).
								setId(board.getString(MetaModel.BoardModel.BOARD_NAME)).
								setTitle(board.getString(MetaModel.BoardModel.BOARD_DESC)).
								setSubtitle(board.getString(MetaModel.BoardModel.BOARD_NAME)).
								setParent(clickedListViewItemId);
						if(!isItemExists(board.getString(MetaModel.BoardModel.BOARD_NAME)))
							nodes.add(temp);
					}
				}
				
				root.get(clickedListViewItemIndex).setChildrenDownloaded(true);
			} else if(rawCmd == CommandName.BoardCommandName.BOARD) {

				Intent i = new Intent(getActivity(), 
						com.lin.handybyr.ArticlelistActivity.class);
				i.putExtra("content", json.toString());
				getActivity().startActivity(i);
				return;
			} 
			
			UpdateListData();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void OnDataArrive(JSONObject json) {
		// TODO Auto-generated method stub
		try {
			ListView list = (ListView)getActivity().findViewById(R.id.sections);
			JSONArray sections = json.getJSONArray(MetaModel.SectionResponse.SECTIONS);
			for(int i = 0; i < sections.length(); i ++) {
				JSONObject row = sections.getJSONObject(i);
				ExpandableListViewController temp = 
						ExpandableListViewController.Builder().setLevel(0).
						setHasChild(true).setExpanded(false).setId(row.getString(
								MetaModel.SectionModel.SECTION_NAME)).
						setTitle(row.getString(
								MetaModel.SectionModel.SECTION_DESC)).
						setSubtitle(row.getString(
								MetaModel.SectionModel.SECTION_NAME)).
						setChildrenDownloaded(false);
				if(!isItemExists(row.getString(MetaModel.SectionModel.SECTION_NAME))) {
					root.add(temp);
					nodes.add(temp);
				}
			}
		//	ListView list = (ListView)getActivity().findViewById(R.id.sections);
			if(list != null)
				list.setAdapter(treeViewAdapter);
		/*	list.setAdapter(new SimpleAdapter(getActivity(), formListItemsFromJSONArray(sections,
					MetaModel.SectionModel.SECTION_DESC, MetaModel.SectionModel.SECTION_NAME), 
					R.layout.listview_item, 
					new String[]{ListViewItemModel.SUBTITLE, ListViewItemModel.TITLE}, 
					new int[] {R.id.listviewitemSubTitle, R.id.listviewitemTitle})); */
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Map<String, Object>> formListItemsFromJSONArray(JSONArray array, 
			String titleIndicator, String subtitleIndicator) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < array.length(); i ++) {
			JSONObject item;
			try {
				item = array.getJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(ListViewItemModel.SUBTITLE, item.getString(subtitleIndicator));
				map.put(ListViewItemModel.TITLE, item.getString(titleIndicator));
				list.add(map);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	
	private class TreeViewAdapter extends ArrayAdapter {

		private class ViewHolder {
			public TextView title;
			public TextView subtitle;
			public ImageView icon;
		}
		
		private LayoutInflater inflater;
		private List<ExpandableListViewController> items;
		private Bitmap fold;
		private Bitmap expand;
		private Bitmap leaf;
		
		
		public TreeViewAdapter(Context context, int textViewResourceId, List objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
			inflater = LayoutInflater.from(context);
			items = objects;
			fold = BitmapFactory.decodeResource(context.getResources(), R.drawable.fold);
			expand = BitmapFactory.decodeResource(context.getResources(), R.drawable.expand);
			leaf = BitmapFactory.decodeResource(context.getResources(), R.drawable.buttom);
		}
		
		public int getCount() {
			return items.size();
		}
		
		public Object getItem(int position) {
			return items.get(position);
		}
		
		public long getItemId(int position) {
			return position;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.expandable_list_view_item, 
					null);
			holder.title = (TextView)convertView.findViewById(
					R.id.expListViewItemTitle); 
			holder.subtitle = (TextView)convertView.findViewById(
					R.id.expListViewItemSubtitle);
			holder.icon = (ImageView)convertView.findViewById(R.id.imgHeader);
			convertView.setTag(holder);
			
			holder.title.setText(items.get(position).getTitle());
			holder.subtitle.setText(items.get(position).getSubtitle());
			holder.icon.setPadding(25 * (items.get(position).getLevel()), holder.icon
					.getPaddingTop(), 0, holder.icon.getPaddingBottom());
			if(items.get(position).hasChild()) {
				if(items.get(position).getExpanded()) {
					holder.icon.setImageBitmap(expand);
				} else {
					holder.icon.setImageBitmap(fold);
				}
			} else {
				holder.icon.setImageBitmap(leaf);
			}
			return convertView;
		}
	}

	@Override
	public void OnCommandError(String errMsg) {
		// TODO Auto-generated method stub
		progressDlg.dismissProgresDialog();
		Toast.makeText(getActivity(), errMsg, 1000).show();
	}
	
}
