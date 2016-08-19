package com.fryjc.patrykprogram.network;

import com.fryjc.patrykprogram.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GitHubService {

    @GET("/users")
    Call<List<User>> getUsers(@Query("since") int page);
}
