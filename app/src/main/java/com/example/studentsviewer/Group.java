package com.example.studentsviewer;

import java.io.Serializable;

public class Group  implements Serializable{
    private long id;
    private String nameGroup;
    private String nameDepartment;
    private int totalStudents;
    
    public Group(){}

	public Group(long id, String nameGroup, String nameDepartment,
		int totalStudents) {
		this.id = id;
		this.nameGroup = nameGroup;
		this.nameDepartment = nameDepartment;
		this.totalStudents = totalStudents;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNameGroup() {
		return nameGroup;
	}

	public void setNameGroup(String nameGroup) {
		this.nameGroup = nameGroup;
	}

	public String getNameDepartment() {
		return nameDepartment;
	}

	public void setNameDepartment(String nameDepartment) {
		this.nameDepartment = nameDepartment;
	}

	public int getTotalStudents() {
		return totalStudents;
	}

	public void setTotalStudents(int totalStudents) {
		this.totalStudents = totalStudents;
	}
    
    
}
