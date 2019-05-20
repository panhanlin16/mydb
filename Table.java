package com.ht.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Table implements Serializable{
	 
	private static final long serialVersionUID = 1L;

	@Override
	public int hashCode() {
		return super.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Table) {
			Table t =(Table) obj;
			if(getName().equals(t.getName())) return true;
			else return false;
		}
		return super.equals(obj);
	}
	private String name=null;
	private Map<String, String> mp = new HashMap<String, String>();
	 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, String> getMp() {
		return mp;
	}
	public void setMp(Map<String, String> mp) {
		this.mp = mp;
	}
	public Table(){}
	public Table(String s){
		this.name = s;
	}
}
