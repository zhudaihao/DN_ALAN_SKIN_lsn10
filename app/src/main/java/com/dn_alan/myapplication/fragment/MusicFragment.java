package com.dn_alan.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dn_alan.myapplication.R;

public class MusicFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music, container, false);
    }

    @Override
    public LayoutInflater onGetLayoutInflater(Bundle savedInstanceState) {
        return super.onGetLayoutInflater(savedInstanceState);
    }
}
