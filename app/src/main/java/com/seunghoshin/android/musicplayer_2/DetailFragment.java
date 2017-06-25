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

    static final String ARG1 = "position";

    ViewHolder viewHolder = null;

    public DetailFragment() {

    }


    public static DetailFragment newInstance(int position) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG1,position);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pager, container, false); // todo 일단 빈화면만 출력해준다 .
        Bundle bundle = getArguments();
        int position = bundle.getInt(ARG1);
        viewHolder = new ViewHolder(view, position); //todo viewHolder = new Detail ...  --> 뷰홀더에 담고 start를 통해서 데이터를 셋팅해준다
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

        public ViewHolder(View view , int position) {
            viewPager = (ViewPager) view.findViewById(R.id.viewPager);
            layoutController = (RelativeLayout) view.findViewById(R.id.layoutController);
            btnPlay = (ImageButton) view.findViewById(R.id.btnPlay);
            btnNext = (ImageButton) view.findViewById(R.id.btnNext);
            btnPrev = (ImageButton) view.findViewById(R.id.btnPrev);
            seekBar = (SeekBar) view.findViewById(R.id.seekBar);
            current = (TextView) view.findViewById(R.id.current);
            duration = (TextView) view.findViewById(R.id.duration);
            setOnClickListener();
            setViewPager(position);
        }

        private void setOnClickListener() {
            btnPlay.setOnClickListener(this);
            btnNext.setOnClickListener(this);
            btnPrev.setOnClickListener(this);
        }

        private void setViewPager(int position){
            DetailAdapter adapter = new DetailAdapter(getDatas());
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(position);
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
