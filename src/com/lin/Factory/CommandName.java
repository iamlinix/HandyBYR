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
		public final static String ARTICLEINFO 		= "articleInfo"; // ��ȡ������Ϣ
		public final static String THREADINFO 		= "threadInfo"; // ��ȡ������Ϣ
		public final static String POSTARTICLE 		= "postArticle"; // ��������
		public final static String FORWARDARTICLE 	= "forwardArticle"; // ת������
		public final static String UPDATEARTICLE 	= "updateArticle"; // ��������
		public final static String DELETEARTICLE 	= "deleteArticle"; // ɾ������ 
	}
	
	public static class AttachmentCommandName {
		public final static String ATTACHINFO 		= "attachmentInfo"; //  ��ȡ������Ϣ
		public final static String UPLOADATTACH 	= "uploadAttachment"; //  �ϴ�����
		public final static String DELETEATTACH 	= "deleteAttachment"; // ɾ������
	}
	
	public static class MailCommandName {
		public final static String INBOXMAILLIST 	= "inboxmailist"; //  ��ȡ������Ϣ
		public final static String OUTBOXMAILLIST 	= "outboxmailist"; //  ��ȡ������Ϣ
		public final static String MAILBOXINFO 		= "mailboxInfo"; //��ȡ�����������Ϣ
		public final static String MAILINFO 		= "mailInfo"; //  ��ȡ�ż���Ϣ
		public final static String SENDMAIL 		= "sendMail"; //  ���Žӿ�
		public final static String FORWARDMAIL 		= "forwardMail"; // ת���ż�
		public final static String REPLYMAIL 		= "replyMail"; // ���Žӿ�
		public final static String DELETEMAIL 		= "deleteMail"; // ɾ���ż�
	}
	
	public static class FavoriteCommandName {
		public final static String FAVORINFO 		= "favorInfo"; //  ��ȡ�ղؼ���Ϣ
		public final static String ADDFAVOR 		= "AddFavor"; // ��ĳһ���ղؼ���Ӱ���/Ŀ¼
		public final static String DELETEFAVOR 		= "deleteFavor"; // ɾ��ĳһ���ղؼ��µİ���/Ŀ¼
	}
	
	public static class SearchCommandName {
		public final static String SEARHBOARD 		= "searchBoard"; //  ���������Ŀ¼
		public final static String SEARCHARTICLE 	= "searchArticle"; // ����һ���������������
		public final static String SEARCHTHREAD 	= "searchThread"; // ����һ���������������
	}
	
	public static class ReferCommandName {
		public final static String REFERLIST 		= "referList"; // ��ȡָ�����������б�
		public final static String REPLYLIST 		= "replyList";
		public final static String REFERINFO 		= "referInfo"; // ��ȡָ���������ѵ�������Ϣ
		public final static String REPLYINFO 		= "replyInfo";
		public final static String SETREAD 			= "setReferRead"; // ����ָ������Ϊ�Ѷ�
		public final static String DELETEREFER 		= "deleteRefer"; // ɾ��ָ������
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
