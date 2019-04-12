package com.mht.modules.swjtu.createxml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
public class XMLHandler {
    public XMLHandler(){
        
    }
    
    public String createXML(){
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
            
//            Element operatorNokia = document.createElement("operator");
//            operatorNokia.setTextContent("CMCC");
//            TreeRoot.appendChild(operatorNokia);
            
            cmd.appendChild(TreeRoot);
            
//            Element xiaomi = document.createElement("type");
//            xiaomi.setAttribute("name", "xiaomi");
//            
//            Element priceXiaoMi = document.createElement("price");
//            priceXiaoMi.setTextContent("699");
//            xiaomi.appendChild(priceXiaoMi);
//            
//            Element operatorXiaoMi = document.createElement("operator");
//            operatorXiaoMi.setTextContent("ChinaNet");
//            xiaomi.appendChild(operatorXiaoMi);
            
//            cmd.appendChild(xiaomi);
            
            root.appendChild(cmd);
            
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transFormer = transFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            
            //export string
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            transFormer.transform(domSource, new StreamResult(bos));
            xmlStr = bos.toString();
            
            //save as file
           /* File file = new File("TelePhone.xml");
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            StreamResult xmlResult = new StreamResult(out);
            transFormer.transform(domSource, xmlResult);*/
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
    
    public void parserXML(String strXML){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            StringReader sr = new StringReader(strXML);
            InputSource is = new InputSource(sr);
            Document doc = builder.parse(is);
            Element rootElement = doc.getDocumentElement();
            NodeList phones = rootElement.getElementsByTagName("type");
            for (int i = 0; i < phones.getLength(); i++) {
                Node type = phones.item(i);
                String phoneName = ((Element)type).getAttribute("name");
                System.out.println("Phone name = "+phoneName);
                NodeList properties = type.getChildNodes();
                for (int j = 0; j < properties.getLength(); j++) {
                    Node property = properties.item(j);
                    String nodeName = property.getNodeName();
                    if (nodeName.equals("price")) {
                        String price=property.getFirstChild().getNodeValue();
                        System.out.println("price="+price);
                    } else if (nodeName.equals("operator")) {
                        String operator=property.getFirstChild().getNodeValue();
                        System.out.println("operator="+operator);
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        XMLHandler handler = new XMLHandler();
        String xml = handler.createXML();
        System.out.println(xml);
//        handler.parserXML(xml);
    }
}