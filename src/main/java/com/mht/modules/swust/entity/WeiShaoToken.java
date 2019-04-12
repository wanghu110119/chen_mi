package com.mht.modules.swust.entity;

import org.json.JSONObject;

public class WeiShaoToken {

	/**
	 * 请求令牌内容
	 */
	private String token;
	/**
	 * 过期时间(秒)
	 */
	private long expiresTime;
	
	private String scope;
	/**
	 * 刷新token的依据
	 */
	private String refreshToken;
	
	private String tokenType;
	
	private String appKey;
	
	private String appSecret;
	
	private long updateTime;
	
	
	public WeiShaoToken(){}
	
	public WeiShaoToken(org.json.JSONObject jsonObject){
		this.token = jsonObject.getString("access_token");
		this.expiresTime = jsonObject.getLong("expires_in");
		this.scope = jsonObject.getString("scope");
		this.refreshToken = jsonObject.getString("refresh_token");
		this.tokenType = jsonObject.getString("token_type");
	}
	
	public void setData(JSONObject json){
		this.token = json.getString("access_token");
		this.expiresTime = json.getLong("expires_in");
		this.scope = json.getString("scope");
		this.refreshToken = json.getString("refresh_token");
		this.tokenType = json.getString("token_type");
		this.updateTime = System.currentTimeMillis();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getExpiresTime() {
		return expiresTime;
	}

	public void setExpiresTime(long expiresTime) {
		this.expiresTime = expiresTime;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
}
