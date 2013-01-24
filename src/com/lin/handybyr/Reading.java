package com.lin.handybyr;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lin.Factory.Command;
import com.lin.Factory.CommandDelegate;
import com.lin.Factory.CommandName;
import com.lin.Model.MetaModel;
import com.lin.Model.MetaModel.ArticleModel;
import com.lin.Model.MetaModel.ArticleResponse;
import com.lin.Model.MetaModel.FileModel;
import com.lin.Model.MetaModel.PaginationModel;
import com.lin.Model.MetaModel.UserModel;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Picture;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebView.PictureListener;
import android.webkit.WebViewClient;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class Reading extends Activity implements CommandDelegate{

	private String htmlPattern = "<!DOCTYPE html>" +
			"<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">" +
			"<head><title></title><style type=\"text/css\">" +
			"		html, body {" +
			"			font-size: 12px;" +
			"			cursor: default;" +
			"			padding: 5px;" +
			"			margin: 0;" +
			"		}" +
			        
			"        div.blank {" +
			"            padding: 10px 0px 15px 0px;" +
			"        }" +
					
			"        a.whitespace {" +
			"            padding: 0px 5px 0px 5px;" +
			            
			"        }" +
			            
			"        div.whitespace {" +
			"             padding: 0px 10px 0px 10px;" +
			"        }" +
			"		div.pagebut {" +
			"			padding: 10px 10px 10px 10px;" +
			"			text-align:center;" +
			"		}" +
			"       div.status {" +
			"			padding-left: 60px;" +
			"           padding-bottom: 5px;" +
			"           padding-top:5px;" +
			"			border-bottom-style:solid;" +
            "			border-bottom-width:1px;" +
            "			border-bottom-color:silver;" +
		    "			position: relative;" +
			"			margin-bottom: 10px;" +
			"		}" +

			"			div.status p{" +
			"				margin: 0 0 5px 0;" +
			"				line-height: 1.5;" +
			"				padding: 0;" +
			"			}" +

			"				div.status span.name {" +
			"					color: #369;" +
			"				}" +
			"				span.name {" +
			"					font-size:12px;" +
			"				}" +
			"				div.status p.status-content {" +
			"					color: #333;" +
			"					font-size: 20px;" +			
			"				}" +
			"				pre.content {" +
			"					color: #333;" +
			"					font-size: 20px;" +
			"					white-space: pre-wrap;" +
			"				}" +
			"				pre.repost {" +
			"					color: green;" +
			"					font-size: 15px;" +
			"					white-space: pre-wrap;" +
			"					boarder-style:solid;" +
			"					boarder-width:1px;" +
			"					boarder-color:silver;" +
			"				}" +

			"			div.status .face {" +
			"				position: absolute;" +
			"				left: 0;" +
			"				top: 0;" +
			"			}" +

			" 			img.face {" +
			"				position: absolute;" +
			"				left: 0;" +
			"				top; 0;" +
			"				width: 60px;" +
			"				height: 60px;" +
			"			}" +
			"			p.id {" +
			"				position: absolute;" +
			"				left: 20px;" +
			"				top: 65px" +
			"			}" +
			"			div.status div.repost {" +
			"				border: solid 1px #ACD;" +
			"				background: #F0FAFF;" +
			"				padding: 10px;" +
			"			}" +
			"			img.imgbox {" +
			"				width: 100;" +
			"				height:100px;" +
			"			}" +
			"		div.repost p.repost-cotent {" +
			"			color: #666 !important;" +
			"		}" +
			"		p.timestamp {" +
			"			color:silver;" +
			"			font-size:12px;" +
			"		}" +
			"		div.box {" +
			"	   		height: 30px;" +
			"		}" +
			"		div.b1 {" +
			"	  		line-height:30px; overflow:hidden; text-align:center; width: 80px; height: 30px; background-color:beige; float: left;" +
			"		}" +
			"		div.b2 {" +
			"	   		line-height:30px; overflow:hidden; text-align:center; width: 80px; height: 30px; background-color:beige; float: left; clear: none; margin-left: 5px; margin-right: 5px;" +
			"		}" +
			"		div.b3 {" +
			"	   		width: 120px; height: 100px; border: 4px double #0000FF; float: left; clear: right;" +
			"		}" +

			"	</style>" +
			"</head>" +
			"<body>" +
			
			"%s" +

			"</body>" +
			"</html>";
	private String articlePattern = "<div class=\"status\">" +
			"<img src=\"%s\" class=\"face\" /><p class=id>[%s]</p>" +
			"<span class=\"name\">" +
			"[%d楼] %s (%s)</span><br /><pre class=\"content\">%s</pre>" +
			"%s" + "</br><p class=\"timestamp\">发布于 %s</p>" +
	        "<div class=\"blank\">" +
	        "<div class=box><a class=\"whitespace\" href=\"reply?%d\"><div class=b1>回复</div></a>" +
	        "<a class=\"whitespace\" href=\"forward?%d\"><div class=b2>转发</div></a></div>" +
	        "</div></div><div class=\"whitespace\"></div>";
	private String imagePattern = "<div><a href=\"%s\"><img src=\"%s\" class=\"imgbox\" /></a></div>"; 
	private String pageButtom = "<a href='next_page'><div class=pagebut>More...</div></a>";
	private JSONArray articles = new JSONArray();
	private JSONObject pagination;
	private String board = "";
	private int threadId = 0;
	private String currentHtml = "";
	private int currentFloor = 0;
	private int scrollX = 0, scrollY = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reading);
		Command.registerOnRouter(this);
		String content = getIntent().getStringExtra("content");
		try {
			JSONObject json = new JSONObject(content);
			setTitle(json.getString(MetaModel.ArticleModel.TITLE));
			updatePageThreadInfo(json);
			WebView wv = (WebView)findViewById(R.id.readingPane);
			wv.getSettings().setDefaultTextEncodingName("utf-8");
	//		wv.getSettings().setJavaScriptEnabled(true);
			wv.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					return (urlPassthrough(url));
				}
				
				@Override
			    public void onPageFinished(WebView view, String url) {
					// TODO Auto-generated method stub
			        super.onPageFinished(view, url);
			        view.scrollTo(scrollX, scrollY);
			    }
			});

			content = generateHtmlPage(json);
			wv.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_reading, menu);
		return true;
	}
	
	private int extractAttIdFromUrl(String attUrl) {
		int index = attUrl.lastIndexOf("/");
		String sId = attUrl.substring(index + 1);
		return Integer.valueOf(sId);
	}
	
	private String highlightRepost(String content) {
		String tag = "<pre class=\"repost\">%s</pre>";
		String newContent = content, toReplace = "";
		String[] splited = content.split("\n");
		for(int i = 0; i < splited.length; i ++) {
			String s = splited[i];
			if(s.startsWith(":")) {
				toReplace += s + (i == splited.length ? "" : "\n");
			}
		}
		return toReplace.length() > 0 ? 
				newContent.replace(toReplace, String.format(tag, toReplace)) : newContent;
	}
	
	private String unixtime2String(long time) {
		String sTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
				new Date(time * 1000));
		return sTime;
	}
	
	private String generateHtmlPage(JSONObject json) {
		String html = "";
		int existingPostCount = articles.length();
		try {
			JSONArray posts = json.getJSONArray(ArticleResponse.ARTICLES);

			for(int i = 0; i < posts.length(); i ++) {
				JSONObject article = posts.getJSONObject(i);
				this.articles.put(article);
				JSONObject user = article.getJSONObject(MetaModel.ArticleModel.USER);
				String imagePane = "";
				String board = article.getString(ArticleModel.BOARD);
				int articleId = article.getInt(ArticleModel.ARTICLE_ID);
				if(article.getBoolean(MetaModel.ArticleModel.HAS_ATTACHMENT)) {
					JSONObject attachment = article.getJSONObject(
							MetaModel.ArticleModel.ATTACHMENTS);
					JSONArray attachments = attachment.getJSONArray(
							MetaModel.AttachmentModel.FILES);

					for(int j = 0; j < attachments.length(); j ++) {
						JSONObject img = attachments.getJSONObject(j);
						String imgWebUrl = Command.BuildAttImageUrl(
								board, articleId, extractAttIdFromUrl(
										img.getString(FileModel.FILE_URL)));
						imagePane += String.format(imagePattern, imgWebUrl, imgWebUrl);
								
					}
				}
				html += String.format(articlePattern, 
						user.getString(MetaModel.UserModel.FACE_URL), 
						user.getString(UserModel.USER_GENDER).compareToIgnoreCase("m") == 0 ? "GG" : "MM",
						i + currentFloor, user.getString(MetaModel.UserModel.USER_ID),
						user.getString(MetaModel.UserModel.USER_NAME),
						highlightRepost(article.getString(MetaModel.ArticleModel.CONTENT)),
						imagePane, unixtime2String(article.getLong(ArticleModel.POST_TIME)),
						i + existingPostCount, i + existingPostCount);
			}
			currentFloor += posts.length();
			html = currentHtml + html;
			currentHtml = html;
			html += pageButtom;
			html = String.format(htmlPattern, html);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return html;
	}
	
	@SuppressLint("ShowToast")
	private boolean urlPassthrough(String url) {
		//my commands
		try {
			if(url.compareTo(CommandName.RequestOnHtml.NEXT_PAGE) == 0) {
				
				if(pagination.getInt(PaginationModel.CURRENT_INDEX) < 
						pagination.getInt(PaginationModel.TOTAL_PAGE_COUNT)) {
					progressDlg.showProgressDialog(this);
					Command.ArticleCommand.threadInfoAtPage(board, threadId, 
						pagination.getInt(MetaModel.PaginationModel.ITEMS_PER_PAGE), 
						pagination.getInt(MetaModel.PaginationModel.CURRENT_INDEX) + 1);
				} else {
					Toast.makeText(this, getString(R.string.hit_last_page), 1000).show();
				}
			} else if(url.startsWith(CommandName.RequestOnHtml.REPLY)) {
				Intent i = new Intent(this, com.lin.handybyr.PostActivity.class);
				int articleIndex = Integer.valueOf(url.substring(url.indexOf("?") + 1));
				i.putExtra("content", articles.getJSONObject(articleIndex).toString());
				this.startActivity(i);
			} else if(url.startsWith("http")) { 
				//real urls, just go
				Intent i = new Intent(this, com.lin.handybyr.AttachmentActivity.class);
				i.putExtra("url", url);
				this.startActivityForResult(i, 0);
				//	this.startActivity(i);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void OnCommandComplete(JSONObject json) {
		// TODO Auto-generated method stub
		progressDlg.dismissProgresDialog();
		String rawCmd;
		final WebView wv = (WebView)findViewById(R.id.readingPane);
		try {
			updatePageThreadInfo(json);
			rawCmd = json.getString(CommandName.CommandIndicator);
			if(rawCmd == CommandName.ArticleCommandName.THREADINFO) {
				scrollX = wv.getScrollX();
				scrollY = wv.getScrollY();
				wv.loadDataWithBaseURL(null, generateHtmlPage(json), 
						"text/html", "utf-8", null);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void updatePageThreadInfo(JSONObject json) {
		try {
			pagination = json.getJSONObject(ArticleResponse.PAGE_INFO);
			board = json.getString(ArticleModel.BOARD);
			threadId = json.getInt(ArticleModel.THREAD_ID);
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
