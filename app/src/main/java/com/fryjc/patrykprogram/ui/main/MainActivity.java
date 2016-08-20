package com.fryjc.patrykprogram.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.fryjc.patrykprogram.R;
import com.fryjc.patrykprogram.model.SearchResult;
import com.fryjc.patrykprogram.model.User;
import com.fryjc.patrykprogram.network.GitHubService;
import com.fryjc.patrykprogram.ui.DataAdapter;
import com.fryjc.patrykprogram.ui.userdetails.Main2Activity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.fryjc.patrykprogram.MESSAGE";
    private RecyclerView recyclerView;
    private ArrayList<User> data;
    private DataAdapter adapter;
    private Handler handler;
    private GitHubService service;
    private int clicked;
    private final PublishSubject<String> textSubject = PublishSubject.create();
    private Observer<Integer> clickObserver;
    private Observable<String> textChange;
    private SearchBar editText;
    private User noResults = new User("No Results");
    ArrayList<User> newdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        initViews();

    }
    private void initViews() {
        data = new ArrayList<User>();
        data.add(noResults);
        editText = (SearchBar) findViewById(R.id.edittext);
        textChange = editText.GetTextUpdate();
        textChange.debounce(300,TimeUnit.MILLISECONDS).subscribe(String -> refreshSearch(String));
        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.github.com")
                .addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(GitHubService.class);
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

    }

    private void refreshSearch(String string) {
        Call<SearchResult> call = service.getUsers(string);
        call.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                ArrayList<User> noResultsList = new ArrayList<User>();
                noResultsList.add(noResults);
                SearchResult noSearchResult = new SearchResult(1,noResultsList);
                data.clear();
                SearchResult newData;
                if(response.body()==null){
                    newData = noSearchResult;
                    Toast.makeText(getApplicationContext(),"No users found", Toast.LENGTH_SHORT).show();
                }   else
                    newData = response.body();
                List<User> jsonResponse = newData.getItems();
                data.addAll(jsonResponse);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }



    public void toUser(int index){
        Gson gson = new Gson();
        String json = gson.toJson(data.get(index));
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra(EXTRA_MESSAGE, json);
        startActivity(intent);
    }
}
