package com.eastapps.mgs.model;

import javax.persistence.ManyToOne;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class SampleMeme {

    @ManyToOne
    private Meme sampleMeme;

    @ManyToOne
    private MemeBackground background;
}
