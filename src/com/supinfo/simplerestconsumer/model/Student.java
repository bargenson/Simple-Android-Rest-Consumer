package com.supinfo.simplerestconsumer.model;

import java.util.Date;

public class Student {
	
	private Long idBooster;
	private String firstName;
	private String lastName;
	private Date birthDate;
	
	public Student() {
		
	}
	
	public Student(Long idBooster, String firstName, String lastName, Date birthDate) {
		this.idBooster = idBooster;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
	}

	public Long getIdBooster() {
		return idBooster;
	}

	public void setIdBooster(Long idBooster) {
		this.idBooster = idBooster;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	public String getFullName() {
		return firstName + " " + lastName;
	}

}
