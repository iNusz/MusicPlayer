package com.seunghoshin.android.musicplayer_2.domain;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by SeungHoShin on 2017. 6. 16..
 */

public class Music {
    private static Music instance = null;
    private Set<Item> items = null;

    private Music() {
        items = new HashSet<>();
    }

    public static Music getInstance() {
        if (instance == null)
            instance = new Music();
        return instance;
    }

    public Set<Item> getItems() {
        return items;
    }

    // 음악데이터를 폰에서 꺼낸다음 List 저장소에 담아둔다
    public void loader(Context context) {
        // 데이터가 계속 쌓이는것을 방지한다
        items.clear();
        ContentResolver resolver = context.getContentResolver();

        // 1. 테이블 명 정의 ?
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        // 2. 가져올 컬럼명 정의
        String proj[] = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST
        };
        // 3. 쿼리  todo 각각의 조건이 어떻게되는지 null말고 딴걸 넣어서 공부해보자
        Cursor cursor = resolver.query(uri, proj, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Item item = new Item();
                int index = cursor.getColumnIndex(proj[0]);
                item.id = getValue(cursor, proj[0]);
                item.albumId = getValue(cursor, proj[1]);
                item.title = getValue(cursor, proj[2]);
                item.artist = getValue(cursor, proj[3]);


                item.musicUri = makeMusicUri(item.id);
                item.albumArt = makeAlbumUri(item.albumId);


                // 저장소에 담는다...
                items.add(item);
            }
        }
    }


    private String getValue(Cursor cursor, String name) {
        int index = cursor.getColumnIndex(name);
        return cursor.getString(index);
    }


    // todo Set이 정상적으로 중복값을 허용하지 않도록 어떤함수를 오버라이드해서 구현하세요
    public class Item {
        public String id;
        public String albumId;
        public String artist;
        public String title;

        Uri musicUri;
        Uri albumArt;

    }

    // Music id만 넘겨주면 Uri로 바뀌어서 다시 반환해주는 메소드이다
    private Uri makeMusicUri(String musicId) {
        Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        return Uri.withAppendedPath(contentUri, musicId);
    }

    private Uri makeAlbumUri(String albumId) {
        String albumUri = "content://media/external/audio/albumart/";
        return Uri.parse(albumUri + albumId);
    }

}
