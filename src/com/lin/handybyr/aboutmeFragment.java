package com.lin.handybyr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lin.Factory.Command;
import com.lin.Factory.CommandDelegate;
import com.lin.Factory.CommandName;
import com.lin.Model.MetaModel.MailModel;
import com.lin.Model.MetaModel.MailResponse;
import com.lin.Model.MetaModel.ReferModel;
import com.lin.Model.MetaModel.ReferResponse;
import com.lin.handybyr.util.FragmentDelegate;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class aboutmeFragment extends Fragment implements FragmentDelegate, CommandDelegate{

	private JSONArray items;
	private String itemType;
	
	private OnItemClickListener upperListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			TextView tv = (TextView)com.lin.handybyr.aboutmeFragment.this.getActivity().findViewById(R.id.tvHeader);
			switch(position) {
				//@me
				case 0:
				{
					tv.setText(getString(R.string.referMe));
					Command.ReferCommand.referList();
					break;
				}
			
				//reply me
				case 1:
				{
					tv.setText(getString(R.string.replyMe));
					Command.ReferCommand.replyList();
					break;
				}
			
				//mailbox
				case 2:
				{
					tv.setText(getString(R.string.mailbox));
					Command.MailCommand.inboxMailList();
					break;
				}
			
			}
			progressDlg.showProgressDialog(view.getContext());
		}
	};
	
	private OnItemClickListener lowerListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			try {
				JSONObject item = items.getJSONObject(position);
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
		
		return inflater.inflate(R.layout.fragment_aboutme, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstance) {
		ListView list = (ListView)getActivity().findViewById(R.id.lvAboutType);
		list.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
				new String[] {getString(R.string.referMe), getString(R.string.replyMe), 
			getString(R.string.mailbox)}));
		list.setOnItemClickListener(upperListener);
		TextView tv = (TextView)getActivity().findViewById(R.id.tvHeader);
		tv.setText(getString(R.string.nothing));
	}
	
	@Override
	public void OnCommandComplete(JSONObject json) {
		// TODO Auto-generated method stub
		progressDlg.dismissProgresDialog();
		try {
			String rawCmd = json.getString(CommandName.CommandIndicator);
			itemType = rawCmd;
			if(rawCmd == CommandName.ReferCommandName.REFERLIST || 
					rawCmd == CommandName.ReferCommandName.REPLYLIST) {
				items = json.getJSONArray(ReferResponse.ARTICLES);
				ListView list = (ListView)getActivity().findViewById(R.id.lvAvoutContent);
				list.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
						assembleDataWithJArray(items, ReferModel.TITLE)));
			} else if(rawCmd == CommandName.MailCommandName.INBOXMAILLIST) {
				items = json.getJSONArray(MailResponse.MAILS);
				ListView list = (ListView)getActivity().findViewById(R.id.lvAvoutContent);
				list.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
						assembleDataWithJArray(items, MailModel.MAIL_TITLE)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void OnDataArrive(JSONObject json) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnCommandError(String errMsg) {
		// TODO Auto-generated method stub
		progressDlg.dismissProgresDialog();
		Toast.makeText(getActivity(), errMsg, Toast.LENGTH_SHORT).show();
	}

	private String[] assembleDataWithJArray(JSONArray array, String key) {
		String[] data = new String[array.length()];
		for(int i = 0; i < array.length(); i ++) {
			try {
				JSONObject json = array.getJSONObject(i);
				data[i] = json.getString(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return data;
	}
}
