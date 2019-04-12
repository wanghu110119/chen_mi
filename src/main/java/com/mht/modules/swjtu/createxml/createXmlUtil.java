package com.mht.modules.swjtu.createxml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.output.ByteArrayOutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class createXmlUtil {
/**
 * 1. 查询.已经存在的对象信息.不修改
 * @Title: seacherCreateXml  
 * @Description: TODO
 * @return String
 * @author zhaolichao
 */
	public static String seacherCreateXml() { 
		String xmlStr = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        document.setXmlStandalone(true);
        document.setXmlVersion("1.0");
        
        Element root = document.createElement("root");
        document.appendChild(root);
        
        Element itemStatistics = document.createElement("fix");
		itemStatistics.setTextContent(" ");
		root.appendChild(itemStatistics);
        
        Element cmd = document.createElement("cmd");
        cmd.setAttribute("Id", "1");
        cmd.setAttribute("uid", "admin");
        cmd.setAttribute("pwd", "");
        
        Element TreeRoot = document.createElement("TreeRoot");
        TreeRoot.setAttribute("ClassId", "0");
        TreeRoot.setAttribute("Id", "0");
        TreeRoot.setAttribute("GetDoor", "1");
        
        Element ClassId = document.createElement("ClassId");
        ClassId.setTextContent("3/63/59/62/61/58/187");
        TreeRoot.appendChild(ClassId);
        
        cmd.appendChild(TreeRoot);
        root.appendChild(cmd);
        
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transFormer = transFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        transFormer.transform(domSource, new StreamResult(bos));
        xmlStr = bos.toString();
      
    } catch (ParserConfigurationException e) {
        e.printStackTrace();
    }catch (TransformerConfigurationException e) {
        e.printStackTrace();
    }catch (TransformerException e) {
        e.printStackTrace();
    }catch (Exception e) {
        e.printStackTrace();
    }
    return xmlStr;
	}

	
	/**
	 *  创建人员到指定人员组.修改人员组ID
	 * @Title: createUserXml 
	 * @Description: TODO
	 * @return String
	 * @author zhaolichao
	 * 
	 * <?xml version="1.0" encoding="utf-8" ?>
<root>
  <fix></fix>
   <cmd Id="6" uid="admin" pwd="" >
      <ParentClassId>59</ParentClassId>
      <ParentId>16</ParentId>
      <Id>-1</Id>
      <Type>0</Type>
      <Name> 张三1</Name>
      <Sex>0</Sex>
      <SocialSecurity>12345678901234567X </SocialSecurity>
      <Employee> GH_001</Employee>
      <MainCard>3000645</MainCard>
      <BeginDate>2015-07-25</BeginDate>
      <ExpireDate>2018-07-25</ExpireDate>
  </cmd>
</root>
	 */
	public static String createUserXml() { 
		String xmlStr = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        document.setXmlStandalone(true);
        document.setXmlVersion("1.0");
        
        Element root = document.createElement("root");
        document.appendChild(root);
        
        Element itemStatistics = document.createElement("fix");
		itemStatistics.setTextContent(" ");
		root.appendChild(itemStatistics);
        
        Element cmd = document.createElement("cmd");
        cmd.setAttribute("Id", "6");
        cmd.setAttribute("uid", "admin");
        cmd.setAttribute("pwd", "");
        
        Element ParentClassId = document.createElement("ParentClassId");
        ParentClassId.setTextContent("59");
        cmd.appendChild(ParentClassId);
        
        Element ParentId = document.createElement("ParentId");
        ParentId.setTextContent("5");
        cmd.appendChild(ParentId);
        
        Element Id = document.createElement("Id");
        Id.setTextContent("-1");
        cmd.appendChild(Id);
        
        Element Type = document.createElement("Type");
        Type.setTextContent("0");
        cmd.appendChild(Type);
        
        Element Name = document.createElement("Name");
        Name.setTextContent("李四Q");
        cmd.appendChild(Name);
        
        Element Sex = document.createElement("Sex");
        Sex.setTextContent("0");
        cmd.appendChild(Sex);
        
        Element SocialSecurity = document.createElement("SocialSecurity");
        SocialSecurity.setTextContent("12345678801234567X");
        cmd.appendChild(SocialSecurity);
        
        Element Employee = document.createElement("Employee");
        Employee.setTextContent("GH_201");
        cmd.appendChild(Employee);
        
        Element MainCard = document.createElement("MainCard");
        MainCard.setTextContent("3000659");
        cmd.appendChild(MainCard);
        
        Element BeginDate = document.createElement("BeginDate");
        BeginDate.setTextContent("2015-07-25");
        cmd.appendChild(BeginDate);
        
        Element ExpireDate = document.createElement("ExpireDate");
        ExpireDate.setTextContent("2020-07-25");
        cmd.appendChild(ExpireDate);
        
      
        root.appendChild(cmd);
        
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transFormer = transFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        transFormer.transform(domSource, new StreamResult(bos));
        xmlStr = bos.toString();
      
    } catch (ParserConfigurationException e) {
        e.printStackTrace();
    }catch (TransformerConfigurationException e) {
        e.printStackTrace();
    }catch (TransformerException e) {
        e.printStackTrace();
    }catch (Exception e) {
        e.printStackTrace();
    }
    return xmlStr;
	}
/**
 * byte转HexString
 * 
 * @Title: printHexString 
 * @Description: TODO
 * @param b
 * @param len
 * @return String
 * @author zhaolichao
 */
	public static  String printHexString( byte[] b,int len) {
		String hexString = "";
		  for (int i = 0; i < len; i++) { 
		    String hex = Integer.toHexString(b[i] & 0xFF); 
		    if (hex.length() == 1) { 
		      hex = '0' + hex; 
		    }
		    hexString = hexString+hex;
		  } 
		       return hexString;
		}
	
	 public static String bytesToHexString(byte[] src,int len){
	        StringBuilder stringBuilder = new StringBuilder("");
	        if (src == null || len <= 0) {
	            return null;
	        }
	        for (int i = 0; i < len; i++) {
	            int v = src[i] & 0xFF;
	            String hv = Integer.toHexString(v);
	            if (hv.length() < 2) {
	                stringBuilder.append(0);
	            }
	            stringBuilder.append(hv);
	        }
	        return stringBuilder.toString();
	    }
	
	
	public static void main(String[] args) {
//		String xmlString=seacherCreateXml();
//		String xmlString=createUserXml();
//		System.out.println(xmlString);
		
		
		
		
		
		
		ServerSocket s =null;
		Socket socket=null;
		try {
			s=new ServerSocket(8000);
			System.out.println("ServerSocket Start:"+s);
			socket=s.accept();
			System.out.println(socket);
			String bufstr="";
			String bufstring="";
//			while(true){
				//由于接收客户发来的请求
				InputStream input=socket.getInputStream();
				byte[] buf =new byte[1024];
				if(input!=null){
					int len=input.read(buf);
//					System.out.println("服务器端收到的报文：\n"+buf+"---------"+len);
					bufstr=printHexString(buf,len).toString();
					bufstring=bytesToHexString(buf,len);
					System.out.println("服务器端收到的报文bufstr："+bufstr);
//					System.out.println("服务器端收到的报文bufstring："+bufstring);
				}
//			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}