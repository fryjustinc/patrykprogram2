package com.fryjc.patrykprogram.network;

import com.fryjc.patrykprogram.model.SearchResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GitHubService {

    @GET("/search/users")
    Call<SearchResult> getUsers(@Query("q") String search);
}
