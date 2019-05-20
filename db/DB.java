package com.ht.db;

import java.io.IOException;

import com.ht.entity.Table;
import com.ht.entity.Tables;
import com.ht.exception.MyException;
import com.util.comm.Tools;
import com.utitl.check.Checker;

public class DB {
	 
	public static String readComm(){
		String s ="";
		try {
			char r=0;
			do{
				r=(char)System.in.read();
				if(r==';') break;
				s+=r; 
			} while(r!=';');
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static void main(String[] args) throws MyException {
		System.out.println("#########开始#########");
		while(true) {
	    String h = readComm().trim();
	
	    if(h.equals("exit"))
	    {
	    	System.out.println("退出成功");
	    	break;
	    }
		Checker ch = new Checker(h);
		
		try {
			 
			if(ch.getComm().charAt(0)=='c'||ch.getComm().charAt(0)=='C') {
				Table tb = ch.checkCreate();
				Tables t= Tools.readTables();
				if(t==null) {
					if(tb!=null) {
					    Tables tbs = new Tables();
					    tbs.getTables().add(tb);
						Tools.writeTables(tbs);
						System.out.println("创建成功！");
					}  else {
						
					 }
				} else {
					 if(t.getTables().contains(tb)) {
						 throw new MyException("表已经存在！创建失败");
					 }
					 else {
						 t.getTables().add(tb);
					     Tools.writeTables(t);
					     
					     System.out.println("表创建成功！");
					 }
					
				}
			} else if(ch.getComm().charAt(0)=='s'||ch.getComm().charAt(0)=='S') {
				ch.checkSelect();
			}else if(ch.getComm().charAt(0)=='i'||ch.getComm().charAt(0)=='I') {
				ch.checkInsert();
			}else if(ch.getComm().charAt(0)=='d'||ch.getComm().charAt(0)=='D') {
				ch.checkDel();
			}else if(ch.getComm().charAt(0)=='u'||ch.getComm().charAt(0)=='U') {
				ch.checkUpdate();
			}else if(ch.getComm().equals("exit")) {
				System.out.println("退出成功");
			}
				
			else {
				throw new MyException("语法错误！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
		System.out.println("谢谢使用！");
	}
	 
}
