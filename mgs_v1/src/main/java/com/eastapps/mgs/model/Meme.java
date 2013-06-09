package com.eastapps.mgs.model;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Meme {

    @OneToOne
    private MemeBackground memeBackground;

    @ManyToOne
    private MemeText topText;

    @ManyToOne
    private MemeText bottomText;

    @ManyToOne
    private MemeUser createdByUser;
}
