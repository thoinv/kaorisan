package com.kaorisan.beans;

public class AttachmentTmp {

	private String fileName;
	
	private String filePath;
	
	private String type;
	
	private boolean isCamera;
	
	private int isLocal = 0;
	
	private boolean isResize = false;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isCamera() {
		return isCamera;
	}

	public void setCamera(boolean isCamera) {
		this.isCamera = isCamera;
	}

	public int getIsLocal() {
		return isLocal;
	}

	public void setIsLocal(int isLocal) {
		this.isLocal = isLocal;
	}

	public boolean isResize() {
		return isResize;
	}

	public void setResize(boolean isResize) {
		this.isResize = isResize;
	}
}
