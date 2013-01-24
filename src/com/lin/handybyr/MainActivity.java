package com.lin.handybyr;

import org.json.JSONException;
import org.json.JSONObject;

import com.lin.Factory.Command;
import com.lin.Factory.CommandDelegate;
import com.lin.Model.MetaModel.ErrorModel;
import com.lin.database.DatabaseModel;
import com.lin.database.DatabaseOperator;
import com.lin.database.SQLiteHelper;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, CommandDelegate{

	private SQLiteHelper helper;
	private Boolean rememberMe = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//set listener
		Button login = (Button)findViewById(R.id.button1);
		login.setOnClickListener(this);
		
		CheckBox guest = (CheckBox)findViewById(R.id.guest);
		guest.setOnClickListener(this);
		
		//set up the callback
		Command.registerOnRouter(this);
		
		helper = new SQLiteHelper(this, DatabaseModel.DB_NAME, null, 
				DatabaseModel.version);
		
		Cursor csr = DatabaseOperator.selectAndInsertIfNotExist(helper, 
				DatabaseModel.DataDictionary.USER_NAME);
		if(csr.moveToNext()) {
			TextView tv = (TextView)findViewById(R.id.user);
			tv.setText(csr.getString(0));
		}
		
		csr =  DatabaseOperator.selectAndInsertIfNotExist(helper, 
				DatabaseModel.DataDictionary.PASSWORD);
		if(csr.moveToNext()) {
			TextView tv = (TextView)findViewById(R.id.pass);
			tv.setText(csr.getString(0));
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
			case(R.id.button1):
			{
				TextView user = (TextView)findViewById(R.id.user);
				TextView pass = (TextView)findViewById(R.id.pass);
				
				progressDlg.showProgressDialog(this);
				
				DatabaseOperator.updateAndInsertIfNotExist(helper, 
						DatabaseModel.DataDictionary.REMEMBER_ME, rememberMe.toString());
				if(rememberMe) {
					DatabaseOperator.updateAndInsertIfNotExist(helper, 
							DatabaseModel.DataDictionary.USER_NAME, user.getText().toString());
					DatabaseOperator.updateAndInsertIfNotExist(helper, 
							DatabaseModel.DataDictionary.PASSWORD, pass.getText().toString());
				}
				
				Command.UserCommand.LogIn(user.getText().toString(), pass.getText().toString());

				break;
			}
			
			case(R.id.guest):
			{
				CheckBox guest = (CheckBox)findViewById(R.id.guest);
				TextView user = (TextView)findViewById(R.id.user);
				TextView pass = (TextView)findViewById(R.id.pass);
				if(guest.isChecked()) {
					user.setText("guest");
					pass.setText("");
				}
				else {
					user.setText("");
					pass.setText("");
				}
				break;
			}
			
			case(R.id.rememberme):
			{
				CheckBox remember = (CheckBox)findViewById(R.id.rememberme);
				rememberMe = remember.isChecked();
				break;
			}
		}
	}
  
	@Override
	public void OnCommandComplete(JSONObject json) {
		// TODO Auto-generated method stub

		progressDlg.dismissProgresDialog();
		
		try {
			if(json.has(ErrorModel.ERROR_MSG)) {
				this.startActivity(this.getIntent());
				Toast.makeText(this, json.getString(ErrorModel.ERROR_MSG), Toast.LENGTH_SHORT).show();
			} else {
				Intent i = new Intent(this, com.lin.handybyr.HomepageActivity.class);
				this.startActivity(i);
			}
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
