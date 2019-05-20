package com.util.comm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.ht.data.Data;
import com.ht.entity.Tables;

public class Tools {
	private static final String name="tables";
	public static String delSpaceEnter(String str){
		if(str==null) return null;
		str=str.replaceAll("[\\s]+", " ");
		str=str.replaceAll("\n"," ");
		return str.trim(); 
	}
	public static Tables readTables(){
		try {
			FileInputStream fis = new FileInputStream(name);  
			ObjectInputStream ois = new ObjectInputStream(fis);  
			Tables tb = (Tables) ois.readObject();  
			ois.close();  
			fis.close();
			return tb;
		} catch (FileNotFoundException e) {
			 
		} catch (ClassNotFoundException e) {
			 
		} catch (IOException e) {
			 
		}  
		return null;
	}
	public static void writeTables(Tables tb)
	{
		try {
			FileOutputStream fos = new FileOutputStream(name);  
			ObjectOutputStream oos = new ObjectOutputStream(fos);  
			oos.writeObject(tb);  
			oos.flush();  
			oos.close();  
			fos.close();  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  

	}
    
	
	public static void writeData(Data tb,String tname)
	{
		try {
			FileOutputStream fos = new FileOutputStream(tname);  
			ObjectOutputStream oos = new ObjectOutputStream(fos);  
			oos.writeObject(tb);  
			oos.flush();  
			oos.close();  
			fos.close();  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  

	}
	
	public static Data readData(String tname){
		try {
			FileInputStream fis = new FileInputStream(tname);  
			ObjectInputStream ois = new ObjectInputStream(fis);  
			Data data = (Data) ois.readObject();  
			ois.close();  
			fis.close();
			return data;
		} catch (FileNotFoundException e) {
			 
		} catch (ClassNotFoundException e) {
			 
		} catch (IOException e) {
			 
		}  
		return null;
	}
   
}
