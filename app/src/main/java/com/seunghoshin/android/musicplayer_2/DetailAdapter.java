package com.seunghoshin.android.musicplayer_2;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seunghoshin.android.musicplayer_2.domain.Music;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by SeungHoShin on 2017. 6. 21..
 */

public class DetailAdapter extends PagerAdapter {

    List<Music.Item> datas = null;

    public DetailAdapter(Set<Music.Item> datas) {
        this.datas = new ArrayList<>(datas);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    // RecyclerView의 onBindViewHolder의 역활
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //return super.instantiateItem(container, position);
        View view = LayoutInflater
                .from(container.getContext())
                .inflate(R.layout.fragment_pager_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        TextView textView = (TextView) view.findViewById(R.id.textView);

        Glide.with(container.getContext())
                .load(datas.get(position).albumArt)
                .into(imageView);

        textView.setText(datas.get(position).title);

        //생성한 뷰를 뷰페이저 container에 담아줘야한다
        container.addView(view);

        return view;
    }

    // 화면에서 사라진 뷰를 메모리에서 제거하는 함수 , 뷰페이저는 항상 3개만 띄워논다 (앞에꺼 , 뒤에꺼 , 현재)
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        // object를 뷰로 캐스팅 해준다
        container.removeView((View)object);
    }

    // instantiateItem 에서 리턴한 object가 view가 맞는지 확인한다
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
