package com.ht.ser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.ht.entity.Table;

public class Operator {
	public void createTable(Table tb) throws IOException{
		String file = tb.getName()+".tb";
		FileOutputStream fos = new FileOutputStream(file);  
		ObjectOutputStream oos = new ObjectOutputStream(fos);  
		oos.writeObject(tb);  
		oos.flush();  
		oos.close();  
		fos.close();  

	} 
	public static void main(String[] args) {
         
	}
}
