package org.minftel.mscrum.model;

import java.io.Serializable;
import java.util.Date;

public class SprintDetail implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int idSprint;
	private int sprintNumber;
	private Date initialDate;
	private Date endDate;
	private int project;	
	
	public SprintDetail(){		
	}
	
	public SprintDetail(int idSprint, int sprintNumber, Date initialDate, Date endDate,
			int project) {
		this.idSprint = idSprint;
		this.sprintNumber = sprintNumber;
		this.initialDate = initialDate;
		this.endDate = endDate;
		this.project = project;
	}
		
	public int getIdSprint() {
		return idSprint;
	}

	public void setIdSprint(int idSprint) {
		this.idSprint = idSprint;
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
