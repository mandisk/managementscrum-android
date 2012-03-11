package org.minftel.mscrum.utils;

public class ScrumConstants {
	
	public static final String TAG = "MScrum";
	
	// SHARED PREFERENCES
	public static final String SHARED_PREFERENCES_FILE = "ScrumPreferences";
	public static final String SESSION_ID = "session_id";
	
	// DIRECCION SERVLET FRONTAL
//	private static String DIRECCION_IP_SERVER = "10.0.2.2";
	private static String DIRECCION_IP_SERVER = "192.168.1.134";
	public static String BASE_URL = "http://" + DIRECCION_IP_SERVER + ":8080/MScrum-war/Dispatcher";
	
	// ACTIONS
	public static final int ACTION_LOGIN = 0;	
	public static final int ACTION_REGISTER = 1;
	public static final int ACTION_REQUEST_LIST_SPRINTS = 2;
	
	// SERVER RESPONSES
	public static final String ERROR_LOGIN = "ERROR_LOGIN";
	
	// RECEIVERS ACTIONS
	public static final String BROADCAST_GO_PROJECTS = "org.minftel.receiver.action.GO_PROJECTS";
	public static final String BROADCAST_GO_LOGIN = "org.minftel.receiver.action.GO_LOGIN";
}
