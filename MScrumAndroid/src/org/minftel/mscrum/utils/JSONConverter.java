package org.minftel.mscrum.utils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.minftel.mscrum.model.ProjectDetail;
import org.minftel.mscrum.model.SprintDetail;
import org.minftel.mscrum.model.TaskDetail;
import org.minftel.mscrum.model.UserDetail;

public class JSONConverter {
	
	/**
	 * Convert JSONObject to ProjectDetail
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	public static ProjectDetail fromJSONObjectToProjectDetail(JSONObject jsonObject) throws JSONException {
		ProjectDetail projectDetail = new ProjectDetail();
		projectDetail.setIdProject(jsonObject.getInt("id"));
		projectDetail.setName(jsonObject.getString("name"));
		projectDetail.setDescription(jsonObject.getString("description"));
		projectDetail.setInitialDate(new Date(jsonObject.getLong("initialdate")));
		projectDetail.setEndDate(new Date(jsonObject.getLong("enddate")));
		
		UserDetail scrumMaster = fromJSONObjectToUserDetail(jsonObject.getJSONObject("scrummaster"));
		
		projectDetail.setScrumMaster(scrumMaster);
		
		return projectDetail;
	}
	
	/**
	 * Convert JSON String to ProjectDetail list
	 * @param json String
	 * @return ProjectDetail list
	 * @throws JSONException
	 */
	public static List<ProjectDetail> fromJSONtoProjecList(String json) throws JSONException {
		JSONArray jsonProjects = new JSONArray(json);
		List<ProjectDetail> projects = new ArrayList<ProjectDetail>();
		
		for (int i = 0; i < jsonProjects.length(); i++) {
			JSONObject project = jsonProjects.getJSONObject(i);
			ProjectDetail projectDetail = fromJSONObjectToProjectDetail(project);
			
			projects.add(projectDetail);
		}
		
		return projects;
	}
	
	/**
	 * Convert JSONObject to UserDetail
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	public static UserDetail fromJSONObjectToUserDetail(JSONObject jsonObject) throws JSONException {
		UserDetail userDetail = new UserDetail();
		userDetail.setName(jsonObject.getString("name"));
		userDetail.setSurname(jsonObject.getString("surname"));
		userDetail.setEmail(jsonObject.getString("email"));
		userDetail.setPhone(jsonObject.getString("phone"));
		
		return userDetail;
	}
	
	/**
	 * Convert JSON String to UserDetail list
	 * @param json String
	 * @return UserDetail list
	 * @throws JSONException
	 */
	public static List<UserDetail> fromJSONtoUserList(String json) throws JSONException {
		JSONArray jsonUsers = new JSONArray(json);
		List<UserDetail> users = new ArrayList<UserDetail>();
		
		for (int i = 0; i < jsonUsers.length(); i++) {
			JSONObject user = jsonUsers.getJSONObject(i);
			UserDetail userDetail = fromJSONObjectToUserDetail(user);
			
			users.add(userDetail);
		}
		
		return users;
	}
	
	/**
	 * Convert JSON String to SprintDetail list
	 * @param json String
	 * @return SprintDetail list
	 * @throws JSONException
	 */
	public static List<SprintDetail> fromJSONtoSprintList(String json) throws JSONException {
		JSONArray jsonSprints = new JSONArray(json);
		List<SprintDetail> sprints = new ArrayList<SprintDetail>();
		
		for (int i = 0; i < jsonSprints.length(); i++) {
			JSONObject sprint = jsonSprints.getJSONObject(i);
			SprintDetail sprintDetail = fromJSONObjectToSprintDetail(sprint);
			
			sprints.add(sprintDetail);
		}
		
		return sprints;
	}
	
	/**
	 * Convert JSON String to SprintDetail
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	public static SprintDetail fromJSONObjectToSprintDetail(JSONObject jsonObject) throws JSONException {
		SprintDetail sprintDetail = new SprintDetail();
		sprintDetail.setIdSprint(jsonObject.getInt("id"));
		sprintDetail.setSprintNumber((jsonObject.getInt("sprintnumber")));
		sprintDetail.setInitialDate(new Date((jsonObject.getLong("initialdate"))));
		sprintDetail.setEndDate(new Date(jsonObject.getLong("enddate")));
		sprintDetail.setProject((jsonObject.getInt("idproject")));
		
		return sprintDetail;
	}
	
	/**
	 * Convert JSON String to TaskDetail list
	 * @param json String
	 * @return TaskDetail list
	 * @throws JSONException
	 */
	public static List<TaskDetail> fromJSONtoTaskList(String json) throws JSONException {
		JSONArray jsonSprints = new JSONArray(json);
		List<TaskDetail> tasks = new ArrayList<TaskDetail>();
		
		for (int i = 0; i < jsonSprints.length(); i++) {
			JSONObject task = jsonSprints.getJSONObject(i);
			TaskDetail taskDetail = fromJSONObjectToTaskDetail(task);
			
			tasks.add(taskDetail);
		}
		
		return tasks;
	}
	
	/**
	 * Convert JSON String to TaskDetail
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	public static TaskDetail fromJSONObjectToTaskDetail(JSONObject jsonObject) throws JSONException {
		TaskDetail taskDetail = new TaskDetail();
		taskDetail.setIdTask(jsonObject.getInt("id"));
		taskDetail.setState(jsonObject.getString("state").charAt(0));
		taskDetail.setDescription(jsonObject.getString("description"));
		taskDetail.setTime(jsonObject.getInt("time"));
		
		UserDetail userDetail = fromJSONObjectToUserDetail(jsonObject.getJSONObject("user"));
		
		taskDetail.setUser(userDetail);
		
		return taskDetail;
	}
	
}
