package com.eastapps.mgs.model;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
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
public class MemeBackground {

    @NotNull
	@Basic
	@Column(name = "active", columnDefinition = "BIT", length = 1)
    private Boolean active;

    private String filePath;

    private String description;

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }


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

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new MemeBackground().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countMemeBackgrounds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MemeBackground o", Long.class).getSingleResult();
    }

	public static List<MemeBackground> findAllMemeBackgrounds() {
        return entityManager().createQuery("SELECT o FROM MemeBackground o", MemeBackground.class).getResultList();
    }
	
	public static List<MemeBackground> findMemeBackgroundsByName(final String query) {
		return entityManager()
			.createQuery("SELECT o FROM MemeBackground o where lower(o.description) like :query", MemeBackground.class)
			.setParameter("query", StringUtils.join("%", StringUtils.lowerCase(query), "%"))
			.getResultList();
	}

	public static MemeBackground findMemeBackground(Long id) {
        if (id == null) return null;
        return entityManager().find(MemeBackground.class, id);
    }

	public static List<MemeBackground> findMemeBackgroundEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MemeBackground o order by o.description", MemeBackground.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            MemeBackground attached = MemeBackground.findMemeBackground(this.id);
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
    public MemeBackground merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        MemeBackground merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
