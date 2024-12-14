package com.isetb.news_application;

import java.util.List;

import java.util.List;

public class News {
    private String status;
    private int totalResults;
    private List<Article> articles;

    public String getStatus() {
        return status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<Article> getArticles() {
        return articles;
    }

    @Override
    public String toString() {
        return "Status: " + status + ", TotalResults: " + totalResults + ", Articles: " + articles.size();
    }
}

