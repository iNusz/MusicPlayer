package com.seunghoshin.android.musicplayer_2;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seunghoshin.android.musicplayer_2.ListFragment.OnListFragmentInteractionListener;
import com.seunghoshin.android.musicplayer_2.domain.Music;
import com.seunghoshin.android.musicplayer_2.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.seunghoshin.android.musicplayer_2.Player.playerStatus;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;

    private Context context = null;
    // 데이터 저장소
    private final List<Music.Item> datas;

    public ListAdapter(Set<Music.Item> items, OnListFragmentInteractionListener listener) {
        mListener = listener;

        // List에서 데이터 꺼내서 사용을 하는데 index를 필요로 하는경우 array 에 담는다
        datas = new ArrayList<>(items);

        // mValues.toArray(datas); // 안에 공간이 확정된 list를 넣어준다 , 원래는 서로타입이 안맞는데 맞춰주는 것이다
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null)
            context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // datas 저장소에 들어가 있는 Music.Item 한개를 꺼낸다
        Music.Item item = datas.get(position);
        holder.position = position; //온클릭리스너를 클릭했을때 그당시의 포지션을 가지고 온다.
        holder.musicUri = datas.get(position).musicUri;
        holder.mIdView.setText(item.id);
        holder.mContentView.setText(item.title);
        // holder.imgAlbum.setImageURI(datas.get(position).albumArt);

        Glide.with(context)
                .load(datas.get(position).albumArt) //로드 할 대상
                .bitmapTransform(new CropCircleTransformation(context)) // 이미지를 동그랗게 보이주기
                .into(holder.imgAlbum);             //이미지를 출력할 대상


        //pause 버튼의 보임 유무를 결정한다
        if(datas.get(position).itemClicked){
            holder.btnPause.setVisibility(View.VISIBLE);
        }else{
            holder.btnPause.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }



    public void goDetail(int position) {

    }

    // 클릭된걸 알려주고 이전꺼는 지워버린다
    public void setItemClicked(int position){
        for(Music.Item item: datas){
            item.itemClicked = false;
        }
        datas.get(position).itemClicked = true;
        //리스트뷰 전체를 갱신해 준다.
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public int position; // 뷰홀더에서 왔다갔다하면 서로 다른 아이디를 갖기때문에 선언해줬다
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView imgAlbum;
        public final ImageButton btnPause;


        public Uri musicUri;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            imgAlbum = (ImageView) view.findViewById(R.id.imgAlbum);
            btnPause = (ImageButton) view.findViewById(R.id.btnPause);

            // 플레이 버튼 노출
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setItemClicked(position);
                    Player.play(musicUri, mView.getContext());
                    btnPause.setImageResource(android.R.drawable.ic_media_pause); // 클릭했을때 플레이가 되면서 퍼즈버튼이 나온다
                    //btnPause.setVisibility(View.VISIBLE);

                }
            });

            btnPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (playerStatus) {
                        case Player.PLAY:
                            Player.pause();
                            // pause 가 클릭되면 이미지 모양이 play로 바뀐다
                            btnPause.setImageResource(android.R.drawable.ic_media_play);
                            break;
                        case Player.PAUSE:
                            Player.replay();
                            btnPause.setImageResource(android.R.drawable.ic_media_pause);
                            break;
                    }

                }
            });


            // 상세보기로 이동 -> 뷰페이저로 이동
            mView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    goDetail(position);
                    return true; // 롱클릭 후 온클릭이 실행되지 않도록 true를 한다.
                }

            });
        }


    }
}
