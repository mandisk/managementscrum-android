package org.minftel.mscrum.model;

import java.util.Date;

public class SprintDetail {
	
	private int sprintNumber;
	private Date initialDate;
	private Date endDate;
	private int project;	
	
	public SprintDetail(){		
	}
	
	public SprintDetail(int sprintNumber, Date initialDate, Date endDate,
			int project) {
		super();
		this.sprintNumber = sprintNumber;
		this.initialDate = initialDate;
		this.endDate = endDate;
		this.project = project;
	}
		
	public int getSprintNumber() {
		return sprintNumber;
	}
	public void setSprintNumber(int sprintNumber) {
		this.sprintNumber = sprintNumber;
	}
	public Date getInitialDate() {
		return initialDate;
	}
	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getProject() {
		return project;
	}
	public void setProject(int project) {
		this.project = project;
	}
	
}
