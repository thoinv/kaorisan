package com.kaorisan.beans;

import java.util.ArrayList;


public class Task {
	private int id;
	private String title;
	private String request;
	private String workflowState;
	private int repliesCount;
	private String createdAt;
	private String completedAt;
	private String updateAt;
	private boolean rated;
	private String urlAudio;
	private ArrayList<Reply> listReply = new ArrayList<Reply>();
	
	private ArrayList<Picture> liPictures = new ArrayList<Picture>();
	
	private ArrayList<Audio> listAudios = new ArrayList<Audio>();
	
//	private ArrayList<Attachment> listAttachments = new ArrayList<Attachment>();
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWorkflowState() {
		return workflowState;
	}
	public void setWorkflowState(String workflowState) {
		this.workflowState = workflowState;
	}
	public int getRepliesCount() {
		return repliesCount;
	}
	public void setRepliesCount(int repliesCount) {
		this.repliesCount = repliesCount;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getCompletedAt() {
		return completedAt;
	}
	public void setCompletedAt(String completedAt) {
		this.completedAt = completedAt;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public ArrayList<Reply> getListReply() {
		return listReply;
	}
	public void setListReply(ArrayList<Reply> listReply) {
		this.listReply = listReply;
	}
	public ArrayList<Picture> getLiPictures() {
		return liPictures;
	}
	public void setLiPictures(ArrayList<Picture> liPictures) {
		this.liPictures = liPictures;
	}
	public ArrayList<Audio> getListAudios() {
		return listAudios;
	}
	public void setListAudios(ArrayList<Audio> listAudios) {
		this.listAudios = listAudios;
	}
	public boolean isRated() {
		return rated;
	}
	public void setRated(boolean rated) {
		this.rated = rated;
	}
	public String getUrlAudio() {
		return urlAudio;
	}
	public void setUrlAudio(String urlAudio) {
		this.urlAudio = urlAudio;
	}
//	public ArrayList<Attachment> getListAttachments() {
//		return listAttachments;
//	}
//	public void setListAttachments(ArrayList<Attachment> listAttachments) {
//		this.listAttachments = listAttachments;
//	}
	public String getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(String updateAt) {
		this.updateAt = updateAt;
	}
	
}
