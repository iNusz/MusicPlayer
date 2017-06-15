package com.seunghoshin.android.musicplayer_2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seunghoshin.android.musicplayer_2.ListFragment.OnListFragmentInteractionListener;
import com.seunghoshin.android.musicplayer_2.domain.Music;
import com.seunghoshin.android.musicplayer_2.dummy.DummyContent.DummyItem;

import java.util.Set;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;
    private final Set<Music.Item> mValues;

    // 데이터 저장소
    private final Music.Item datas[];

    public ListAdapter(Set<Music.Item> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;

        // set에서 데이터 꺼내서 사용을 하는데 index를 필요로 하는경우 array 에 담는다
        datas = (Music.Item[]) mValues.toArray();

       // mValues.toArray(datas); // 안에 공간이 확정된 list를 넣어준다 , 원래는 서로타입이 안맞는데 맞춰주는 것이다
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // datas 저장소에 들어가 있는 Music.Item 한개를 꺼낸다
        holder.mItem = datas[position];
        holder.mIdView.setText(holder.mItem.id);
        holder.mContentView.setText(holder.mItem.title);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Music.Item mItem; // 미리 아이템에 해당하는 데이터를 몰아넣고 화면에 보이지않는 데이터를 넣는다

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
