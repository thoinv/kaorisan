package com.kaorisan.beans;

public class Request {
	private String title;
	private String nameSender;
	private String dateCreate;
	private  boolean isAssistant;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getNameSender() {
		return nameSender;
	}
	public void setNameSender(String nameSender) {
		this.nameSender = nameSender;
	}
	public String getDateCreate() {
		return dateCreate;
	}
	public void setDateCreate(String dateCreate) {
		this.dateCreate = dateCreate;
	}
	public boolean isAssistant() {
		return isAssistant;
	}
	public void setAssistant(boolean isAssistant) {
		this.isAssistant = isAssistant;
	}
}
