package com.leqienglish.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.leqienglish.R;

/**
 * Created by zhuleqi on 16/3/19.
 */
public class MyPageFragment  extends Fragment implements
        Handler.Callback {
    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View contextView = inflater.inflate(R.layout.books, container, false);


        return contextView;
    }
}
