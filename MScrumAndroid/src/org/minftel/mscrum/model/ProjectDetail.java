package org.minftel.mscrum.model;

import java.io.Serializable;
import java.util.Date;

public class ProjectDetail implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int idProject;
	private String name;
	private String description;
	private Date initialDate;
	private Date endDate;
	private UserDetail scrumMaster;
	
	public ProjectDetail() {
	}
	
	public ProjectDetail(int idProject, String name, String description,
			Date initialDate, Date endDate, UserDetail scrumMaster) {
		this.idProject = idProject;
		this.name = name;
		this.description = description;
		this.initialDate = initialDate;
		this.endDate = endDate;
		this.scrumMaster = scrumMaster;
	}

	public int getIdProject() {
		return idProject;
	}
	
	public void setIdProject(int idProject) {
		this.idProject = idProject;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
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
	
	public UserDetail getScrumMaster() {
		return scrumMaster;
	}
	
	public void setScrumMaster(UserDetail scrumMaster) {
		this.scrumMaster = scrumMaster;
	}
	
	@Override
	public String toString() {
		return "[ id: " + this.idProject + " ]";
	}
}
