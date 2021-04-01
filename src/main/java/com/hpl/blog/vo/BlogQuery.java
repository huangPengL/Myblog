package com.hpl.blog.vo;

public class BlogQuery {
    private String title;
    private Long typeId;
    private boolean recommendState;

    public BlogQuery() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public boolean isRecommendState() {
        return recommendState;
    }

    public void setRecommendState(boolean recommendState) {
        this.recommendState = recommendState;
    }
}
