package com.mht.modules.swust.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class HttpsUtil {

	private static class TrustAnyTrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	/**
	 * post方式请求服务器(https协议)
	 * 
	 * @param url
	 *            请求地址
	 * @param content
	 *            参数
	 * @param charset
	 *            编码
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws IOException
	 */
	static Logger logger = LogManager.getLogger(HttpsUtil.class.getName());
	
	public static String post(String url, String content, String charset)
			throws NoSuchAlgorithmException, KeyManagementException, IOException {
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
		logger.info("request.uri=" + url);
		URL console = new URL(url);
		HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
		conn.setSSLSocketFactory(sc.getSocketFactory());
		conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
		conn.connect();
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.write(content.getBytes(charset));
		// 刷新、关闭
		out.flush();
		out.close();
		Integer x = conn.getResponseCode();
		logger.info("response code =" + x);
		if (x == 200) {
			InputStream is = conn.getInputStream();
			if (is != null) {
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
				is.close();
				logger.info("response text = " + outStream.toString());
				return outStream.toString();
			}
		}
		return null;
	}
	
	public static String get(String path) throws Exception{
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
		BufferedReader in=null;
		HttpsURLConnection conn=null;
        try {
        	URL console = new URL(path);
    		conn = (HttpsURLConnection) console.openConnection();
    		conn.setSSLSocketFactory(sc.getSocketFactory());
    		conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
    		conn.setDoInput(true);
    		conn.setDoOutput(true);
    		conn.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
//    		conn.setRequestProperty("Charset", "utf-8"); // 设置编码
    		conn.connect();
            //读取响应
            if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                StringBuffer content=new StringBuffer();
                String tempStr="";
                in=new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
                while((tempStr=in.readLine())!=null){
                    content.append(tempStr);
                }
                return content.toString();
            }else{
//                throw new Exception("请求出现了问题!");
            	System.err.println("conn.getResponseCode(): "+conn.getResponseCode());
            	return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        	if(in != null){
        		in.close();        		
        	}
        	if(conn != null){
        		conn.disconnect();        		
        	}
        }
        return null;
    }

}
