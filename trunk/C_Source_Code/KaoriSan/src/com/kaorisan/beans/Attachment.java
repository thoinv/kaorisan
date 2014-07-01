package com.kaorisan.beans;

public class Attachment {
	private int id;
	
	private String fileName;
	
	private String filePath;
	
	private String createAt;
	
	private String type;
	
	private int tmp;
	
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreateAt() {
		return createAt;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getTmp() {
		return tmp;
	}

	public void setTmp(int tmp) {
		this.tmp = tmp;
	}

	public int getIsLocal() {
		return isLocal;
	}

	public void setIsLocal(int isLocal) {
		this.isLocal = isLocal;
	}

	public boolean isCamera() {
		return isCamera;
	}

	public void setCamera(boolean isCamera) {
		this.isCamera = isCamera;
	}

	public boolean isResize() {
		return isResize;
	}

	public void setResize(boolean isResize) {
		this.isResize = isResize;
	}
}
