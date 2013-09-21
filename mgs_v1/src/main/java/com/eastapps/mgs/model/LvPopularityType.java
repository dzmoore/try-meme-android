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
import org.apache.log4j.Logger;
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
public class LvPopularityType {

    private String popularityTypeName;

	public String getPopularityTypeName() {
        return this.popularityTypeName;
    }

	public void setPopularityTypeName(String popularityTypeName) {
        this.popularityTypeName = popularityTypeName;
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

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new LvPopularityType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countLvPopularityTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LvPopularityType o", Long.class).getSingleResult();
    }

	public static List<LvPopularityType> findAllLvPopularityTypes() {
        return entityManager().createQuery("SELECT o FROM LvPopularityType o", LvPopularityType.class).getResultList();
    }

	public static LvPopularityType findLvPopularityType(Long id) {
        if (id == null) return null;
        return entityManager().find(LvPopularityType.class, id);
    }
	
	public static LvPopularityType findLvPopularityTypeByName(final String name) {
		if (name == null) return null;
		LvPopularityType singleResult = null;
		
		try {
    		singleResult = (LvPopularityType) entityManager()
    			.createQuery("SELECT o from LvPopularityType o where o.popularityTypeName = ?")
    			.setParameter(1, name).getSingleResult();
    		
		} catch (Exception e) {
			Logger.getLogger(LvPopularityType.class).error("error occurred while attempting to retrieve lv popularity type", e);
		}
		
		return singleResult;
	}

	public static List<LvPopularityType> findLvPopularityTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LvPopularityType o", LvPopularityType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            LvPopularityType attached = LvPopularityType.findLvPopularityType(this.id);
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
    public LvPopularityType merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        LvPopularityType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
