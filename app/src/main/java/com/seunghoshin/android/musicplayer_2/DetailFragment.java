package com.seunghoshin.android.musicplayer_2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.seunghoshin.android.musicplayer_2.domain.Music;

import java.util.Set;


public class DetailFragment extends Fragment {

    ViewHolder viewHolder = null;

    public DetailFragment() {

    }


    public static DetailFragment newInstance() {
        DetailFragment fragment = new DetailFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pager, container, false);
        viewHolder = new ViewHolder(view); //todo return new ViewHolder(view) 하면 왜 안되는지..
        return view;
    }


    public Set<Music.Item> getDatas(){
        Music music = Music.getInstance();
        music.loader(getContext());

        return music.getItems();
    }


    // ViewPager 의 View
    public class ViewHolder implements View.OnClickListener{
        ViewPager viewPager;
        RelativeLayout layoutController;

        ImageButton btnPlay, btnNext, btnPrev;
        SeekBar seekBar;
        TextView current, duration;

        public ViewHolder(View view) {
            viewPager = (ViewPager) view.findViewById(R.id.viewPager);
            layoutController = (RelativeLayout) view.findViewById(R.id.layoutController);
            btnPlay = (ImageButton) view.findViewById(R.id.btnPlay);
            btnNext = (ImageButton) view.findViewById(R.id.btnNext);
            btnPrev = (ImageButton) view.findViewById(R.id.btnPrev);
            seekBar = (SeekBar) view.findViewById(R.id.seekBar);
            current = (TextView) view.findViewById(R.id.current);
            duration = (TextView) view.findViewById(R.id.duration);
            setOnClickListener();
            setViewPager();
        }

        private void setOnClickListener() {
            btnPlay.setOnClickListener(this);
            btnNext.setOnClickListener(this);
            btnPrev.setOnClickListener(this);
        }

        private void setViewPager(){
            DetailAdapter adapter = new DetailAdapter(getDatas());
            viewPager.setAdapter(adapter);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id. btnPlay:
                    break;
                case R.id.btnNext:
                    break;
                case R.id.btnPrev:
                    break;
            }
        }
    }

}
