package com.kaorisan.beans;

import java.util.Date;

public class Reply {

	private int id;
	private String body;

	private String createdAt;

	private boolean isAssistant;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isAssistant() {
		return isAssistant;
	}

	public long getTimeCreate() {
		return new Date().getTime() - Integer.parseInt(getCreatedAt());
	}

	public void setAssistant(boolean isAssistant) {
		this.isAssistant = isAssistant;
	}

}
