package com.youngo.mobile.model.user;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.youngo.core.json.JasonDateFieldFormatter;
import com.youngo.core.json.JsonDateDeserializer;

public class Gallery {
	@JsonIgnore
    private String id;
	
	@JsonIgnore
    private String userId;

    private String thumbUrl;

    private String normalUrl;

    private String originalUrl;

    @JsonSerialize(using = JasonDateFieldFormatter.class) @JsonDeserialize(using=JsonDateDeserializer.class)
    private Date createtime;

    private Byte seq;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl == null ? null : thumbUrl.trim();
    }

    public String getNormalUrl() {
        return normalUrl;
    }

    public void setNormalUrl(String normalUrl) {
        this.normalUrl = normalUrl == null ? null : normalUrl.trim();
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl == null ? null : originalUrl.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Byte getSeq() {
        return seq;
    }

    public void setSeq(Byte seq) {
        this.seq = seq;
    }
}