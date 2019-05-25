package com.utitl.check;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import sun.tools.jar.resources.jar;

import com.ht.data.Data;
import com.ht.entity.Table;
import com.ht.entity.Tables;
import com.ht.exception.MyException;
import com.util.comm.Tools;

public class Checker {
	private String comm = null;
	public Checker(String s) throws MyException{
		this.comm = Tools.delSpaceEnter(s).trim();
		checkMatch();
	}

	public void checkMatch() throws MyException{
		int len = comm.length();
		int[] stack = new int[comm.length()];
		int top=-1;
		for(int i=0;i<len;i++) {
			if(comm.charAt(i)=='(') {
				stack[++top] = '(';
			} else if(comm.charAt(i)==')') {
				if(top>=0) {
					top--;
				} else {
					throw new MyException("括号不匹配！！"); 
				}
			}
		}

	}
	public Table checkCreate() throws MyException{
		String[] words = comm.split("[\\s]");
		Table table  = new Table();

		if(!words[1].toUpperCase().equals("TABLE")){
			throw new MyException("无法解析 创建表语句！");
		}
		String na = "";
		for(int i=13;i<comm.length();i++) {
			if(comm.charAt(i)=='(') break; 
			na+=comm.charAt(i);
		}

		if(!na.trim().matches("[a-zA-Z0-9_]+")) {

			throw new MyException(" 表名创建无效，只能为大小写字母下划线！");
		}
		
		table.setName(na.trim());
		String st="";
		for(int i=0;i<comm.length();i++) {
			if(comm.charAt(i)=='(') {
				st = comm.substring(i+1,comm.length()-1);
				break;
			}
		}
		if(st.equals("")){
			throw new MyException("括号不匹配！");
		}
		String[] w = st.split(",");
		for(int i=0;i<w.length;i++) {
			String name = Tools.delSpaceEnter(w[i]).trim();
			String[] c = name.split("\\s");
			if(c.length!=2) {
				throw new MyException("属性解析错误！");
			}else {
				if(c[0].trim().matches("[a-zA-Z0-9_]+")==false||
						(c[0].charAt(0)>='0'&&c[0].charAt(0)<='9')){
					throw new MyException("属性名定义不合法");
				} else {
					if(c[1].trim().matches("int")) {
						table.getMp().put(c[0].trim(),"int");
					} else if(c[1].trim().matches("double")) {
						table.getMp().put(c[0].trim(),"double");
					} else if(c[1].trim().matches("long")) {
						table.getMp().put(c[0].trim(),"long");
					} else if(c[1].trim().matches("float")) {
						table.getMp().put(c[0].trim(),"float");}
					
					else if(c[1].trim().matches("string\\([0-9]+\\)")){
						table.getMp().put(c[0].trim(), c[1].trim());
					} else {
						throw new MyException("属性类型不合法！");
					}
				}
			}
		}
		return table;
	}
	public static void main(String[] args) throws MyException {
	}

	public boolean checkInsert() throws MyException{
		String[] words = this.comm.split("\\s");
		if(words[0].toLowerCase().equals("insert")&&words[1].toLowerCase().equals("into")) {
			String name = "";
			int index=0;
			for(int i=12;i<comm.length();i++) {
				if(comm.charAt(i)=='(') {
					name=name.trim();
					index=i+1;
					break;
				}
				name+=comm.charAt(i);
			}
			
			Table t = new Table();
			System.out.println(name);
			if(!Tools.readTables().getTables().contains(t)) {
				throw new MyException("表名不存在，请检查！");
			}

			Tables tbs = Tools.readTables();
			Table table = null;
			for (Table tb : tbs.getTables()) {
				if(tb.getName().equals(name)) {
					table = tb;
					break ;
				}
			}
			String colm = "";
			for(int i=index;i<comm.length();i++) {
				if(comm.charAt(i)==')') {
					index= i+1;
					break;
				}
				colm+=comm.charAt(i);
			}
			String[] cs = colm.split(",");

			String val ="" ;
			for(int i=index;i<comm.length();i++) {
				if(comm.charAt(i)=='(') {
					index=i+1;
					break;
				}
				val+=comm.charAt(i);
			}
			val=val.trim();
			if(val.toLowerCase().equals("values")==false){
				throw new MyException("解析错误！");
			}
			String num="";
			for(int i=index;i<comm.length();i++) {
				if(comm.charAt(i)==')') {
					break;
				}
				num+= comm.charAt(i);
			}
			String[] vals=num.split(",");
			List<Object> va = new ArrayList<Object>();
			if(cs.length!=vals.length) {
				throw new MyException("插入的值不对应请检查！");
			}
			for(int i=0;i<cs.length;i++) {
				String k=table.getMp().get(cs[i].trim());
				if(null==k){
					throw new MyException("属性不存在，请检查！");
				} else {
					if(k.equals("int")) {
						if(vals[i].trim().matches("[0-9]+")==false) {
							throw new MyException("插入的值与表定义的类型不一致！插入失败");
						}else {
							va.add(Integer.valueOf(vals[i]));
						}
					}else if(k.equals("long")) {
						if(vals[i].trim().matches("[0-9]+")==false) {
							throw new MyException("插入的值与表定义的类型不一致！插入失败");
						}else {
							va.add(Long.valueOf(vals[i]));
						}
					}else if(k.equals("float")) {
						if(vals[i].trim().matches("[a-zA-Z_]+")==true) {
							throw new MyException("插入的值与表定义的类型不一致！插入失败");
						}else {
							va.add(Float.valueOf(vals[i]));
						}
					}
					else if(k.equals("double")) {
						if(vals[i].trim().matches("[a-zA-Z_]+")==true) {
							throw new MyException("插入的值与表定义的类型不一致！插入失败");
						}else {
							va.add(Double.valueOf(vals[i]));
						}
					}
					else {
						vals[i]=vals[i].trim();
						if(vals[i].charAt(0)!='\''||vals[i].charAt(vals[i].length()-1)!='\'') {
							throw new MyException("插入的值与表定义的类型不一致！插入失败");
						}
						va.add(vals[i]); 
					}

				}
			}
			Data dt = Tools.readData(name+".tb");
			if(dt==null) {
				dt = new Data();
			}
			List<Table> tbs2=Tools.readTables().getTables();
			Table tbn = null;
			for (Table tb2 : tbs2) {
				if(tb2.getName().equals(name)) {
					tbn = tb2;
					break;
				}
			}

			Set s = tbn.getMp().keySet();
			Iterator i = s.iterator();

			while(i.hasNext()){

				Object k = i.next();
				for(int j=0;j<cs.length;j++) {
					if(cs[j].trim().equals((String)k)){
						//						System.out.println("#");
						if(va.get(j) instanceof String) {
							String oi=(String)(va.get(j));
							oi=oi.replace("'", "");
							dt.getData().add(oi);
						}
						else 
							dt.getData().add(va.get(j));
						break;
					}
				}
			}

		
			Tools.writeData(dt, name+".tb");
			System.out.println("数据插入成功！");
		} else {
			throw new MyException("无法解析插入命令！");
		}
		return true;
	}
	public boolean checkDel() throws MyException{
		//delete from name where cnamm=value;
		int num=0;
		Tables tbs = Tools.readTables();
		String [] w = comm.split("\\s");
		if(w[0].trim().toLowerCase().equals("drop")==true||w[1].trim().toLowerCase().equals("table")==true||w.length==3)
		{
			if(tbs.getTables().contains(new Table(w[2].trim()))==false) {
				throw new MyException("表名错误！请检查！~");
			}
			Table t = null;
			File file = new File(w[2].trim()+".tb");
			if (file.exists()) {
				file.delete();
				System.out.println("文件已经被成功删除");
			}
		}
		if(w[0].trim().toLowerCase().equals("delete")==false) {
			throw new MyException("解析语句错误!删除失败！");
		} else if(w[1].trim().toLowerCase().equals("from")==false){
			throw new MyException("from缺失！删除失败！");
		} 
		
		if(w.length<3) {
			throw new MyException("参数不足！删除失败！");
		} else if(w.length==3)
		{
			if(tbs.getTables().contains(new Table(w[2].trim()))==false) {
				throw new MyException("表名错误！请检查！~");
			}
			Table t = null;
			File file = new File(w[2].trim()+".tb");
			if (file.exists()) {
				file.delete();
				System.out.println("文件已经被成功删除");
			}
			
			
		}else {
			if(tbs.getTables().contains(new Table(w[2].trim()))==false) {
				throw new MyException("表名错误！请检查！~");
			}
			Table t = null;
			for (Table tb : tbs.getTables()) {
				if(tb.getName().equals(w[2].trim())) {
					t = tb;
					break;
				}
			}

			if(w[3].trim().equals("where")==false) {
				throw new MyException("解析错误！");
			}
			String last = this.comm.split("where")[1].trim();
			String [] condition = last.split(",");
			int a[] = new int[t.getMp().size()+2];
			List<Object> ans = new ArrayList<Object>();
			for(int i=0;i<condition.length;i++) {
				String [] kv = condition[i].trim().split("=");
				if(kv.length!=2) {
					throw new MyException("where条件解析错误，请检查！");
				}
				kv[0]= kv[0].trim();
				kv[1]= kv[1].trim();
				if(t.getMp().get(kv[0])==null) {
					throw new MyException("列名不存在！");
				} 
				if(t.getMp().get(kv[0]).equals("int")) {
					if(kv[1].matches("[0-9]+")==false) {
						throw new MyException("与定义列的属性不符，请检查！");
					} else {
						///code
						Set s = t.getMp().keySet();
						Iterator it = s.iterator();
						int de=0;

						while(it.hasNext()){
							Object k = it.next();
							String u = (String)k;
							if(u.equals(kv[0])) {
								a[de]=1;
								ans.add(kv[1]);
								break;
							}
							de++;
						}

					}
				} else	if(t.getMp().get(kv[0]).equals("long")) {
					if(kv[1].matches("[0-9]+")==false) {
						throw new MyException("与定义列的属性不符，请检查！");
					} else {
						///code
						Set s = t.getMp().keySet();
						Iterator it = s.iterator();
						int de=0;

						while(it.hasNext()){
							Object k = it.next();
							String u = (String)k;
							if(u.equals(kv[0])) {
								a[de]=1;
								ans.add(kv[1]);
								break;
							}
							de++;
						}

					}
				}
				else	if(t.getMp().get(kv[0]).equals("double")) {
					if(kv[1].matches("[a-zA-Z_]+")==true) {
						throw new MyException("与定义列的属性不符，请检查！");
					} else {
						///code
						Set s = t.getMp().keySet();
						Iterator it = s.iterator();
						int de=0;

						while(it.hasNext()){
							Object k = it.next();
							String u = (String)k;
							if(u.equals(kv[0])) {
								a[de]=1;
								ans.add(kv[1]);
								break;
							}
							de++;
						}

					}
				}
				else	if(t.getMp().get(kv[0]).equals("float")) {
					if(kv[1].matches("[a-zA-Z_]+")==true) {
						throw new MyException("与定义列的属性不符，请检查！");
					} else {
						///code
						Set s = t.getMp().keySet();
						Iterator it = s.iterator();
						int de=0;

						while(it.hasNext()){
							Object k = it.next();
							String u = (String)k;
							if(u.equals(kv[0])) {
								a[de]=1;
								ans.add(kv[1]);
								break;
							}
							de++;
						}

					}
				}
				else {
					if(!(kv[1].charAt(0)=='\''&&kv[1].charAt(kv[1].length()-1)=='\'')){
						throw new MyException("与定义列的属性不符，请检查！");
					} else {
						kv[1]=kv[1].replace("'", "");
						//code
						Set s = t.getMp().keySet();
						Iterator it = s.iterator();
						int de=0;

						while(it.hasNext()){
							Object k = it.next();
							String u = (String)k;
							if(u.equals(kv[0])) {
								if(a[de]==1) {
									throw new MyException("解析错误！");
								}
								a[de]=1;
								ans.add(kv[1]);
								break;
							}
							de++;
						}
					}

				}
			}
			Data dt = Tools.readData(t.getName()+".tb");
			int [] stack = new int[333];
			int top=-1;
			for(int i=0;i<dt.getData().size();i+=t.getMp().size()) {
				int ind=0;
				int ret=0;
				for(int j=i;j<i+t.getMp().size();j++) {
					if(a[j-i]==1) {
						if(dt.getData().get(j) instanceof Integer ) {

							if(((Integer)dt.getData().get(j)).equals(Integer.valueOf((String)ans.get(ind)))){
								ret++;

							}
						}
						if(dt.getData().get(j) instanceof String) {
							if(((String)(dt.getData().get(j))).equals((String)ans.get(ind))){
								ret++;
							}
						}
						ind++;
					}
				}
				if(ret==ans.size()){
					stack[++top] = i/t.getMp().size();
				}
			}
			int yu=0;
		    num=(top+1);
			for(int i=0;i<dt.getData().size();i+=t.getMp().size()) {
				if(yu<=top&&stack[yu]==(int)(i/t.getMp().size())) {
					for(int j=i;j<i+t.getMp().size();j++){
						dt.getData().set(j, null);
					}
					yu++;
				}
			}
		    List<Integer> nullArr = new ArrayList<Integer>();  
			nullArr.add(null);  
			dt.getData().removeAll(nullArr);
			Tools.writeData(dt,t.getName()+".tb");

		}

		System.out.println(num+" 条记录删除成功！");
		return true;
	}
	public boolean checkSelect() throws MyException{
		String[] w=comm.split("\\s");
		if(w[0].trim().toLowerCase().equals("select")==false) {
			throw new MyException("解析错误，请检查！");
		}
		if(w[1].trim().equals("*")){
			if(w[2].trim().equals("from")==false) {
				throw new MyException("解析错误，请检查！");
			}
			if(Tools.readTables().getTables().contains(new Table(w[3].trim()))==false) {
				throw new MyException("表不存在，请检查！");
			}

			Table tb = null;
			for (Table	t : Tools.readTables().getTables()) {
				if(t.getName().equals(w[3].trim())) {
					tb = t;
					break;
				}
			}

	
			Set<?> s = tb.getMp().keySet();
			Iterator<?> ii = s.iterator();
			System.out.println(w[3]+" : ");
			while(ii.hasNext()){
				Object k = ii.next();
				System.out.print(k+"     ");
			}
			System.out.println();
			Data dt = Tools.readData(w[3].trim()+".tb");
			int size=dt.getData().size();
			for(int i=0;i<size;i++) {
				System.out.print(dt.getData().get(i)+"   ");
				if((i+1)%tb.getMp().size()==0&&i!=0) {
					System.out.println();
				}
			}

		} else {
			String[] cls = comm.split("from");
			if(cls.length<2) {
				throw new MyException("解析错误，缺失from！");
			}

			String tnm = cls[1].trim().split("\\s")[0].trim();
			Tables ts = Tools.readTables();
			if(ts.getTables().contains(new Table(tnm))==false) {
				throw new MyException("表不存在！");
			}
			Table t = new Table();
			cls[0] = cls[0].substring(7).trim();
			String [] p = cls[0].split(","); 
			for(int i=0;i<ts.getTables().size();i++) {
				if(ts.getTables().get(i).getName().equals(tnm)) {
					t = ts.getTables().get(i);
					break;
				}
			}
			for(int i=0;i<p.length;i++) {
				if(t.getMp().get(p[i].trim())==null) {
					System.out.print(p[i].trim());
					throw new MyException(":列名不存在，请检查！");
				}
			}
			Set<?> s = t.getMp().keySet();
			Iterator<?> ii = s.iterator();
			System.out.println(tnm+" : ");
			int[]  a = new int[22];
			int ui=0;
			while(ii.hasNext()){
				Object k = ii.next();
				for(int i=0;i<p.length;i++) {
					if(p[i].trim().equals((String)k)){
						System.out.print(p[i].trim()+"       ");
						a[ui]=1;
						break;
					}
				}
				ui++;
			}
			System.out.println("");
			Data dt = Tools.readData(tnm.trim()+".tb");
			int size=dt.getData().size();
			for(int i=0;i<size;i++) {
				if(a[i%t.getMp().size()]==1){
					System.out.print(dt.getData().get(i)+"   ");
				}
				if((i+1)%t.getMp().size()==0&&i!=0) {
					System.out.println();
				}
			}



		}
		return false;
	}

	public boolean checkUpdate() throws MyException
	{
		String[] w=comm.split("\\s");
		if(w[0].trim().toLowerCase().equals("update")==false)
		{
			throw new MyException("解析错误，请检查！");
		}

		Table tb = null;
		for (Table	t : Tools.readTables().getTables()) {
			if(t.getName().equals(w[1].trim())) {
				tb = t;
				break;
			}
		}
		if(w[0].trim().toLowerCase().equals("set")==false)
		{
			throw new MyException("解析错误，请检查！");
		}
		String[] tem=comm.split("set");

		 
		return false;
		
	}

	public String getComm() {
		return comm;
	}
	public void setComm(String comm) {
		this.comm = comm;
	}
}

