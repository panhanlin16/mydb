package com.ht.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Data implements Serializable
{
	private static final long serialVersionUID = 1L;
	private List<Object> data = new ArrayList<Object>();

	public List<Object> getData() {
		return data;
	}

	public void setData(List<Object> data) {
		this.data = data;
	}
}
