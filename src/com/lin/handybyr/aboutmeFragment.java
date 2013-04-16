package com.lin.handybyr;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lin.Factory.Command;
import com.lin.Factory.CommandDelegate;
import com.lin.Factory.CommandName;
import com.lin.Factory.TextProcess;
import com.lin.Model.MetaModel.ArticleModel;
import com.lin.Model.MetaModel.MailModel;
import com.lin.Model.MetaModel.MailResponse;
import com.lin.Model.MetaModel.ReferModel;
import com.lin.Model.MetaModel.ReferResponse;
import com.lin.Model.MetaModel.UserModel;
import com.lin.handybyr.util.FragmentDelegate;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class aboutmeFragment extends Fragment implements FragmentDelegate, CommandDelegate{

	private enum AboutMode {REFER, REPLY, MAIL};
	private JSONArray items;
	private int selectedIndex;
	private AboutMode mode;
	
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
					mode = AboutMode.REFER;
					break;
				}
			
				//reply me
				case 1:
				{
					tv.setText(getString(R.string.replyMe));
					Command.ReferCommand.replyList();
					mode = AboutMode.REPLY;
					break;
				}
			
				//mailbox
				case 2:
				{
					tv.setText(getString(R.string.mailbox));
					Command.MailCommand.inboxMailList();
					mode = AboutMode.MAIL;
					break;
				}
			
			}
			progressDlg.showProgressDialog(view.getContext());
		}
	};
	
	private OnItemClickListener lowerListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectedIndex = position;
			switch(mode) {
			case REFER:	
			case REPLY:
				try {
					JSONObject item = items.getJSONObject(position);
					Command.ArticleCommand.articleInfo(item.getString(ReferModel.BOARD), 
							item.getInt(ReferModel.REFER_ID));
					progressDlg.showProgressDialog(view.getContext());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				
			case MAIL:
				JSONObject json;
				try {
					json = items.getJSONObject(selectedIndex);
					Command.MailCommand.mailInfo("inbox", 
							json.getInt(MailModel.MAIL_INDEX));
					progressDlg.showProgressDialog(view.getContext());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
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
		list = (ListView)getActivity().findViewById(R.id.lvAvoutContent);
		list.setOnItemClickListener(lowerListener);
		TextView tv = (TextView)getActivity().findViewById(R.id.tvHeader);
		tv.setText(getString(R.string.nothing));
	}
	
	private void showAlertDialog(JSONObject json, final View view, String mainTitle, 
			int titleRsId, String sTitle, 
			int userRsId, String sUser, int contentRsId, String content, 
			DialogInterface.OnClickListener listener) throws JSONException {
		JSONObject user = json.getJSONObject(sUser);
		EditText et = (EditText)view.findViewById(titleRsId);
		et.setText(json.getString(sTitle));
		et = (EditText)view.findViewById(userRsId);
		et.setText(user.getString(UserModel.USER_ID));
		et = (EditText)view.findViewById(contentRsId);
		et.setText(json.getString(content));
		alertDlg.showDesignedAlertWindow(getActivity(), mainTitle, 
				view, getString(R.string.send), 
				new  DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,  int  which) {
						String title = ((EditText)view.findViewById(R.id.edMailToTitle)).
								getText().toString();
						String content = ((EditText)view.findViewById(R.id.edMailToContent)).
								getText().toString();
						String id = ((EditText)view.findViewById(R.id.edMailToUser)).
								getText().toString();
						Command.MailCommand.sendMail(id, title, content);	
						dialog.dismiss();  
					}   
				} , getString(R.string.cancel), null);
	}
	
	@Override
	public void OnCommandComplete(JSONObject json) {
		// TODO Auto-generated method stub
		progressDlg.dismissProgresDialog();
		try {
			String rawCmd = json.getString(CommandName.CommandIndicator);
			if(rawCmd == CommandName.ReferCommandName.REFERLIST || 
					rawCmd == CommandName.ReferCommandName.REPLYLIST) {
				items = json.getJSONArray(ReferResponse.ARTICLES);
				ListView list = (ListView)getActivity().findViewById(R.id.lvAvoutContent);
				ItemAdapter ia = new ItemAdapter(getActivity(), R.layout.listview_item, 
						formListFromJSONArray(items, ReferModel.TITLE, ReferModel.POSTER, 
								ReferModel.IS_READ));
				list.setAdapter(ia);
			} else if(rawCmd == CommandName.MailCommandName.INBOXMAILLIST) {
				items = json.getJSONArray(MailResponse.MAILS);
				ListView list = (ListView)getActivity().findViewById(R.id.lvAvoutContent);
				ItemAdapter ia = new ItemAdapter(getActivity(), R.layout.listview_item, 
						formListFromJSONArray(items, MailModel.MAIL_TITLE, MailModel.MAIL_SENDER, 
								MailModel.IS_READ));
				list.setAdapter(ia);
			} else if(rawCmd == CommandName.ArticleCommandName.ARTICLEINFO) {
				Command.ReferCommand.setReferReadById(items.getJSONObject(selectedIndex).
						getInt(ReferModel.REFER_INDEX));
				final JSONObject article = json;
				alertDlg.showRawAlertWindow(getActivity(), 
						json.getString(ArticleModel.TITLE), 
						json.getString(ArticleModel.CONTENT), getString(R.string.reply), 
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								Intent i = new Intent(getActivity(), com.lin.handybyr.PostActivity.class);
								i.putExtra("content", article.toString());
								getActivity().startActivity(i);
								dialog.dismiss();
							}
						}, getString(R.string.cancel), null);
						
			} else if(rawCmd == CommandName.MailCommandName.MAILINFO) {
				final JSONObject mail = json;
				alertDlg.showRawAlertWindow(getActivity(), 
						json.getString(MailModel.MAIL_TITLE), 
						json.getString(MailModel.CONTENT), getString(R.string.reply), 
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						try {	
							final JSONObject user = mail.getJSONObject(MailModel.MAIL_SENDER);
							final View view = LayoutInflater.from(getActivity()).inflate(R.layout.send_mail_view, null);
							EditText et = (EditText)view.findViewById(R.id.edMailToTitle);
							et.setText(mail.getString(MailModel.MAIL_TITLE));
							et = (EditText)view.findViewById(R.id.edMailToUser);
							et.setText(user.getString(UserModel.USER_ID));
							alertDlg.showDesignedAlertWindow(getActivity(), String.format(
									getString(R.string.mail_to), user.getString(UserModel.USER_ID)), 
									view, getString(R.string.send), 
									new  DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,  int  which) {
											String title = ((EditText)view.findViewById(R.id.edMailToTitle)).
													getText().toString();
											String content = ((EditText)view.findViewById(R.id.edMailToContent)).
													getText().toString();
											try {
												Command.MailCommand.replyMail(mail.getInt(MailModel.MAIL_INDEX), 
														title, content);
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											dialog.dismiss();  
										}   
									} , getString(R.string.cancel), null);
						} catch(Exception e) {
							
						}
					}
				}, getString(R.string.cancel), null);
			} else if(rawCmd == CommandName.MailCommandName.REPLYMAIL) {
				alertDlg.showRawPositiveAlertWindow(getActivity(), "Succeed", 
						"Send Complete!", getString(R.string.ok), null);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void OnDataArrive(JSONObject json) {
		// TODO Auto-generated method stub
		progressDlg.dismissProgresDialog();
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
	
	private List<ListViewItem> formListFromJSONArray(JSONArray array, String title, 
			String subTitle, String bool) {
		List<ListViewItem> list = new ArrayList<ListViewItem>();
		try {
			for(int i = 0; i < array.length(); i ++) {
				JSONObject json = array.getJSONObject(i);
				JSONObject user = json.getJSONObject(subTitle);
				list.add(new ListViewItem(json.getString(title), user.getString(UserModel.USER_ID), 
						json.getBoolean(bool) ? android.graphics.Color.BLACK : 
							android.graphics.Color.RED));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	//listview adapter
	private class ItemAdapter extends ArrayAdapter {

		private LayoutInflater inflater;
		private List<ListViewItem> items;
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ListViewItem lvi = items.get(position);
			convertView = inflater.inflate(R.layout.listview_item, null);
			TextView tv = (TextView)convertView.findViewById(R.id.listviewitemTitle);
			tv.setText(lvi.getTitle());
			tv.setTextColor(lvi.color);
			tv = (TextView)convertView.findViewById(R.id.listviewitemSubTitle);
			tv.setText(lvi.getSubTitle());
			return convertView;
		}
		
		public ItemAdapter(Context context, int textViewResourceId, List objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
			inflater = LayoutInflater.from(context);
			items = objects;
		}
		
	}
	
	private class ListViewItem {
		private String subTitle;
		private String title;
		private int color;
		
		public ListViewItem(String title, String subTitle, int color) {
			this.title = title;
			this.subTitle = subTitle;
			this.color = color;
		}
		
		public String getSubTitle() {
			return subTitle;
		}
		
		public String getTitle() {
			return title;
		}
		
		public int getColor() {
			return color;
		}
	}
}
