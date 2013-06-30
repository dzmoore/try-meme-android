package com.eastapps.mgs.model;


public class SampleMeme {

    private Meme sampleMeme;

    private MemeBackground background;

    private Long id;

    private Integer version;
    
	public Meme getSampleMeme() {
        return this.sampleMeme;
    }

	public void setSampleMeme(Meme sampleMeme) {
        this.sampleMeme = sampleMeme;
    }

	public MemeBackground getBackground() {
        return this.background;
    }

	public void setBackground(MemeBackground background) {
        this.background = background;
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
