package com.ht.exception;

public class MyException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String em = null;
	public MyException(String s){
		this.em =s ;
		System.out.println(s);
	}
	public String getEm() {
		return em;
	}

	public void setEm(String em) {
		this.em = em;
	}

}
