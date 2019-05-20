package com.ht.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tables implements Serializable {
   
 
	private static final long serialVersionUID = 1L;
	private List<Table> tables = new ArrayList<Table>();

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}
	
   
	 
}