package com.kaorisan.beans;

public class User {
	private String id;
	private String fullName;
	private String email;
	private String avatar;
	private String googleToken;
	private String plan;
	private String googleRefreshToken;
	private String googleExpDate;
	private String pushToken;
	private String pushable;
	
	public String getGoogleToken() {
		return googleToken;
	}
	
	public void setGoogleToken(String googleToken) {
		this.googleToken = googleToken;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getGoogleRefreshToken() {
		return googleRefreshToken;
	}

	public void setGoogleRefreshToken(String googleRefreshToken) {
		this.googleRefreshToken = googleRefreshToken;
	}

	public String getGoogleExpDate() {
		return googleExpDate;
	}

	public void setGoogleExpDate(String googleExpDate) {
		this.googleExpDate = googleExpDate;
	}

	public String getPushToken() {
		return pushToken;
	}

	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}

	public String getPushable() {
		return pushable;
	}

	public void setPushable(String pushable) {
		this.pushable = pushable;
	}
}
