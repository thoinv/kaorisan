// detail object contain: id,title ,link ,expire date, distributor id
package com.niw.adv.object;

public class DetailObj {
	public static String DETAIL_NUM = "detail_num";
	public static String TITLE_COLUMN="title";
	public static String LINK_COLUMN="link";
	public static String EXPIRE_COLUMN="expire";
	public static String DISTRIBUTOR_COLUMN="distributor_id";
	
	private String title;
	private String link;
	private String expire;

	public DetailObj( String title, String link, String empire) {
		this.title = title;
		this.link = link;
		this.expire = empire;
	}
	
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

	public String getEmpire() {
		return expire;
	}

	public void setEmpire(String empire) {
		this.expire = empire;
	}
}
