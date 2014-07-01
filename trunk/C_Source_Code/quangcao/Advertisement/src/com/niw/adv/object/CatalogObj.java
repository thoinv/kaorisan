// Catalog object contain: id,name ,icon link path
package com.niw.adv.object;


public class CatalogObj {
	// STATIC COlUMN NAME
	public static String CATALOG_NUM = "catalog_num";
	public static String ID_COLUMN = "id";
	public static String NAME_COLUMN = "name";
	public static String ICON_COLUMN = "icon";
	public static String RECORDS_COLUMN = "records";

	private int id;
	private String name;
	private String icon;
	private int records;

	public CatalogObj(int id, String name, String icon, int records) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.records = records;
	}

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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.records = records;
	}

}
