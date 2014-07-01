package com.niw.adv.object;

public class AdObj {
	private String title;
	private String link;
	public static String TITLE_COLUMN="title";
	public static String LINK_COLUMN="link";

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public AdObj(String title, String link) {
		this.title = title;
		this.link = link;
	}
}
