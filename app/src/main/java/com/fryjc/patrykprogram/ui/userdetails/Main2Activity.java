package com.fryjc.patrykprogram.ui.userdetails;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.fryjc.patrykprogram.R;
import com.fryjc.patrykprogram.model.User;
import com.google.gson.Gson;

public class Main2Activity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.fryjc.patrykprogram.MESSAGE";
    private String value;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString(EXTRA_MESSAGE);
        }
        Gson gson = new Gson();
        user = gson.fromJson(value, User.class);
        initViews();
    }
    private void initViews() {
        TextView userName = (TextView) findViewById(R.id.textView);
        TextView userURL = (TextView) findViewById(R.id.textView2);
        TextView userFollowers = (TextView) findViewById(R.id.textView3);
        TextView userRepoNum = (TextView) findViewById(R.id.textView4);
        TextView userRepoURL = (TextView) findViewById(R.id.textView5);
        userName.setText(user.getLogin());
        userFollowers.setText("Number of Followers: " + user.getFollowers());
        userURL.setText(user.getUrl());
        userRepoNum.setText("Number of Public Repos: " + user.getPublic_repos());
        userRepoURL.setText("user repo URL: " +user.getRepos_url());

    }
}
