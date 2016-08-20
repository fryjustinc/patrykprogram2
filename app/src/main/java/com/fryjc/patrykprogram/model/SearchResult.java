package com.fryjc.patrykprogram.model;

import java.util.List;

/**
 * Created by patrykpoborca on 8/20/16.
 */
public class SearchResult {
    private int total_count;
    private String incomplete_results;

    public String getIncomplete_results() {
        return incomplete_results;
    }

    public void setIncomplete_results(String incomplete_results) {
        this.incomplete_results = incomplete_results;
    }

    private List<User> items;

    public SearchResult(int total_count, List<User> items) {
        this.total_count = total_count;
        this.items = items;
    }

    public SearchResult(int total_count, String incomplete_results, List<User> items) {

        this.total_count = total_count;
        this.incomplete_results = incomplete_results;
        this.items = items;
    }

    public List<User> getItems() {
        return items;
    }

    public void setItems(List<User> items) {
        this.items = items;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }
}
