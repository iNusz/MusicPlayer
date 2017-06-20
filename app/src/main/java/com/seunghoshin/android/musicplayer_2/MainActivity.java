package com.seunghoshin.android.musicplayer_2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements
        ListFragment.OnListFragmentInteractionListener, PermissionControl.CallBack {

    FrameLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionControl.checkPermission(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionControl.onResult(this, requestCode, grantResults);
    }


    @Override
    public void init() {
        setView();
        setFragment(ListFragment.newInstance(1)); // 목록 프래그먼트  , ()숫자는 컬럼의 형태 , 2,3 2줄, 3줄 로 나누어짐
    }

    private void setView() {
        layout = (FrameLayout) findViewById(R.id.layout);
    }

    private void setFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.layout, fragment);
        transaction.commit();
    }

    private void addFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.layout, fragment);
        transaction.addToBackStack(null); // todo 왜 스택구조인가..
        transaction.commit();
    }


    // Fragment를 통해 Adapter까지 interface 를 전달하고
    // Adapter 에서 interface를 직접 호출해서 사용한다 .
    @Override
    public void goDetailInteraction() {  // todo 인터페이스 공책보고 질문...
        addFragment(DetailFragment.newInstance());
    }
}
