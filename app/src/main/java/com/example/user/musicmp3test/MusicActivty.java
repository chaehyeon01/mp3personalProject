package com.example.user.musicmp3test;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MusicActivty extends Fragment implements OnItemClick{

    View view;
    RecyclerView music_RecyclerView;

    MyMusicDataAdapter myMusicDataAdapter;
     LinearLayoutManager linearLayoutManager;
     ArrayList<MymusicDataVO> list=new ArrayList<MymusicDataVO>();
    static String musicpath;

    static final String MP3_PATH=Environment.getExternalStorageDirectory().getAbsolutePath()+"/Music/";


    public static MusicActivty newInstance(){

        MusicActivty MusicActivty =new MusicActivty();
        return MusicActivty;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.music_layout,container,false);

        music_RecyclerView=view.findViewById(R.id.music_RecyclerView);

        getMusicList(view.getContext());


        Log.d("뮤직","3");

        linearLayoutManager=new LinearLayoutManager(view.getContext());

        Log.d("뮤직","4");
        music_RecyclerView.setLayoutManager(linearLayoutManager);

        Log.d("뮤직","5");
        myMusicDataAdapter= new MyMusicDataAdapter(R.layout.music_list_layout,list,this);

        Log.d("뮤직","6");
        music_RecyclerView.setAdapter(myMusicDataAdapter);


        Log.d("뮤직","7");



        return view;
    }

    public  void getMusicList(Context context){

        Log.d("뮤직","9");

        //가져오고 싶은 컬럼 명을 나열합니다. 음악의 아이디, 앰블럼 아이디, 제목, 아스티스트 정보를 가져옵니다.
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID
        };


        Log.d("뮤직","10");
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,  MediaStore.Audio.Media.DATA + " like ? ",new String[]{"%music/%"}, null); //sdcard의 절대경로를 받아 음악파일을 집어늠.

        if(cursor !=null){
            //다음 파일이 있으면
            while(cursor.moveToNext()){

                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                int albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                Log.d("뮤직",title+" / "+artist+" / "+album+" / "+duration+" / "+path+" / "+albumId);

                title=title.replace("`","\u0060");
                path=path.replace("'","\'");


                //음악앨범 아트 가져오기
                Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(
                            context.getContentResolver(), albumArtUri);
                    bitmap = Bitmap.createBitmap(bitmap);

                } catch (FileNotFoundException exception) {
                    exception.printStackTrace();
                    bitmap = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.music2);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                list.add(new MymusicDataVO(title,album,artist,path,duration,bitmap,String.valueOf(albumId)));
            }

        }else {

            Log.d("뮤직","불러오지 못했음.");
        }

        cursor.close();
    }


    //인터페이스 추상메소드
    @Override
    public void onListClick(MymusicDataVO mymusicDataVO) {

           musicpath=mymusicDataVO.getPath();
           Log.d("뮤직",musicpath);
        MainActivity.music_btnPlay.setVisibility(View.INVISIBLE);
        MainActivity.music_btnPause.setVisibility(View.VISIBLE);
        MainActivity.music_btnStop.setVisibility(View.VISIBLE);
        MainActivity.music_LinearLayout.setVisibility(View.VISIBLE);
        MainActivity.btnIsPause=false;
        MainActivity.music_btnPlay.callOnClick();



    }


}
