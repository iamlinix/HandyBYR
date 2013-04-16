package com.lin.Controller;

import java.io.Serializable;

public class ParamPair implements Serializable {
	
	private int id;
	private String value;
	private String className;
	
	public ParamPair(int id, String value, String cls) {
		this.id = id;
		this.value = value;
		this.className = cls;
	}
	
	public int getId() {
		return id;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getClassName() {
		return className;
	}
	
}
