package com.eastapps.mgs.model;

import com.trymeme.meme_gen_android.R;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class MemeUser implements Serializable {

	private static final long serialVersionUID = -9022883959291231458L;

	private Boolean active;

    private String username;

    private String password;

    private String salt;

    private Long id;

    private Integer version;

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
