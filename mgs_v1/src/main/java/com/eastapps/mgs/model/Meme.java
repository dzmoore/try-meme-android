package com.eastapps.mgs.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
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

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new Meme().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countMemes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Meme o", Long.class).getSingleResult();
    }

	public static List<Meme> findAllMemes() {
        return entityManager().createQuery("SELECT o FROM Meme o", Meme.class).getResultList();
    }

	public static Meme findMeme(Long id) {
        if (id == null) return null;
        return entityManager().find(Meme.class, id);
    }

	public static List<Meme> findMemeEntries(int firstResult, int maxResults) {
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
    public Meme merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Meme merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

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
