package com.oreilly.test1.model;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
	
	private String id;
	private String title;
	private String publisher;
	private String format;
	private int numPages;
	private Date created;
	
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public int getNumPages() {
		return numPages;
	}

	public void setNumPages(int numPages) {
		this.numPages = numPages;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	
	
	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	
	public String getId() {
		return id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setId(String id) {
		this.id = id;
	}	

	public String getTitle() {
		return title;
	}
}
