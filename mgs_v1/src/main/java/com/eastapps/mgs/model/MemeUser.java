package com.eastapps.mgs.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
public class MemeUser {

    private Boolean active;

    private String username;

    private String password;

    private String salt;

    @PersistenceContext
    transient EntityManager entityManager;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Meme> favoriteMemes = new HashSet<Meme>();

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return this.salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public static final EntityManager entityManager() {
        EntityManager em = new MemeUser().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countMemeUsers() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MemeUser o", Long.class).getSingleResult();
    }

    public static List<com.eastapps.mgs.model.MemeUser> findAllMemeUsers() {
        return entityManager().createQuery("SELECT o FROM MemeUser o", MemeUser.class).getResultList();
    }

    public static com.eastapps.mgs.model.MemeUser findMemeUser(Long id) {
        if (id == null) return null;
        return entityManager().find(MemeUser.class, id);
    }

    public static List<com.eastapps.mgs.model.MemeUser> findMemeUserEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MemeUser o", MemeUser.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            MemeUser attached = MemeUser.findMemeUser(this.id);
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
    public com.eastapps.mgs.model.MemeUser merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        MemeUser merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

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

	public Set<Meme> getFavoriteMemes() {
        return this.favoriteMemes;
    }

	public void setFavoriteMemes(Set<Meme> favoriteMemes) {
        this.favoriteMemes = favoriteMemes;
    }
}
