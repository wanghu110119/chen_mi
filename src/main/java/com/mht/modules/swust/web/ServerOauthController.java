package com.mht.modules.swust.web;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.oltu.oauth2.client.HttpClient;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mht.common.config.Global;
import com.mht.modules.swust.entity.WeiShaoToken;
import com.mht.modules.swust.utils.HttpsUtil;



@RequestMapping("/api/swust/meeting")
@Controller
public class ServerOauthController {
	
	protected HttpClient httpClient;
	
	public static String basehost = "https://api.weishao.com.cn";
	
private static final String APPLICATION_JSON = "application/json";
    
    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";
	
	String clientId = null;

	String clientSecret = null;

	String accessTokenUrl = null;

	String userInfoUrl = null;

	String redirectUrl = null;

	String response_type = null;

	String code = null;

	// 提交申请code的请求
//requestServerCode
	@RequestMapping("requestServerCode")
	public  String  requestServerFirst(HttpServletRequest httpRequest)
			throws OAuthProblemException {
//		System.out.println("-----------客户端/requestServerCode--------------------------------------------------------------------------------");
		clientId = "a1df9a1985bc3b85";
		clientSecret = "86149db2bcac4ba5df2bb3cd296a2328";
		accessTokenUrl = "https://api.weishao.com.cn/oauth/authorize";
		redirectUrl = "http://"+Global.getConfig("hostAddress")+":"+httpRequest.getServerPort()+httpRequest.getContextPath()+"/api/swust/meeting/index";
		// redirectUrl = "http://localhost:8081/oauthclient01/server/callbackCode";
		response_type = "code";
		OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
		String requestUrl = null;
		try {
			// 构建oauthd的请求。设置请求服务地址（accessTokenUrl）、clientId、response_type、redirectUrl
			OAuthClientRequest accessTokenRequest = OAuthClientRequest.authorizationLocation(accessTokenUrl)
					.setResponseType(response_type)
					.setRedirectURI(redirectUrl)
					.setClientId(clientId)
					.setParameter("scope", "base_api")
					.setParameter("state", "123")
//					.setParameter("grant_type", "authorization_code")
//					.setParameter("app_key", "a1df9a1985bc3b85")
//					.setParameter("app_secret", "86149db2bcac4ba5df2bb3cd296a2328")
					.buildQueryMessage();
			requestUrl = accessTokenRequest.getLocationUri();
//			System.out.println("redirect:" +requestUrl);
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		}
		return "redirect:" + requestUrl;
		// return "redirect:http://localhost:8082/oauthserver/"+requestUrl ;
	}
	
	@RequestMapping("/index")
	public Object getToken(HttpServletRequest httpRequest,HttpServletResponse response) throws OAuthProblemException, KeyManagementException, NoSuchAlgorithmException, IOException {
//		System.out.println("-----------客户端/index--------------------------------------------------------------------------------");
		accessTokenUrl = "https://api.weishao.com.cn/oauth/token";
		userInfoUrl = "userInfoUrl";
		redirectUrl = httpRequest.getScheme()+"://"+Global.getConfig("hostAddress")+":"+httpRequest.getServerPort()+httpRequest.getContextPath()+"/api/swust/meeting/accessToken";
		code = httpRequest.getParameter("code");
//		System.out.println(code);
			JSONObject ajson = new JSONObject();
			ajson.put("grant_type", "authorization_code");
			ajson.put("app_key", "a1df9a1985bc3b85");
			ajson.put("app_secret", "86149db2bcac4ba5df2bb3cd296a2328");
			ajson.put("code", code);
			ajson.put("redirect_uri", httpRequest.getScheme()+"://"+Global.getConfig("hostAddress")+":"+httpRequest.getServerPort()+httpRequest.getContextPath()+"/api/swust/meeting/index");
//			System.out.println(String.format("post: %s", ajson.toString()));
			String str = HttpsUtil.post(basehost + "/oauth/token", ajson.toString(), "UTF-8");
			if (str == null) {
				return null;
			}else{
				ajson = new JSONObject(str);
			}
			WeiShaoToken token = new WeiShaoToken(new JSONObject(str));
			String json = ajson.toString();
			String string = "";
			try {
				 string = HttpsUtil.get(basehost + "/userinfo?access_token=" + token.getToken());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
			if (string == null) {
				return null;
			}
			JSONObject jsonEntity = new JSONObject(string);
			// 获取服务端返回过来的access token
			return "redirect:"+httpRequest.getScheme()+"://"+Global.getConfig("hostAddress")+":"+httpRequest.getServerPort()+httpRequest.getContextPath()+"/a/cas?username="+jsonEntity.get("student_number")+"&code="+jsonEntity.get("student_number") ;
	}
	
	
	// 接受客户端返回的code，提交申请access token的请求
	@RequestMapping("/index1")
	public Object toLogin(HttpServletRequest httpRequest,HttpServletResponse response) throws OAuthProblemException {
//		System.out.println("-----------客户端/index--------------------------------------------------------------------------------");
		clientId = "a1df9a1985bc3b85";
		clientSecret = "86149db2bcac4ba5df2bb3cd296a2328";
//		accessTokenUrl = "http://localhost:8082/oauthserver/responseAccessToken";https://api.weishao.com.cn/oauth/token
		accessTokenUrl = "https://api.weishao.com.cn/oauth/token";
		userInfoUrl = "userInfoUrl";
//		redirectUrl = "http://localhost:8081/oauthclient01/server/accessToken";
		redirectUrl = "http://202.115.160.151:8077/mht_oeg/api/swust/meeting/accessToken";
		code = httpRequest.getParameter("code");
//		System.out.println(code);
		OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
		try {
			OAuthClientRequest accessTokenRequest = OAuthClientRequest.tokenLocation(accessTokenUrl)
					.setParameter("grant_type", "authorization_code")
					.setParameter("app_key", "a1df9a1985bc3b85")
					.setParameter("app_secret", "86149db2bcac4ba5df2bb3cd296a2328")
					.setCode(code)
					.setRedirectURI(redirectUrl)
					.buildQueryMessage();
			accessTokenRequest.addHeader("Content-Type", "application/json");
			accessTokenRequest.addHeader("Accept", "application/json");
			// 去服务端请求access token，并返回响应
			 Map<String, String> headers = new HashMap<String, String>();
		        headers.put(OAuth.HeaderType.CONTENT_TYPE, OAuth.ContentType.JSON);
//		        System.out.println(accessTokenRequest.getLocationUri());
			OAuthAccessTokenResponse oAuthResponse =
					httpClient.execute(accessTokenRequest, headers, OAuth.HttpMethod.POST, OAuthJSONAccessTokenResponse.class);
//			oAuthClient.accessToken(accessTokenRequest, OAuth.HttpMethod.POST);
			// 获取服务端返回过来的access token
//			System.out.println("================获取到token=====================");
			String accessToken = oAuthResponse.getAccessToken();
			// 查看access token是否过期
			Long expiresIn = oAuthResponse.getExpiresIn();
//			System.out.println("客户端/callbackCode方法的token：：：" + accessToken);
//			System.out.println("-----------客户端/callbackCode--------------------------------------------------------------------------------");
			// return"redirect:http://localhost:8081/oauthclient01/server/accessToken?accessToken="+accessToken;
			return "redirect:https://api.weishao.com.cn/userinfo?accessToken=" + accessToken;
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	   public static void main(String[] args) {
		   ServerOauthController a = new ServerOauthController();
//		   try {
//			a. requestServerFirst();
//		} catch (OAuthProblemException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	   
	   
	   
	
}
