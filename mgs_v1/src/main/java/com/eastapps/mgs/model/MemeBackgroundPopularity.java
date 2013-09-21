package com.eastapps.mgs.model;

import java.util.ArrayList;
import java.util.List;
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
import org.apache.log4j.Logger;
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
public class MemeBackgroundPopularity {

    @ManyToOne
    private LvPopularityType lvPopularityType;

    private Long memeBackgroundPopularityValue;
    
    @ManyToOne
    private MemeBackground memeBackground;

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new MemeBackgroundPopularity().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countMemeBackgroundPopularitys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MemeBackgroundPopularity o", Long.class).getSingleResult();
    }
	
	@SuppressWarnings("unchecked")
	public static List<MemeBackgroundPopularity> findAllMemeBackgroundPopularitiesByPopularityTypeName(
		final String popularityTypeName, 
		final int firstResult, 
		final int maxResults) 
	{
		List<MemeBackgroundPopularity> memeBackgroundPopularities = new ArrayList<MemeBackgroundPopularity>(0);
		try {
    		memeBackgroundPopularities = (List<MemeBackgroundPopularity>)entityManager()
        		.createQuery("SELECT o FROM MemeBackgroundPopularity o where o.lvPopularityType.popularityTypeName = ?")
        		.setParameter(1, popularityTypeName)
        		.setFirstResult(firstResult)
        		.setMaxResults(maxResults)
        		.getResultList();
    		
		} catch (Exception e) {
			Logger.getLogger(MemeBackgroundPopularity.class).error("error occurred while attempting to find meme background popularity for lv popularity type name", e);
		}
		
		return memeBackgroundPopularities;
	}

	public static List<MemeBackgroundPopularity> findAllMemeBackgroundPopularitys() {
        return entityManager().createQuery("SELECT o FROM MemeBackgroundPopularity o", MemeBackgroundPopularity.class).getResultList();
    }

	public static MemeBackgroundPopularity findMemeBackgroundPopularity(Long id) {
        if (id == null) return null;
        return entityManager().find(MemeBackgroundPopularity.class, id);
    }

	public static List<MemeBackgroundPopularity> findMemeBackgroundPopularityEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MemeBackgroundPopularity o", MemeBackgroundPopularity.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            MemeBackgroundPopularity attached = MemeBackgroundPopularity.findMemeBackgroundPopularity(this.id);
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
    public MemeBackgroundPopularity merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        MemeBackgroundPopularity merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public LvPopularityType getLvPopularityType() {
        return this.lvPopularityType;
    }

	public void setLvPopularityType(LvPopularityType lvPopularityType) {
        this.lvPopularityType = lvPopularityType;
    }

	public Long getMemeBackgroundPopularityValue() {
        return this.memeBackgroundPopularityValue;
    }

	public void setMemeBackgroundPopularityValue(Long memeBackgroundPopularityValue) {
        this.memeBackgroundPopularityValue = memeBackgroundPopularityValue;
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

	public MemeBackground getMemeBackground() {
		return memeBackground;
	}

	public void setMemeBackground(MemeBackground memeBackground) {
		this.memeBackground = memeBackground;
	}
}
