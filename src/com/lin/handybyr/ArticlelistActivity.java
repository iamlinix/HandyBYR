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
import com.lin.Model.MetaModel.PaginationModel;
import com.lin.Model.MetaModel.UserModel;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class ArticlelistActivity extends Activity implements CommandDelegate{

	private JSONArray articles;
	private JSONObject page_info;
	private String board;
	
	private OnItemClickListener listener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			try {
				JSONObject json = articles.getJSONObject(position); 

				progressDlg.showProgressDialog(view.getContext());
				
				Command.ArticleCommand.threadInfo(
						json.getString(ArticleModel.BOARD), 
						json.getInt(ArticleModel.THREAD_ID));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	private Button.OnClickListener buttonListener = new Button.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				int curPage = page_info.getInt(MetaModel.PaginationModel.CURRENT_INDEX);
				progressDlg.showProgressDialog(v.getContext());
				
				switch(v.getId()) {
				case R.id.btn_prev:
				{
					if(curPage > 1) {
						Command.BoardCommand.articleListAtPage(board, 
								page_info.getInt(PaginationModel.ITEMS_PER_PAGE), 
								page_info.getInt(PaginationModel.CURRENT_INDEX) - 1);
					}
					break;
				}
				
				case R.id.btn_next:
				{
					Command.BoardCommand.articleListAtPage(board, 
							page_info.getInt(PaginationModel.ITEMS_PER_PAGE), 
							page_info.getInt(PaginationModel.CURRENT_INDEX) + 1);
					break;
				}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	private Button.OnClickListener listenerNewPost = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(
					com.lin.handybyr.ArticlelistActivity.this, 
					com.lin.handybyr.PostActivity.class);
			i.putExtra("board", board);
			com.lin.handybyr.ArticlelistActivity.this.startActivity(i);
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_articlelisting);
		
		Command.registerOnRouter(this);
		
		ListView list = (ListView)findViewById(R.id.articlelist);
		list.setOnItemClickListener(listener);
		
		Button btn = (Button)findViewById(R.id.btn_prev);
		btn.setOnClickListener(buttonListener);
		btn = (Button)findViewById(R.id.btn_next);
		btn.setOnClickListener(buttonListener);
		btn = (Button)findViewById(R.id.btnNewPost);
		btn.setOnClickListener(listenerNewPost);
		
		//init the view
		try {
			JSONObject json = new JSONObject(getIntent().getStringExtra("content"));
			articles = json.getJSONArray(MetaModel.BoardResponse.ARTICLES);
			board = articles.getJSONObject(0).getString(MetaModel.ArticleModel.BOARD);
			page_info = json.getJSONObject(MetaModel.BoardResponse.PAGINATION);
			setTitle(board);
			updateList();
			updatePageNumber();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_articlelist, menu);
		return true;
	}
	
	public List<Map<String, Object>> formListItemsFromJSONArray(JSONArray array) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < array.length(); i ++) {
			JSONObject item;
			try {
				item = array.getJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(ListViewItemModel.SUBTITLE, 
						item.getJSONObject(ArticleModel.USER).getString(UserModel.USER_ID));
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
	public void OnCommandComplete(JSONObject json) {
		// TODO Auto-generated method stub
		String rawCmd;
		progressDlg.dismissProgresDialog();
		
		try {
			rawCmd = json.getString(CommandName.CommandIndicator);
			if(rawCmd == CommandName.BoardCommandName.BOARD) { 
				page_info = json.getJSONObject(MetaModel.BoardResponse.PAGINATION);
				articles = json.getJSONArray(MetaModel.BoardResponse.ARTICLES);
				updateList();
				updatePageNumber();
				
			} else if(rawCmd == CommandName.ArticleCommandName.THREADINFO) {
				Intent i = new Intent(this, com.lin.handybyr.Reading.class);
				i.putExtra("content", json.toString());
				startActivity(i);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void updateList() {
		ListView list = (ListView)findViewById(R.id.articlelist);
		list.setAdapter(new SimpleAdapter(this, 
				formListItemsFromJSONArray(articles), 
				R.layout.listview_item, 
				new String[]{ListViewItemModel.SUBTITLE, ListViewItemModel.TITLE}, 
				new int[] {R.id.listviewitemSubTitle, R.id.listviewitemTitle}));
	}
	
	private void updatePageNumber() {
		TextView tv = (TextView)findViewById(R.id.pageNum);
		try {
			tv.setText(String.format(getString(R.string.page_format), 
					page_info.getInt(PaginationModel.CURRENT_INDEX)));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void OnCommandError(String errMsg) {
		// TODO Auto-generated method stub
		progressDlg.dismissProgresDialog();
		Toast.makeText(this, errMsg, 1000).show();
	}

}
