package com.troy;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Car implements Serializable{
	@Id
	private String id;
	private String value;
	
	
	public Car() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Car(String id, String value) {
		super();
		this.id = id;
		this.value = value;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	

}
