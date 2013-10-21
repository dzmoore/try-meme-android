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
public class CrawlerBackground {

    private String sourceDesc;

    private String crawlerImgFilename;

    private String crawlerImgDesc;

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new CrawlerBackground().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countCrawlerBackgrounds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CrawlerBackground o", Long.class).getSingleResult();
    }

	public static List<CrawlerBackground> findAllCrawlerBackgrounds() {
        return entityManager().createQuery("SELECT o FROM CrawlerBackground o", CrawlerBackground.class).getResultList();
    }

	public static CrawlerBackground findCrawlerBackground(Long id) {
        if (id == null) return null;
        return entityManager().find(CrawlerBackground.class, id);
    }

	public static List<CrawlerBackground> findCrawlerBackgroundEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CrawlerBackground o", CrawlerBackground.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            CrawlerBackground attached = CrawlerBackground.findCrawlerBackground(this.id);
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
    public CrawlerBackground merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        CrawlerBackground merged = this.entityManager.merge(this);
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

	public String getSourceDesc() {
        return this.sourceDesc;
    }

	public void setSourceDesc(String sourceDesc) {
        this.sourceDesc = sourceDesc;
    }

	public String getCrawlerImgFilename() {
        return this.crawlerImgFilename;
    }

	public void setCrawlerImgFilename(String crawlerImgFilename) {
        this.crawlerImgFilename = crawlerImgFilename;
    }

	public String getCrawlerImgDesc() {
        return this.crawlerImgDesc;
    }

	public void setCrawlerImgDesc(String crawlerImgDesc) {
        this.crawlerImgDesc = crawlerImgDesc;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
