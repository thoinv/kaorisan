package com.kaorisan.dataLayer;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;

import com.kaorisan.beans.AccountDashBoard;
import com.kaorisan.beans.Attachment;
import com.kaorisan.beans.AttachmentTmp;
import com.kaorisan.beans.DashBoard;
import com.kaorisan.beans.RecommendTask;
import com.kaorisan.beans.Task;
import com.kaorisan.beans.User;

public class CacheData {
	private static CacheData instant = null;
	ProgressDialog currentProgressDialog = null;
	public static CacheData getInstant() {
		if (instant == null) {
			instant = new CacheData();
			
		}
		return instant;
	}
	
	private ArrayList<RecommendTask> recommendTasks = new ArrayList<RecommendTask>();
	
	private AccountDashBoard accountDashBoard = null;
	
	private User currentUser = null;
	private List<Task> listTask = null;
	
	public List<Task> getListTask() {
		return listTask;
	}

	public void setListTask(List<Task> listTask) {
		this.listTask = listTask;
	}

	private DashBoard dashBoard = null;
	
	private ArrayList<Attachment> listAttachment = new ArrayList<Attachment>();
	
	private ArrayList<Attachment> listAttachmentPicture = new ArrayList<Attachment>();
	
	private ArrayList<Attachment> listAttachmentAudio = new ArrayList<Attachment>();
	
	private ArrayList<AttachmentTmp> listAttachmentTmps = new ArrayList<AttachmentTmp>();
	
	public DashBoard getDashBoard() {
		return dashBoard;
	}

	public void setDashBoard(DashBoard dashBoard) {
		this.dashBoard = dashBoard;
	}

	public String urlFaceBook ="";
	
	public String tokenKaorisan = "";
	
	private Task currentTask = null;
	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public String getUrlFaceBook() {
		return urlFaceBook;
	}

	public void setUrlFaceBook(String urlFaceBook) {
		this.urlFaceBook = urlFaceBook;
	}

	public Task getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(Task currentTask) {
		this.currentTask = currentTask;
	}

	public String getTokenKaorisan() {
		return tokenKaorisan;
	}

	public void setTokenKaorisan(String tokenKaorisan) {
		this.tokenKaorisan = tokenKaorisan;
	}

	public ArrayList<Attachment> getListAttachment() {
		return listAttachment;
	}

	public void setListAttachment(ArrayList<Attachment> listAttachment) {
		this.listAttachment = listAttachment;
	}

	public AccountDashBoard getAccountDashBoard() {
		return accountDashBoard;
	}

	public void setAccountDashBoard(AccountDashBoard accountDashBoard) {
		this.accountDashBoard = accountDashBoard;
	}

	public ArrayList<Attachment> getListAttachmentPicture() {
		return listAttachmentPicture;
	}

	public void setListAttachmentPicture(ArrayList<Attachment> listAttachmentPicture) {
		this.listAttachmentPicture = listAttachmentPicture;
	}

	public ArrayList<Attachment> getListAttachmentAudio() {
		return listAttachmentAudio;
	}

	public void setListAttachmentAudio(ArrayList<Attachment> listAttachmentAudio) {
		this.listAttachmentAudio = listAttachmentAudio;
	}

	public ProgressDialog getCurrentProgressDialog() {
		return currentProgressDialog;
	}

	public void setCurrentProgressDialog(ProgressDialog currentProgressDialog) {
		this.currentProgressDialog = currentProgressDialog;
	}

	public ArrayList<AttachmentTmp> getListAttachmentTmps() {
		return listAttachmentTmps;
	}

	public void setListAttachmentTmps(ArrayList<AttachmentTmp> listAttachmentTmps) {
		this.listAttachmentTmps = listAttachmentTmps;
	}

	public ArrayList<RecommendTask> getRecommendTasks() {
		return recommendTasks;
	}

	public void setRecommendTasks(ArrayList<RecommendTask> recommendTasks) {
		this.recommendTasks = recommendTasks;
	}
	
	
}
