package com.radar.common;

public class IqConstant {
	/**联系人*/
	public static final String FRIENDS_LIST="imcrm:friends:list";
	public static final String ADD_FRIENDS_GROUPS="imcrm:friends:addgroup";
	public static final String DEL_FRIENDS_GROUPS="imcrm:friends:delgroup";
	public static final String RENAME_FRIENDS_GROUPS="imcrm:friends:renamegroup";
	public static final String ADD_FRIENDS="imcrm:friends:addmember";
	public static final String DEL_FRIENDS="imcrm:friends:delmember";
	public static final String RENAMR_MEMBER="imcrm:friends:renamemember";
	public static final String MOVE_MEMBER="imcrm:friends:movemember";
	/**更新联系人列表*/
	public static final String ALERT_UPDATE_CONTACT_MEMBER="alert:imcrm:contact:update";
	
	/**群组*/
	public static final String ADD_GROUP_ROOM="imcrm:group:addgrouproom";
	public static final String GROUP_ROOM_LIST="imcrm:group:grouproomlist";
	public static final String UPDATE_GROUP_ROOM="imcrm:group:updategrouproom";
	public static final String ADD_GROUP_ROOM_MEMBER="imcrm:group:addgrouproommember";
	public static final String DEL_GROUP_ROOM_MEMBER="imcrm:group:delgrouproommember";
	public static final String GET_GROUP_ROOM="imcrm:group:grouproombyid";
	public static final String GROUP_ROOM_MEMBER_LIST="imcrm:group:grouproommemberlist";
	public static final String GROUP_ROOM_MEMBERS="imcrm:group:grouproommembers";
	
	/**更新群组成员列表*/
	public static final String ALERT_UPDATE_GROUP_MEMBER="alert:imcrm:groupmember:update";
	
	/**通知*/
	public static final String PUSH_NOTICE="push:imcrm:notice";
	public static final String NOTICE_ACCEPTER_LIST="imcrm:notice:accepter:list";
	public static final String NOTICE_SENDER_LIST="imcrm:notice:sender:list";
	public static final String GET_NOTICE_BYID="imcrm:notice:findnoticebyid";
	public static final String UPDATE_NOTICE_STATE="imcrm:notice:updatenoticestate";
	
	/**聊天记录*/
	public static final String MESSAGE_LIST="imcrm:message:messagelist";
	public static final String DEL_MESSAGE_IDS="imcrm:message:delmessage";
	/**会话*/
	public static final String DIALOGUE_LIST="imcrm:dialogue:dialoguelist";
	public static final String DEL_DIALOGUE_IDS="imcrm:dialogue:deldialogue";
	
	/**apns 推送配置*/
	public static final String PUSH_CONFIG="imcrm:push:config";
	public static final String PUSH_CONFIG_STATUS="imcrm:pushconfig:status";
}
