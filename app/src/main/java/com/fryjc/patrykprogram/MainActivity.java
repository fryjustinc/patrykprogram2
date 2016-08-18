package com.fryjc.patrykprogram;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.fryjc.patrykprogram.MESSAGE";
    private RecyclerView recyclerView;
    private ArrayList<User> data;
    private DataAdapter adapter;
    private Handler handler;
    private GitHubService service;
    private int clicked;
    private Observer<Integer> clickObserver;
    ArrayList<User> newdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        initViews();

    }
    private void initViews() {
        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(GitHubService.class);
        Call<List<User>> call = service.getUsers(0);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> jsonResponse = response.body();
                data = (ArrayList<User>) jsonResponse;
                adapter = new DataAdapter(data, recyclerView);
                recyclerView.setAdapter(adapter);
                clickObserver = new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer s) {
                        toUser(s);
                    }
                };
                adapter.getPositionClicks().subscribe(clickObserver);
                initListen();
            }


            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }
    private void initListen() {
        adapter.setOnLoadMoreListener(new DataAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int page) {
                Call<List<User>> call = service.getUsers(page);
                call.enqueue(new Callback<List<User>>() {

                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        List<User> jsonResponse = response.body();
                        newdata = (ArrayList<User>) jsonResponse;
                        User loading = new User();
                        loading.setLogin("LOADING...");
                        data.add(loading);
                        adapter.notifyItemInserted(data.size() - 1);
                        data.remove(data.size() - 1);
                        adapter.notifyItemRemoved(data.size());
                        for (int i = 0; i < 30; i++) {
                            data.add(newdata.get(i));
                            adapter.notifyItemInserted(data.size());
                        }
                        adapter.setLoaded();
                        System.out.println("load");
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                    }
                });
            }
        });
}

    public void toUser(int login){
        Gson gson = new Gson();
        String json = gson.toJson(data.get(login));
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra(EXTRA_MESSAGE, json);
        startActivity(intent);
    }
}
