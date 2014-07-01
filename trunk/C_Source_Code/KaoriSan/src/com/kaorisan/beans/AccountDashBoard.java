package com.kaorisan.beans;

public class AccountDashBoard {
	private int id;
	private String name;
	private String planName;
	private int createAt;
	private int userRequest;
	private int totalRequest;
	private String email;
	private String avatar;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public int getCreateAt() {
		return createAt;
	}

	public void setCreateAt(int createAt) {
		this.createAt = createAt;
	}

	public int getUserRequest() {
		return userRequest;
	}

	public void setUserRequest(int userRequest) {
		this.userRequest = userRequest;
	}

	public int getTotalRequest() {
		return totalRequest;
	}

	public void setTotalRequest(int totalRequest) {
		this.totalRequest = totalRequest;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}
