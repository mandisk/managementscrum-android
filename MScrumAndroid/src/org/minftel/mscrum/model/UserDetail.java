package org.minftel.mscrum.model;

public class UserDetail {
	private String name;
	private String surname;
	private String email;
	private String phone;
//	private int id;
	
	public UserDetail() {
		
	}
	

	public UserDetail(String name, String surname, String email, String phone,
			int id) {
		super();
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phone = phone;
//		this.id = id;
	}


//	public int getId() {
//		return id;
//	}
//
//
//	public void setId(int id) {
//		this.id = id;
//	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String toString(){
		return name;
	}
}
