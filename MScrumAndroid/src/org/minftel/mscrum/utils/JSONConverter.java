package org.minftel.mscrum.utils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.minftel.mscrum.model.ProjectDetail;
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
	 * Convert JSON String to UserDetail
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static UserDetail fromJSONtoUserDetail(String json) throws JSONException{
		JSONObject jsonUser = new JSONObject(json);
		UserDetail userDetail = fromJSONObjectToUserDetail(jsonUser);
		
		return userDetail;
	}
}