package org.minftel.mscrum.utils;

public class ScrumConstants {
	
	public static final String TAG = "MScrum";
	
	// SHARED PREFERENCES
	public static final String SHARED_PREFERENCES_FILE = "ScrumPreferences";
	public static final String SESSION_ID = "session_id";
	
	// DIRECCION SERVLET FRONTAL
	private static String DIRECCION_IP_SERVER = "192.168.1.107";
//	private static String DIRECCION_IP_SERVER = "10.0.2.2";
//	private static String DIRECCION_IP_SERVER = "192.168.1.134";
//	private static String DIRECCION_IP_SERVER = "192.168.1.13";
//	private static String DIRECCION_IP_SERVER = "192.168.1.119";
//	private static String DIRECCION_IP_SERVER = "192.168.133.151";
	public static String BASE_URL = "http://" + DIRECCION_IP_SERVER + ":8080/MScrum-war/Dispatcher";
	public static String SESSION_URL = ";jsessionid=";
	
	// ACTIONS
	public static final int ACTION_LOGIN = 0;	
	public static final int ACTION_REGISTER = 1;
	public static final int ACTION_REQUEST_LIST_SPRINTS = 2;
	public static final int ACTION_REQUEST_LIST_TASKS = 3;
	public static final int ACTION_REQUEST_LIST_USERS = 4;
	public static final int ACTION_ADD_PROJECT = 5;
	public static final int ACTION_ADD_SPRINT = 6;
	public static final int ACTION_ADD_TASK = 7;
	public static final int ACTION_DELETE_PROJECT = 8;
	public static final int ACTION_DELETE_SPRINT = 9;
	public static final int ACTION_DELETE_TASK = 10;

	public static final int ACTION_MODIFY_TASK = 11;
	
	
	// SERVER RESPONSES
	public static final String ERROR_ADD_PROJECT = "ERROR_ADD_PROJECT";
	public static final String ERROR_ADD_TASK = "ERROR_ADD_TASK";
	public static final String ERROR_LOGIN = "ERROR_LOGIN";
	public static final String ERROR_REGISTER = "ERROR_REGISTER";
	public static final String ERROR_PROJECTS = "ERROR_PROJECTS";
	public static final String ERROR_SPRINTS = "ERROR_SPRINTS";
	public static final String ERROR_TASKS = "ERROR_TASKS";

    public static final String REGISTER_OK = "REGISTER_OK";
	public static final String SESSION_EXPIRED = "SESSION_EXPIRED";
	
	// RECEIVERS ACTIONS
	public static final String BROADCAST_GO_PROJECTS = "org.minftel.receiver.action.GO_PROJECTS";
	public static final String BROADCAST_GO_LOGIN = "org.minftel.receiver.action.GO_LOGIN";
	public static final String BROADCAST_GO_SPRINTS = "org.minftel.receiver.action.GO_SPRINTS";
	public static final String BROADCAST_GO_TASKS = "org.minftel.receiver.action.GO_TASKS";
}
