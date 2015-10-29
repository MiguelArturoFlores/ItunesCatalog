package com.mgl.catalogitunes.model;

import java.io.Serializable;

public class ItunesApp implements Serializable {

	
	private Long id;
	private String name;

	private String image53;
	private String image75;
	private String image100;

	private String company;

	private String summary;
	private String price;
	private String currency;
	private String type;
	private String category;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage53() {
		return image53;
	}

	public void setImage53(String image53) {
		this.image53 = image53;
	}

	public String getImage75() {
		return image75;
	}

	public void setImage75(String image75) {
		this.image75 = image75;
	}

	public String getImage100() {
		return image100;
	}

	public void setImage100(String image100) {
		this.image100 = image100;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void addImg(String image, int pos) {
		try {

			if (pos == 0) {
				image53 = image;
			} else if (pos == 1) {
				image75 = image;
			} else if (pos == 2) {
				image100 = image;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
