// Distributor object contain: id,name ,icon link path, catalog_id
package com.niw.adv.object;

public class DistributorObj {
	public static String DISTRIBUTOR_NUM = "distributor_num";
	public static String ID_COLUMN = "id";
	public static String NAME_COLUMN = "name";
	public static String ICON_COLUMN = "icon";
	public static String RECORDS_COLUMN = "records";

	private int id;
	private String name;
	private String icon;
	private int catalog_id;
	private int records;

	public DistributorObj(int id, String name, String icon, int catalog_id,
			int records) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.catalog_id = catalog_id;
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

	public int getCatalog_id() {
		return catalog_id;
	}

	public void setCatalog_id(int catalog_id) {
		this.catalog_id = catalog_id;
	}

	public int getRecords() {
		return records;
	}

	public void setRecords(int records) {
		this.records = records;
	}
}
