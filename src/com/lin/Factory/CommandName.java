package com.lin.Factory;

public class CommandName {
	
	public final static String CommandIndicator = "com.lin.handybyr.command";
	public final static String ClassNameOfCaller = "caller";
	
	public static class UserCommandName {
		public static final String LOGIN			= "login";
		public static final String LOGOUT 			= "logout";
		public static final String QUERY 			= "query";
	}
	
	public static class SectionCommandName {
		public final static String SECTIONLIST 		= "sectionList";
		public final static String ROOTSECTIONS 	= "rootSection";
	}
	
	public static class BoardCommandName {
		public final static String BOARD 			= "board";
	}
	
	public static  class ArticleCommandName {
		public final static String ARTICLEINFO 		= "articleInfo"; // 获取文章信息
		public final static String THREADINFO 		= "threadInfo"; // 获取主题信息
		public final static String POSTARTICLE 		= "postArticle"; // 发表文章
		public final static String FORWARDARTICLE 	= "forwardArticle"; // 转寄文章
		public final static String UPDATEARTICLE 	= "updateArticle"; // 更新文章
		public final static String DELETEARTICLE 	= "deleteArticle"; // 删除文章 
	}
	
	public static class AttachmentCommandName {
		public final static String ATTACHINFO 		= "attachmentInfo"; //  获取附件信息
		public final static String UPLOADATTACH 	= "uploadAttachment"; //  上传附件
		public final static String DELETEATTACH 	= "deleteAttachment"; // 删除附件
	}
	
	public static class MailCommandName {
		public final static String INBOXMAILLIST 	= "inboxmailist"; //  获取信箱信息
		public final static String OUTBOXMAILLIST 	= "outboxmailist"; //  获取信箱信息
		public final static String MAILBOXINFO 		= "mailboxInfo"; //获取信箱的属性信息
		public final static String MAILINFO 		= "mailInfo"; //  获取信件信息
		public final static String SENDMAIL 		= "sendMail"; //  发信接口
		public final static String FORWARDMAIL 		= "forwardMail"; // 转寄信件
		public final static String REPLYMAIL 		= "replyMail"; // 回信接口
		public final static String DELETEMAIL 		= "deleteMail"; // 删除信件
	}
	
	public static class FavoriteCommandName {
		public final static String FAVORINFO 		= "favorInfo"; //  获取收藏夹信息
		public final static String ADDFAVOR 		= "AddFavor"; // 向某一级收藏夹添加版面/目录
		public final static String DELETEFAVOR 		= "deleteFavor"; // 删除某一级收藏夹下的版面/目录
	}
	
	public static class SearchCommandName {
		public final static String SEARHBOARD 		= "searchBoard"; //  搜索版面或目录
		public final static String SEARCHARTICLE 	= "searchArticle"; // 搜索一个或多个版面的文章
		public final static String SEARCHTHREAD 	= "searchThread"; // 搜索一个或多个版面的主题
	}
	
	public static class ReferCommandName {
		public final static String REFERLIST 		= "referList"; // 获取指定提醒类型列表
		public final static String REPLYLIST 		= "replyList";
		public final static String REFERINFO 		= "referInfo"; // 获取指定类型提醒的属性信息
		public final static String REPLYINFO 		= "replyInfo";
		public final static String SETREAD 			= "setReferRead"; // 设置指定提醒为已读
		public final static String DELETEREFER 		= "deleteRefer"; // 删除指定提醒
	}
	
	public static class WidgetCommand {
		public final static String TOPTENLIST		= "toptenList";
		public final static String RECOMMENDDLIST 	= "recommendList";
		public final static String HOTLIST 			= "hotList";
	}
	
	public static class RequestOnHtml {
		public final static String NEXT_PAGE		= "next_page";
		public final static String REPLY			= "reply";
		public final static String FORWARD			= "foreard";
		public final static String UPDATE			= "update";
		public final static String SENDMAIL			= "mailto";
	}
}
