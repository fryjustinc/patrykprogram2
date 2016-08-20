package com.fryjc.patrykprogram.ui.main;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by patrykpoborca on 8/19/16.
 */
public class SearchBar extends EditText {
    private final PublishSubject<String> onTextubject = PublishSubject.create();

    public SearchBar(Context context, AttributeSet aSet) {
        super(context, aSet);
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                onTextubject.onNext(s.toString());
            }
        });
    }

    public Observable<String> GetTextUpdate(){
       return onTextubject.asObservable();
    }

}
