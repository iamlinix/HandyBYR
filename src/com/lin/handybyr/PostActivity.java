package com.lin.handybyr;

import org.json.JSONException;
import org.json.JSONObject;

import com.lin.Factory.Command;
import com.lin.Factory.CommandDelegate;
import com.lin.Model.MetaModel.ArticleModel;
import com.lin.Model.MetaModel.UserModel;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PostActivity extends Activity implements CommandDelegate{

	private String ad = "\n---------------\n本消息由HandyBYR客户端发送\n";
	private String replyHeader = "\n\n【 在 %s 的大作中提到: 】\n: %s \n";
	
	private JSONObject article = null;
	private String board = null;
	
	private Button.OnClickListener listener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			progressDlg.showProgressDialog(v.getContext());
			
			EditText hl = (EditText)findViewById(R.id.etHeadline);
			EditText et = (EditText)findViewById(R.id.etContent);
			
			if(board == null) {
				try {
					int reid = article.getInt(ArticleModel.ARTICLE_ID);
					Command.ArticleCommand.replyArticle(
						article.getString(ArticleModel.BOARD), 
						et.getText().toString() + ad, 
						hl.getText().toString(), reid);
				} catch (JSONException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Command.ArticleCommand.postArticle(board, 
						et.getText().toString() + ad, 
						hl.getText().toString());
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		
		Command.registerOnRouter(this);
		Button btn = (Button)findViewById(R.id.btn_send);
		btn.setOnClickListener(listener);
		if(getIntent().hasExtra("board")) {
			board = getIntent().getStringExtra("board");
		} else {
			initReplyWindow();
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_post, menu);
		return true;
	}

	@Override
	public void OnCommandComplete(JSONObject json) {
		// TODO Auto-generated method stub
		progressDlg.dismissProgresDialog();
		Log.v("LIN", json.toString());
		this.finish();
	}
	
	private void initReplyWindow() {
		try {
			article = new JSONObject(getIntent().getStringExtra("content"));
			EditText et = (EditText)findViewById(R.id.etHeadline);
			et.setText(article.getString(ArticleModel.TITLE));
			String contentSnippet = article.getString(ArticleModel.CONTENT).
					substring(0, 10);
			et = (EditText)findViewById(R.id.etContent);
			et.setText(String.format(replyHeader, 
					article.getJSONObject(ArticleModel.USER).getString(UserModel.USER_ID),
					contentSnippet));
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
