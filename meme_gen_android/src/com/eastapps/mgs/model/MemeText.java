package com.eastapps.mgs.model;

import java.io.Serializable;


public class MemeText implements Serializable {
	private static final long serialVersionUID = 5778466522510355213L;

	private String text;

    private Double fontSize;

    private Long id;

    private Integer version;
    
	public String getText() {
        return this.text;
    }

	public void setText(String text) {
        this.text = text;
    }

	public Double getFontSize() {
        return this.fontSize;
    }

	public void setFontSize(Double fontSize) {
        this.fontSize = fontSize;
    }

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

}
