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


<br/>



##### CallBack



init 함수를 실행 한다.



```java
public interface CallBack {
  public void init();
}
```


<br/>



## Music



<br/>




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



<br/>




## DeatailAdapter



DeatailAdapter는 PagerAdapter를 상속받는다.



#### instantiateItem


이 함수는 따로 Override를 해줘야 하며, RecyclerView의 onBindViewHolder의 역활을 한다.



```java
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
```



#### destroyItem




이 함수는 따로 Override를 해줘야 하며, 화면에서 사라진 뷰를 메모리에서 제거하는 역활을 한다.



뷰페이저에 항상 3개만 띄워논다. (앞페이지, 뒤페이지, 현재페이지)




```java
@Override
public void destroyItem(ViewGroup container, int position, Object object) {
  //super.destroyItem(container, position, object);
  // object를 뷰로 캐스팅 해준다
  container.removeView((View)object);
}
```





#### isViewFromObject



이 함수는 instantiateItem 에서 리턴한 object가 view가 맞는지 확인하는 역활을 한다.



```java
@Override
public boolean isViewFromObject(View view, Object object) {
  return view == object;
}
```



#### DeatailFragment에 달기



DeatailFragment의 ViewHolder에 다음과 같이 함수를 호출 해준다.



```java
private void setViewPager(){
  DetailAdapter adapter = new DetailAdapter(getDatas());
  viewPager.setAdapter(adapter);
}
```




```java
public Set<Music.Item> getDatas(){
  Music music = Music.getInstance();
  music.loader(getContext());
  return music.getItems();
}
```






<br/>




## Glide API 사용하기




Glide를 쓰는 이유는 화면에 이미지를 로드할때 보이는것 대비 쓸데없이 큰 용량이 들어가기 때문에



자칫하다간 앱이 다운될 수 있다. 이를 방지하기 위해서 Glide를 쓴다.



<br/>



#### Gradle에 추가하기





3버전 중에 최신버전을 쓰겠다는 말이다.
     *
별표 는 3버전 전체를 가져온다는 뜻이다. 그래서 우리는 +를 써야한다

```xml
compile 'com.github.bumptech.glide:glide:3.+'
```


만약 이미지를 동그랗게 표현하고 싶으면 아래를 추가시킨다. (glide-transformations 구글링)



```xml
compile 'jp.wasabeef:glide-transformations:2.0.2'
```




#### 내용에 추가하기



ListAdapter의 onBindViewHolder에 추가한다.



이때 우리는 Adapter에서 context를 불러올 수 없기 때문에 전역변수로 context를 선언해주고



onCreateViewHolder에서 context를 받아왔다. 계속받아오면 안되기 때문에 if절을 걸어뒀다.



```java
@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
  if(context==null)
  context = parent.getContext();
  View view = LayoutInflater.from(parent.getContext())
  .inflate(R.layout.fragment_item, parent, false);
  return new ViewHolder(view);
}
```



```java
public void onBindViewHolder(final ViewHolder holder, int position) {
Glide.with(context)
.load(datas.get(position).albumArt) //로드 할 대상
.bitmapTransform(new CropCircleTransformation(context))
.into(holder.imgAlbum);             //이미지를 출력할 대상
}
```


#### 이미지가 깨져 보일 때



scaleType=”centerCrop” 는 ImageView 의 크기에 이미지를 끼워 맞추는 옵션이다.




좋은 옵션이지만 빈공간 없이 맞추기 때문에 가로/세로 균형이 맞지 않는다면 이미지가 찌그러져 보인다.





```xml
android:scaleType="centerCrop"
```








## 앱의 아이콘 바꾸기



우리는 안드로이드의 아이콘을 평생 쓸 수 없음으로 바꿔줘야 한다.



앱의 아이콘은 Manifest에 들어가서 바꿔주면 된다.



```xml
android:icon="@mipmap/ic_launcher"
<!--위의 것을 아래와 같이 바꿔준다 -->
android:icon="@mipmap/icon"
<!-- 안에 이미지 파일은 원하는 id값을 넣어주면 된다. -->
```



<br/>




## Tip


- Framelayout 에서 LinearLayout으로 바꿀 때에는 android:orientation="vertical || horizon"을 추가시켜야 한다.



- ViewPager을 추가하고 싶으면 xml에가서 타이핑을 쳐야 한다.



- ImageView에서 adjustviewbounds는 이미지가 표현하고자 하는 레이아웃보다 클 때 비율 유지여부를 나타내는 것이다.
