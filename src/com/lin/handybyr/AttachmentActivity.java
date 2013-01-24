package com.lin.handybyr;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class AttachmentActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attachments);
		WebView wv = (WebView)findViewById(R.id.wvAttachment);
		wv.getSettings().setBuiltInZoomControls(true);
		wv.getSettings().setSupportZoom(true);
		wv.loadUrl(getIntent().getStringExtra("url"));
		
		Button back = (Button)findViewById(R.id.back);
		back.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AttachmentActivity.this.setResult(0);
				AttachmentActivity.this.finish();
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_attachment, menu);
		return true;
	}

}
