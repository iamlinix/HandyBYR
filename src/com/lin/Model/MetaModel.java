package com.lin.Model;

public class MetaModel {
	//
	public static class InteractionModel {
		public static final String JSONEXTRA		= "jsonExtaInString";
	}
	
	public static class AboutModel {
		public static final String REFER_ME			= "referMe";
		public static final String REPLY_ME			= "replyMe";
		public static final String MAILBOX			= "mailbox";
	}
	
	//UI model
	public static class ListViewItemModel {
		public static final String SUBTITLE			= "subtitle";
		public static final String TITLE			= "title";
		public static final String FRONT_IMAGE		= "front_image";
		public static final String REAR_IMAGE		= "rear_image";
	}
	
	//http response objects
	public static class ToptenResponse {
		public static final String ARTICLE			= "article";
	}
	
	public static class RecommendResponse {
		public static final String ARTICLE			= "article";
	}
	
	public static class SectionResponse {
		//root section command response
		public static final String SECTION_COOUNT	= "section_count";
		public static final String SECTIONS			= "section";
		
		//seciton command
		public static final String SUB_SECTIONS		= "sub_section";
		public static final String BOARDS			= "board";
	}
	
	public static class BoardResponse {
		public static final String ARTICLES			= "article";
		public static final String PAGINATION		= "pagination";
	}
	
	public static class ArticleResponse {
		//thread
		public static final String ARTICLES			= "article";
		public static final String PAGE_INFO		= "pagination";
	}
	
	public static class ReferResponse {
		public static final String ARTICLES			= "article";
		public static final String DESCRIPTION		= "description";
		public static final String PAGINATION		= "pagination";
	}
	
	public static class MailResponse {
		public static final String MAILS			= "mail";
		public static final String DESCRIPTION		= "description";
		public static final String PAGINATION		= "pagination";
	}
	
	//meta model
	public static class ArticleModel {
		public static final String ARTICLE_ID		= "id";
		public static final String THREAD_ID		= "group_id";
		public static final String REPLY_ID			= "reply_id";
		public static final String ARTICLE_FLAG		= "flag";
		public static final String POSITION			= "position";
		public static final String IS_TOP			= "is_top";
		public static final String IS_SUBJECT		= "is_subject";
		public static final String HAS_ATTACHMENT	= "has_attachment";
		public static final String ADMIN_OF_ARTICLE	= "is_admin";
		public static final String TITLE			= "title";
		public static final String USER			= "user";
		public static final String POST_TIME		= "post_time";
		public static final String BOARD			= "board_name";
		public static final String CONTENT			= "content";
		public static final String ATTACHMENTS		= "attachment";
		public static final String PRE_ARTICLE_ID	= "previous_id";
		public static final String NXT_ARTICLE_ID	= "next_id";
		public static final String PRE_THREAD_ID	= "threads_previous_id";
		public static final String NXT_THREAD_ID	= "threads_next_id";
		public static final String REPLY_COUNT		= "reply_count";
		public static final String LAST_RPL_USR_ID	= "last_reply_user_id";
		public static final String LAST_RPL_TIME	= "last_reply_time";

	}
	
	public static class UserModel {
		public static final String USER_ID			= "id";
		public static final String USER_NAME		= "user_name";
		public static final String FACE_URL			= "face_url";
		public static final String FACE_WIDTH		= "face_width";
		public static final String FACE_HEIGHT		= "face_height";
		public static final String USER_GENDER		= "gender";
		public static final String USER_ASTRO		= "astro";
		public static final String LIFE_POWER		= "life";
		public static final String USER_QQ			= "qq";
		public static final String USER_MSN			= "msn";
		public static final String USER_HOMEPAGE	= "home_page";
		public static final String USER_LEVEL		= "level";
		public static final String IS_ONLINE_NOW	= "is_online";
		public static final String TOTAL_ARTICLE	= "post_count";
		public static final String LAST_LOGIN_TIME	= "last_login_time";
		public static final String LAST_LOGIN_IP	= "last_login_ip";
		public static final String HIDE_GENDER		= "is_hide";
		public static final String REGISTERD_USER	= "is_register";
		public static final String FIRST_LOGIN_TIME	= "first_login_time";
		public static final String LOGIN_COUNT		= "login_count";
		public static final String IS_ADMIN			= "is_admin";
		public static final String ONLINE_SECONDS	= "stay_count";
	}
	
	public static class BoardModel {
		public static final String BOARD_NAME		= "name";
		public static final String BOARD_OWNERS		= "manager";
		public static final String BOARD_DESC		= "description";
		public static final String BOARD_TYPE		= "class";
		public static final String SECTION			= "section";
		public static final String ARTICLES_TODAY	= "post_today_count";
		public static final String THREADS_TODAY	= "post_threads_count";
		public static final String TOTAL_ARTICLES	= "post_all_count";
		public static final String READONLY			= "is_read_only";
		public static final String NO_REPLY			= "is_no_reply";
		public static final String ALLOW_ATTACHMENT	= "allow_attachment";
		public static final String ALLOW_ANONYMOUS	= "allow_anonymous";
		public static final String ALLOW_OUTGO		= "allow_outgo";
		public static final String CAN_POST_HERE	= "allow_post";
		public static final String OL_USER_COUNT	= "user_online_count";
	}
	
	public static class SectionModel {
		public static final String SECTION_NAME		= "name";
		public static final String SECTION_DESC		= "description";
		public static final String IS_ROOT_SECTION	= "is_root";
		public static final String PARENT_SECTION	= "parent";
	}
	
	public static class AttachmentModel {
		public static final String FILES			= "file";
		public static final String SPACE_REMAIN		= "remain_space";
		public static final String COUNT_REMAIN		= "remain_count";
	}
	
	public static class FileModel {
		public static final String FILE_NAME		= "name";
		public static final String FILE_URL			= "url";
		public static final String FILE_SIZE		= "size";
		public static final String THUMBNAIL_SMALL	= "thumbnail_small";
		public static final String THUMBNAIL_MID	= "thumbnail_middle";
	}

	public static class MailboxModel {
		public static final String HAVE_UNREAD_MAIL	= "new_mail";
		public static final String IS_BOX_FULL		= "full_mail";
		public static final String SPACE_USED		= "space_used";
		public static final String CAN_SEND_MAIL	= "can_send";
	}
	
	public static class MailModel {
		public static final String MAIL_INDEX		= "index";
		public static final String IS_M				= "is_m";
		public static final String IS_READ			= "is_read";
		public static final String IS_REPLIED		= "is_reply";
		public static final String HAS_ATTACHMENT	= "has_attachment";
		public static final String MAIL_TITLE		= "title";
		public static final String MAIL_SENDER		= "user";
		public static final String SEND_TIME		= "post_time";
		public static final String MAILBOX			= "box_name";
		public static final String CONTENT			= "content";
		public static final String ATTACHMENTS		= "attchment";
	}
	
	public static class FavoriteModel {
		public static final String LEVEL			= "level";
		public static final String DESCRIPTION		= "description";
		public static final String POSITION			= "position";
	}
	
	public static class PaginationModel { 
		public static final String TOTAL_PAGE_COUNT	= "page_all_count";
		public static final String CURRENT_INDEX	= "page_current_count";
		public static final String ITEMS_PER_PAGE	= "item_page_count";
		public static final String TOTAL_ITEM_COUNT	= "item_all_count";
	}
	
	public static class ReferModel {
		public static final String REFER_INDEX		= "index";
		public static final String REFER_ID			= "id";
		public static final String GROUP_ID			= "group_id";
		public static final String REPLY_ID			= "reply_id";
		public static final String BOARD			= "board_name";
		public static final String TITLE			= "title";
		public static final String POSTER			= "user";
		public static final String POST_TIME		= "time";
		public static final String IS_READ			= "is_read";
		
		public static class ReferType {
			public static final String REFER		= "at";
			public static final String REPLY		= "reply";
		}
	}
	
	public static class WidgetModel {
		public static final String WIDGET_NAME		= "name";
		public static final String TITLE			= "title";
		public static final String LAST_MOD_TIME	= "time";
	}
	
	public static class ErrorModel {
		public static final String ERROR_MSG		= "msg";
		public static final String ERROR_CODE		= "code";
		public static final String FAILED_REQUEST	= "request";
	}
	
	public static class TabTag {
		public static final String TOPTEN_TAG		= "top10";
		public static final String SECTION_TAG		= "sections";
		public static final String RECOMMEND_TAG	= "recommends";
		public static final String ABOUTME_TAG		= "aboutme";
		public static final String MORE_TAG			= "more";
	}
}