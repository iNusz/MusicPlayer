package com.seunghoshin.android.musicplayer_2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.seunghoshin.android.musicplayer_2.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements
        ItemFragment.OnListFragmentInteractionListener{

    FrameLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setView();
        setFragment(ItemFragment.newInstance(1)); // 목록 프래그먼트  , ()숫자는 컬럼의 형태 , 2,3 2줄, 3줄 로 나누어짐
    }


    private void setView(){
        layout = (FrameLayout) findViewById(R.id.layout);
    }

    private void setFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.layout, fragment);
        transaction.commit();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
