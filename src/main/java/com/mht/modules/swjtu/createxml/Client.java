package com.mht.modules.swjtu.createxml;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Arrays;

import org.junit.Test;


public class Client {  
	   
	   public static void main(String args[]) throws Exception {  
	      //为了简单起见，所有的异常都直接往外抛  
	      String host = "127.0.0.1";  //要连接的服务端IP地址  
	      int port = 8000;   //要连接的服务端对应的监听端口  
	      //与服务端建立连接  
	      Socket client = new Socket(host, port);  
	      //建立连接后就可以往服务端写数据了  
	      Writer writer = new OutputStreamWriter(client.getOutputStream(),"UTF-8");  
	     /* writer.write("<?xml version='1.0' encoding='utf-8' ?>"
	      		+ "<root>   <cmd Id='13' uid='admin' pwd='' >	"
	      		+ "<DeviceId ClassId='-1' Enable='1'>-1</DeviceId> "
	      		+ "</cmd></root>");  */
	      writer.write(createXmlUtil.seacherCreateXml());
	      writer.flush();//写完后要记得flush  
	      writer.close();  
	      client.close();  
	   }  
	   @Test
	   public  void Test1 (){
		   int[] a = {1,23,334,234,1321,5435,3213,4343,11,232,554};
		   //比较排序
		   for (int i = 0; i < a.length; i++) {
			for (int j = i; j < a.length; j++) {
				if(a[i]>a[j]){
					int temp;
					temp=a[i];
					a[i]=a[j];
					a[j]=temp;
				}
			}
		}
		   System.out.println(Arrays.toString(a));
		   
		   for (int i = 0; i < a.length-1; i++) {
				for (int j = i; j < a.length-i-1; j++) {
					if(a[j]>a[j+1]){
						int temp;
						temp=a[j+1];
						a[j+1]=a[j];
						a[j]=temp;
					}
				}
			}
		   System.out.println(Arrays.toString(a));
	   }
	   
	   
	     
	} 




