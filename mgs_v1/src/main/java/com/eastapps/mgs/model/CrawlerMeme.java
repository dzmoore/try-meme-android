package com.eastapps.mgs.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class CrawlerMeme {

    private String name;

    private String topText;

    private String bottomText;

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new CrawlerMeme().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countCrawlerMemes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CrawlerMeme o", Long.class).getSingleResult();
    }

	public static List<CrawlerMeme> findAllCrawlerMemes() {
        return entityManager().createQuery("SELECT o FROM CrawlerMeme o", CrawlerMeme.class).getResultList();
    }

	public static CrawlerMeme findCrawlerMeme(Long id) {
        if (id == null) return null;
        return entityManager().find(CrawlerMeme.class, id);
    }

	public static List<CrawlerMeme> findCrawlerMemeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CrawlerMeme o", CrawlerMeme.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            CrawlerMeme attached = CrawlerMeme.findCrawlerMeme(this.id);
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
    public CrawlerMeme merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        CrawlerMeme merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String getTopText() {
        return this.topText;
    }

	public void setTopText(String topText) {
        this.topText = topText;
    }

	public String getBottomText() {
        return this.bottomText;
    }

	public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
    }
}
