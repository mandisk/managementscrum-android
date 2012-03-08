package org.minftel.mscrum.model;

public class TaskDetail {
	
	private int idTask;
	private char state;
	private String description;
	private int time;
	private UserDetail user;
	
	public TaskDetail() {
	}

	public TaskDetail(int idTask, char state, String description, int time,
			UserDetail user) {
		this.idTask = idTask;
		this.state = state;
		this.description = description;
		this.time = time;
		this.user = user;
	}

	public int getIdTask() {
		return idTask;
	}

	public void setIdTask(int idTask) {
		this.idTask = idTask;
	}

	public char getState() {
		return state;
	}

	public void setState(char state) {
		this.state = state;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public UserDetail getUser() {
		return user;
	}

	public void setUser(UserDetail user) {
		this.user = user;
	}
	
	
}
