package com.fryjc.patrykprogram.ui;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fryjc.patrykprogram.R;
import com.fryjc.patrykprogram.model.User;

import java.util.ArrayList;

import rx.Observable;
import rx.subjects.PublishSubject;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<User> user;
    private final PublishSubject<Integer> onClickSubject = PublishSubject.create();
    private int visibleThreshold = 8;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    public DataAdapter(ArrayList<User> users, RecyclerView recyclerView) {
        this.user = users;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        }
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {
        final int element = user.get(i).getId();
        viewHolder.tv_name.setText(user.get(i).getLogin());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubject.onNext(i);
            }
        });

    }
    public Observable<Integer> getPositionClicks(){
        return onClickSubject.asObservable();
    }

    @Override
    public int getItemViewType(int position) {
        return user.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void setLoaded() {
        loading = false;
    }


    @Override
    public int getItemCount() {
        return user.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name;
        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView)view.findViewById(R.id.tv_name);

        }
    }

}