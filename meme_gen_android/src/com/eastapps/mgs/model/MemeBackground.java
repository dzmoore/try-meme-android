package com.eastapps.mgs.model;

import java.io.Serializable;


public class MemeBackground implements Serializable {

	private static final long serialVersionUID = 6617358846863686747L;

	private Boolean active;

    private String filePath;

    private String description;

    private Long id;

    private Integer version;
    
	public Boolean getActive() {
        return this.active;
    }

	public void setActive(Boolean active) {
        this.active = active;
    }

	public String getFilePath() {
        return this.filePath;
    }

	public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

	public String getDescription() {
        return this.description;
    }

	public void setDescription(String description) { 
        this.description = description;
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
