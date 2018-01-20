package com.example.studentsviewer;

import java.io.Serializable;

public class Student implements Serializable {
    private long id;
    private String nameFirst;
    private String nameSecond;
    private String nameThird;
    private String dateBirth;
    private long groupID;
   
	public Student(long id, String nameFirst, String nameSecond, String nameThird, String dateBirth, long groupID) {
		this.id = id;
		this.nameFirst = nameFirst;
		this.nameSecond = nameSecond;
		this.nameThird = nameThird;
		this.dateBirth = dateBirth;
		this.groupID = groupID;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNameFirst() {
		return nameFirst;
	}
	public void setNameFirst(String nameFirst) {
		this.nameFirst = nameFirst;
	}
	public String getNameSecond() {
		return nameSecond;
	}
	public void setNameSecond(String nameSecond) {
		this.nameSecond = nameSecond;
	}
	public String getNameThird() {
		return nameThird;
	}
	public void setNameTherd(String nameThird) {
		this.nameThird = nameThird;
	}
	public String getDateBirth() {
		return dateBirth;
	}
	public void setDateBirth(String dateBirth) {
		this.dateBirth = dateBirth;
	}
	public long getGroupID() {
		return groupID;
	}
	public void setGroupID(long groupID) {
		this.groupID = groupID;
	}

}
