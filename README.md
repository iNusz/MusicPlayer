# Music Player


## Permission


권한을 설정해준다.


##### Manifaest


만약에 Write, Read 권한이 필요하다면 Write권한만 적어주면 된다. 그 이유는 Write권한이 더 상위에 있기 때문이다.



```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```



## Music






#### Loader


음악데이터를 읽어와서 저장소에 담는다



```java
public void loader(Context context){

  ContentResolver resolver = context.getContentResolver();
  // 1. 테이블 명 정의 ?
  Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
  // 2. 가져올 컬럼명 정의
  String proj[] = {
    MediaStore.Audio.Media._ID,
    MediaStore.Audio.Media.ALBUM_ID,
  }
  ...---Skip---...


  // 3. 쿼리  todo 각각의 조건이 어떻게되는지 null말고 딴걸 넣어서 공부해보자
  Cursor cursor = resolver.query(uri, proj, null, null, null);

  if(cursor != null){

    while (cursor.moveToNext()){

      Item item = new Item();
      int index = cursor.getColumnIndex(proj[0]);
      item.id = getValue(cursor, proj[0]);
      item.albumId = getValue(cursor, proj[1]);               

      item.musicUri = makeMusicUri(item.id);
      item.albumArt = makeAlbumUri(item.albumId);


      // 저장소에 담는다...
      items.add(item);
    }
  }
}
```




##### makeMusicUri, makeAlbumUri


Music id만 넘겨주면 Uri로 바뀌어서 다시 반환해주는 메소드이다


```java
private Uri makeMusicUri(String musicId) {
  Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
  return Uri.withAppendedPath(contentUri, musicId);
}
```


```java
private Uri makeAlbumUri(String albumId) {
  String albumUri = "content://media/external/audio/albumart/";
  return Uri.parse(albumUri + albumId);
}
```
