package com.lin.Factory;

import it.sauronsoftware.base64.Base64;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.lin.Model.MetaModel.ReferModel;

import android.net.http.AndroidHttpClient;
import android.os.StrictMode;
import android.util.Log;

public class Command {

	private static Map<String, CommandDelegate> router = 
			new HashMap<String, CommandDelegate>();
	
	private static String _user;
	private static String _pass;

	public static String COMMAND_ERROR = "com.lin.handybyr.commanderror";
	public static boolean isRegisteredUser = false;
	
	private static final String _uriPrefix = "http://api.byr.cn";//"http://nforum.byr.edu.cn/api" ;
	private static final String appkey = "?appkey=0ae1b3728362ea9b&";
	private static final String format = ".json"; // use json only
	private static final String attBaseUrl = "http://bbs.byr.cn/att/";
	
	public static final String GET = "GET";
	public static final String POST = "POST";

	public static String BuildCommandUri(String command) {
		return _uriPrefix + command + format + appkey;
	}
	
	public static String BuildCommandUriWithParam(String command, String param) {
		return BuildCommandUri(command) + param;
	}
	
	public static String BuildAttImageUrl(String board, int articleId, int attId) {
		return attBaseUrl + "/" + board + "/" + String.valueOf(articleId) + "/" 
				+ String.valueOf(attId);
	}

	public static class UserCommand {
		private final static String query = "/user/query/%d";
		private final static String login = "/user/login";
		private final static String logout = "/user/logout";

		public static void LogIn(String user, String pass) {
			Log.v("LIN", Thread.currentThread().getStackTrace()[3].getClassName());
			_user = user;
			_pass = pass;
			isRegisteredUser = !_pass.isEmpty();
			new NetworkTask().execute(BuildCommandUri(login),
			 _user, _pass, GET, CommandName.UserCommandName.LOGIN
			 , callerCls());
		}

		public static void LogOut() {
			new NetworkTask().execute(BuildCommandUri(logout), _user, _pass,
					GET, CommandName.UserCommandName.LOGOUT
					, callerCls());
		}
	}

	public static class SectionCommand {
		private final static String section = "/section/%s";
		private final static String rootSection = "/section";

		public static void rootSections() {
			Log.v("LIN", Thread.currentThread().getStackTrace()[3].getClassName());
			new NetworkTask().execute(BuildCommandUri(rootSection), _user, _pass,
					GET, CommandName.SectionCommandName.ROOTSECTIONS
					, callerCls());
		}

		public static void sectionList(String sectionName) {
			Log.v("LIN", Thread.currentThread().getStackTrace()[3].getClassName());
			new NetworkTask().execute(
					BuildCommandUri(String.format(section, sectionName)),
					_user, _pass, GET, 
					CommandName.SectionCommandName.SECTIONLIST
					, callerCls());
		}
	}

	public static class BoardCommand {
		private final static String board = "/board/%s";
		private final static String boardWithPageParam = "count=%d&page=%d";

		public static void articleList(String boardName) {
			Log.v("LIN", Thread.currentThread().getStackTrace()[3].getClassName());
			new NetworkTask().execute(
					BuildCommandUri(String.format(board, boardName)), _user,
					_pass, GET, CommandName.BoardCommandName.BOARD
					, callerCls());
		}
		
		public static void articleListAtPage(String boardName, int countPerPage, int page) {
			new NetworkTask().execute(
					BuildCommandUriWithParam(String.format(board, 
							boardName), String.format(boardWithPageParam, countPerPage, page)),					  
							_user, _pass, GET, 
							CommandName.BoardCommandName.BOARD
							, callerCls());
		}
	}

	public static class ArticleCommand {
		private final static String article = "/article/%s/%d"; // 获取文章信息
		private final static String thread = "/threads/%s/%d"; // 获取主题信息
		private final static String threadWithPageParam = "count=%d&page=%d";
		private final static String post = "/article/%s/post"; // 发表文章
		private final static String forward = "/article/%s/forward/%d"; // 转寄文章
		private final static String update = "/article/%s/update/%d"; // 更新文章
		private final static String delete = "/article/%s/delete/%d"; // 删除文章
		private final static String titleParam = "title";
		private final static String contentParam = "content";
		private final static String replyParam = "reid";
		

		public static void articleInfo(String board, int articleId) {
			new NetworkTask().execute(
					BuildCommandUri(String.format(article, board, articleId)),
					_user, _pass, GET, CommandName.ArticleCommandName.ARTICLEINFO
					, callerCls());
		}

		public static void threadInfo(String board, int threadId) {
			new NetworkTask().execute(
					BuildCommandUri(String.format(thread, board, threadId)),
					_user, _pass, GET, CommandName.ArticleCommandName.THREADINFO
					, callerCls());
		}
		
		public static void threadInfoAtPage(String board, int threadId, 
				int countPerPage, int page) {
			new NetworkTask().execute(
					BuildCommandUriWithParam(String.format(thread, board, 
							threadId), String.format(threadWithPageParam, countPerPage, page)),					  
							_user, _pass, GET, 
							CommandName.ArticleCommandName.THREADINFO
							, callerCls());
		}

		
		public static void postArticle(String board, String content, String title) {
			new NetworkTask().execute(
					BuildCommandUri(String.format(post, board)),
					_user, _pass, POST, CommandName.ArticleCommandName.POSTARTICLE
					, callerCls(), titleParam, title, contentParam, content);
		} 
		
		public static void replyArticle(String board, String content, 
				String title, int replyId) {
			new NetworkTask().execute(
					BuildCommandUri(String.format(post, board)),
					_user, _pass, POST, CommandName.ArticleCommandName.POSTARTICLE
					, callerCls(), titleParam, title, contentParam, content, 
					replyParam, String.valueOf(replyId));
		}
		
		/*
		 * forward an article or a thread
		 */
		public static void forwardArticle(String board, int articleId) {
			new NetworkTask().execute(
					BuildCommandUri(String.format(post, board)),
					_user, _pass, POST, CommandName.ArticleCommandName.POSTARTICLE
					, callerCls());
		}
		
		/*
		 * update an article or a thread
		 */
		public static void updateArticle(String board, int articleId) {
			new NetworkTask().execute(
					BuildCommandUri(String.format(post, board)),
					_user, _pass, POST, CommandName.ArticleCommandName.POSTARTICLE
					, callerCls());
		}
		
		/*
		 * delete an article or a thread
		 */
		public static void deleteArticle(String board, int articleId) {
			new NetworkTask().execute(
					BuildCommandUri(String.format(post, board)),
					_user, _pass, POST, CommandName.ArticleCommandName.POSTARTICLE
					, callerCls());
		}
	}

	public static class AttachmentCommand {
		private final static String attInfo = "/attachment/%s/%d"; // 获取附件信息
		private final static String upload = "/attachment/%s/add/%d"; // 上传附件
		private final static String delete = "/attachment/%s/delete/%d"; // 删除附件
	}

	public static class MailCommand {
		private final static String inboxmaillist = "/mail/inbox"; // 获取信箱信息
		private final static String outboxmaillist = "/mail/outbox"; // 获取信箱信息
		private final static String boxInfo = "/mail/info"; // 获取信箱的属性信息
		private final static String mail = "/mail/%s/%d"; // 获取信件信息
		private final static String send = "/mail/send"; // 发信接口
		private final static String forward = "/mail/%s/forward/%d"; // 转寄信件
		private final static String reply = "/mail/%s/reply/%d"; // 回信接口
		private final static String delete = "/mail/%s/delete/%d"; // 删除信件
		
		private final static String idParam = "id";
		private final static String titleParam = "title";
		private final static String contentParam = "content";
		
		public static void inboxMailList() {
			new NetworkTask().execute(
					BuildCommandUri(inboxmaillist),
					_user, _pass, GET, CommandName.MailCommandName.INBOXMAILLIST
					, callerCls());
		}
		
		public static void outboxMailList() {
			new NetworkTask().execute(
					BuildCommandUri(outboxmaillist),
					_user, _pass, GET, CommandName.MailCommandName.OUTBOXMAILLIST
					, callerCls());
		}
		
		public static void sendMail(String id, String title, String content) {
			new NetworkTask().execute(
					BuildCommandUri(send), _user, _pass, POST, 
					CommandName.MailCommandName.SENDMAIL, callerCls(),
					idParam, id, titleParam, title, contentParam, content);
		}
		
		public static void mailInfo(String mailbox, int index) {
			new NetworkTask().execute(
					BuildCommandUri(String.format(mail, mailbox, index)),
					_user, _pass, GET, CommandName.MailCommandName.MAILINFO
					, callerCls());
		}
		
		public static void replyMail(int index, String title, String content) {
			new NetworkTask().execute(
					BuildCommandUri(String.format(reply, "inbox", index)),
					_user, _pass, POST, CommandName.MailCommandName.REPLYMAIL, callerCls(),
					titleParam, title, contentParam, content);	
		}
	}

	public static class FavoriteCommand {
		private final static String favorites = "/favorite/%d"; // 获取收藏夹信息
		private final static String add = "/favorite/add/%d"; // 向某一级收藏夹添加版面/目录
		private final static String delete = "/favorite/delete/%d"; // 删除某一级收藏夹下的版面/目录
	}

	public static class SearchCommand {
		private final static String board = "/search/%s"; // 搜索版面或目录
		private final static String article = "/search/%s"; // 搜索一个或多个版面的文章
		private final static String threads = "/search/threads"; // 搜索一个或多个版面的主题
	}

	public static class ReferCommand {
		public final static String AT_TYPE = "at";
		public final static String RP_TYPE = "reply";

		private final static String list = "/refer/%s"; // 获取指定提醒类型列表
		private final static String info = "/refer/%s/info"; // 获取指定类型提醒的属性信息
		private final static String setRead = "/refer/%s/setRead/%d"; // 设置指定提醒为已读
		private final static String delete = "/refer/%s/delete/%d"; // 删除指定提醒
		
		public static void referInfo() {
			new NetworkTask().execute(
					BuildCommandUri(String.format(info, ReferModel.ReferType.REFER)),
					_user, _pass, GET, CommandName.ReferCommandName.REFERINFO
					, callerCls());
		}
		
		public static void replyInfo() {
			new NetworkTask().execute(
					BuildCommandUri(String.format(info, ReferModel.ReferType.REPLY)),
					_user, _pass, GET, CommandName.ReferCommandName.REPLYINFO
					, callerCls());
		}
		
		public static void referList() {
			new NetworkTask().execute(
					BuildCommandUri(String.format(list, ReferModel.ReferType.REFER)),
					_user, _pass, GET, CommandName.ReferCommandName.REFERLIST
					, callerCls());
		}
		
		public static void replyList() {
			new NetworkTask().execute(
					BuildCommandUri(String.format(list, ReferModel.ReferType.REPLY)),
					_user, _pass, GET, CommandName.ReferCommandName.REPLYLIST
					, callerCls());
		}
		
		public static void setReferReadById(int id) {
			new NetworkTask().execute(
					BuildCommandUri(String.format(setRead, ReferModel.ReferType.REFER, id)),
					_user, _pass, POST, CommandName.ReferCommandName.SETREAD
					, callerCls());
		}
		
		public static void setReplyReadById(int id) {
			new NetworkTask().execute(
					BuildCommandUri(String.format(setRead, ReferModel.ReferType.REPLY, id)),
					_user, _pass, POST, CommandName.ReferCommandName.SETREAD
					, callerCls());
		}
		
		public static void setReferReadAll() {
			new NetworkTask().execute(
					BuildCommandUri(String.format(setRead, ReferModel.ReferType.REFER, -1)),
					_user, _pass, POST, CommandName.ReferCommandName.SETREAD
					, callerCls());
		}
		
		public static void setReplyReadAll() {
			new NetworkTask().execute(
					BuildCommandUri(String.format(setRead, ReferModel.ReferType.REPLY, -1)),
					_user, _pass, POST, CommandName.ReferCommandName.SETREAD
					, callerCls());
		}
	}

	public static class WidgetCommand {
		private final static String topten = "/widget/topten";
		private final static String recommend = "/widget/recommend";
		private final static String hot = "/widget/section-%s";

		public static void toptenList() {
			Log.v("LIN", Thread.currentThread().getStackTrace()[3].getClassName());
			new NetworkTask().execute(BuildCommandUri(topten), _user, _pass,
					GET, CommandName.WidgetCommand.TOPTENLIST, callerCls());
		}
		
		public static void recommendList() {
			Log.v("LIN", Thread.currentThread().getStackTrace()[3].getClassName());
			new NetworkTask().execute(BuildCommandUri(recommend), _user, _pass,
					GET, CommandName.WidgetCommand.RECOMMENDDLIST, callerCls());
		}
		
	}

	// method section
	public static void onRequestReturnStatic(JSONObject json) {
		// TODO Auto-generated method stub
		try {
			CommandDelegate callback = router.get(json.getString(
					CommandName.ClassNameOfCaller));
			if(callback != null) {
				callback.OnCommandComplete(json);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void onRequestErrorStatic(String message, String caller) {
		CommandDelegate callback = router.get(caller);
		if(callback != null) {
			callback.OnCommandError(message);
		}
	}

	public static void registerOnRouter(CommandDelegate cd) {
		//0, 1, 2 are track stack functions
		String className = Thread.currentThread().getStackTrace()[3].getClassName();
		router.put(className, cd);
	}
	
	private static String getBaseClass(String cls) {
		int index = cls.indexOf("$");
		if(index > 0)
			return cls.substring(0,  index);
		else
			return cls;
	}
	
	private static String callerCls() {
		return getBaseClass(
				Thread.currentThread().getStackTrace()[5].getClassName());
	}
}
