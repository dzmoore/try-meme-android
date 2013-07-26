package com.eastapps.mgs.model;

import java.io.Serializable;
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
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooSerializable
public class MemeUserFavorite implements Serializable {
	private static final long serialVersionUID = -3528340674212252670L;

	@ManyToOne
    private MemeUser memeUser;

    @ManyToOne
    private MemeBackground memeBackground;

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

	public MemeUser getMemeUser() {
        return this.memeUser;
    }

	public void setMemeUser(MemeUser memeUser) {
        this.memeUser = memeUser;
    }

	public MemeBackground getMemeBackground() {
        return this.memeBackground;
    }

	public void setMemeBackground(MemeBackground memeBackground) {
        this.memeBackground = memeBackground;
    }


	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new MemeUserFavorite().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countMemeUserFavorites() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MemeUserFavorite o", Long.class).getSingleResult();
    }

	public static List<MemeUserFavorite> findAllMemeUserFavorites() {
        return entityManager().createQuery("SELECT o FROM MemeUserFavorite o", MemeUserFavorite.class).getResultList();
    }

	public static MemeUserFavorite findMemeUserFavorite(Long id) {
        if (id == null) return null;
        return entityManager().find(MemeUserFavorite.class, id);
    }

	public static List<MemeUserFavorite> findMemeUserFavoriteEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MemeUserFavorite o", MemeUserFavorite.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            MemeUserFavorite attached = MemeUserFavorite.findMemeUserFavorite(this.id);
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
    public MemeUserFavorite merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        MemeUserFavorite merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
