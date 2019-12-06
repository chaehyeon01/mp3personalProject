package com.example.user.musicmp3test;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class SubMusicActivity extends Fragment{

    private View view;
    private RecyclerView recyclerView;
//   private Button btnInteData;
    public static MymusicListAdapter mymusicListAdapter;
    private LinearLayoutManager linearLayoutManager;

    MyDBHelper myDBHelper;
    SQLiteDatabase sqLiteDatabase;

    static ArrayList<MymusicDataVO> sub_list=new ArrayList<MymusicDataVO>();

    public static SubMusicActivity newInstance(){

        SubMusicActivity SubMusicActivty =new SubMusicActivity();
        return SubMusicActivty;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("뮤직","12");
        view=inflater.inflate(R.layout.sub_music_layout,container,false);
        recyclerView=view.findViewById(R.id.sub_recyclerView);
//        btnInteData=view.findViewById(R.id.btnInteData);
        view.setTag("SubMusic");
        myDBHelper=new MyDBHelper(view.getContext());

        Log.d("뮤직","13");

        linearLayoutManager=new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        Log.d("뮤직","14");

//        btnInteData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sqLiteDatabase=myDBHelper.getWritableDatabase();
//                myDBHelper.onUpgrade(sqLiteDatabase,1,2);
//                sqLiteDatabase.close();
//                Toast.makeText(view.getContext(),"초기화되었습니다.",Toast.LENGTH_SHORT).show();
//                sub_list.removeAll(sub_list);
//                mymusicListAdapter.notifyDataSetChanged();
//                MainActivity.fragmentPagerAdapter.notifyDataSetChanged();
//            }
//        });

        sub_list.clear();
        sqLiteDatabase=myDBHelper.getWritableDatabase();
        Cursor cursor;
        cursor=sqLiteDatabase.rawQuery("SELECT * FROM musicList",null);

        while (cursor.moveToNext()){
            sub_list.add(new MymusicDataVO(cursor.getString(0),
                    cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4),
                    cursor.getString(5)
            ));

        }
        cursor.close();
        sqLiteDatabase.close();
        mymusicListAdapter=new MymusicListAdapter(R.layout.sub_music_list_layout,sub_list);
        recyclerView.setAdapter(mymusicListAdapter);

            Log.d("뮤직","DB저장3");


        return view;
    }




}
