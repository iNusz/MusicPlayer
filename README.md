# Music Player


## PermissionControl


권한을 설정해준다.


##### Manifaest


만약에 Write, Read 권한이 필요하다면 Write권한만 적어주면 된다. 그 이유는 Write권한이 더 상위에 있기 때문이다.



```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```




##### checkVersion



마시멜로 이상버전에서 Runtime 권한 체크를 해준다.



```java
public static void checkVersion(Activity activity){
  // 마시멜로 이상버전에서만 런타임 권한체크
  if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    checkPermission(activity);
  }else {
    callInit(activity);
  }
}
```




##### checkPermission




권한체크 및 권한이 없을 시 요청한다.




```java
@TargetApi(Build.VERSION_CODES.M)
public static void checkPermission(Activity activity){
  //1 권한체크 - 특정권한이 있는지 시스템에 물어본다
  boolean denied = false;
  for(String perm : permissions){
    if(activity.checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED){
      denied = true;
      break;
    }
  }
  if(denied){
    // 2. 권한이 없으면 사용자에 권한을 달라고 요청
    activity.requestPermissions(permissions ,REQ_FLAG);
  }else{
    callInit(activity);
  }
}
```



##### onResult



권한 설정을 했는지 안했는지 구별 해준다.



```java
public static void onResult(Activity activity, int requestCode, @NonNull int[] grantResults){
  if(requestCode == REQ_FLAG){
    boolean granted = true;
    for(int grant : grantResults){
      if(grant != PackageManager.PERMISSION_GRANTED){
        granted = false;
        break;
      }
    }
    // 3.1 사용자가 승인을 했음
    if(granted){
      callInit(activity);
    }else{
      Toast.makeText(activity, "권한요청을 승인하셔야 앱을 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
      //activity.finish();
    }
  }
}  
```




##### callInit



Activity가 callback을 구현했는지 확인하고, 그렇지 않으면 RuntimeException을 발생시킨다



```java
private static void callInit(Activity activity){
  if(activity instanceof CallBack)
  // activity를 넘겨주면 중간에 implement하거나 , 상속받은 모든 객체로 다 캐스팅 된다 . 그래서 CallBack으로 캐스팅
  ((CallBack)activity).init();
  else
  throw new RuntimeException("must implement this.CallBack");
    }
```




##### CallBack



init 함수를 실행 한다.



```java
public interface CallBack {
  public void init();
}
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
