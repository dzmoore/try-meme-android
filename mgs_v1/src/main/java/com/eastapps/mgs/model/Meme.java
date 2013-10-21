package com.eastapps.mgs.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Meme {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version;

    @PersistenceContext
    transient EntityManager entityManager;

    @ManyToOne(cascade = CascadeType.ALL)
    private MemeBackground memeBackground;

    @ManyToOne(cascade = CascadeType.ALL)
    private MemeText topText;

    @ManyToOne(cascade = CascadeType.ALL)
    private MemeText bottomText;

    @ManyToOne(cascade = CascadeType.ALL)
    private MemeUser createdByUser;

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

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static final EntityManager entityManager() {
        EntityManager em = new Meme().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countMemes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Meme o", Long.class).getSingleResult();
    }

    public static List<com.eastapps.mgs.model.Meme> findAllMemes() {
        return entityManager().createQuery("SELECT o FROM Meme o", Meme.class).getResultList();
    }
    
    public static List<Meme> findMemesForUserId(final Long userId, final int firstResult, final int maxResults) {
    	return entityManager()
    		.createQuery("select m from Meme m where m.createdByUser.id = :userId", Meme.class)
    		.setParameter("userId", userId)
    		.setFirstResult(firstResult)
    		.setMaxResults(maxResults)
    		.getResultList();
    }

    public static com.eastapps.mgs.model.Meme findMeme(Long id) {
        if (id == null) return null;
        return entityManager().find(Meme.class, id);
    }

    public static List<com.eastapps.mgs.model.Meme> findMemeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Meme o", Meme.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Meme attached = Meme.findMeme(this.id);
            this.entityManager.remove(attached);
        }
    }

    @Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

    @Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

    @Transactional
    public com.eastapps.mgs.model.Meme merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Meme merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public MemeBackground getMemeBackground() {
        return this.memeBackground;
    }

	public void setMemeBackground(MemeBackground memeBackground) {
        this.memeBackground = memeBackground;
    }

	public MemeText getTopText() {
        return this.topText;
    }

	public void setTopText(MemeText topText) {
        this.topText = topText;
    }

	public MemeText getBottomText() {
        return this.bottomText;
    }

	public void setBottomText(MemeText bottomText) {
        this.bottomText = bottomText;
    }

	public MemeUser getCreatedByUser() {
        return this.createdByUser;
    }

	public void setCreatedByUser(MemeUser createdByUser) {
        this.createdByUser = createdByUser;
    }
}
