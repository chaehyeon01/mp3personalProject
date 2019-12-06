package com.example.user.musicmp3test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class MyMusicDataAdapter extends RecyclerView.Adapter<MyMusicDataAdapter.CustomViewHolder> {

    private int layout;
    ArrayList<MymusicDataVO> list;
    private OnItemClick onItemClick;
   private  MyDBHelper myDBHelper;
   private SQLiteDatabase sqLiteDatabase;
    View view;
     SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);

    public MyMusicDataAdapter(int layout, ArrayList<MymusicDataVO> list,OnItemClick onItemClick) {
        this.layout = layout;
        this.list = list;
        this.onItemClick = onItemClick;

    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view=LayoutInflater.from(viewGroup.getContext()).inflate(layout,viewGroup,false);

        Log.d("뮤직","8");

        myDBHelper=new MyDBHelper(view.getContext());

        CustomViewHolder viewHolder=new CustomViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder customViewHolder, final int position) {



        customViewHolder.txtMusicName.setText(list.get(position).getaName());
        customViewHolder.txtSinger.setText(list.get(position).getaArtist());
        customViewHolder.imgMusic.setImageBitmap(list.get(position).getAlbumID());

        customViewHolder.itemView.setTag(position);
        customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MymusicDataVO mymusicDataVO=list.get(position); //리스트뷰의 position을 넘겨줌
                onItemClick.onListClick(mymusicDataVO);
                MainActivity.txtCurrentPlayMusic.setText("★★실행 중인 음악은 "+"'"+mymusicDataVO.getaArtist()+"'"+"의 "+mymusicDataVO.getaName()+" 입니다! 즐거운 하루 보내세요><★★");
                MainActivity.txtCurrentPlayMusic.setSelected(true);
            }
        });

        customViewHolder.btnMyMusicList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {

                final MymusicDataVO mymusicDataVO=list.get(position);

                Log.d("뮤직","sub 사이즈"+SubMusicActivity.sub_list.size());
                for (int i=0; i <SubMusicActivity.sub_list.size(); ++i){

                    if (mymusicDataVO.getaName().trim().equals(SubMusicActivity.sub_list.get(i).getaName().trim())
                            && (mymusicDataVO.getaArtist().trim().equals(SubMusicActivity.sub_list.get(i).getaArtist().trim()))
                            ){

                        Log.d("뮤직","sub 사이즈"+SubMusicActivity.sub_list.size());
                        Log.d("뮤직","메인이름"+mymusicDataVO.getaName());
                        Log.d("뮤직","sub 이름"+SubMusicActivity.sub_list.get(i).getaName());
                        Log.d("뮤직","메인 아티스트"+mymusicDataVO.getaArtist());
                        Log.d("뮤직","sub 아티스트"+SubMusicActivity.sub_list.get(i).getaArtist());
                        Toast.makeText(view.getContext(),"이미 마이리스트에 저장되었습니다.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                View alertView=View.inflate(v.getContext(),R.layout.voite_music_layout,null);
               View alertTitleView=View.inflate(v.getContext(),R.layout.dirlog_title_layout,null);
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(v.getContext());

                TextView chooseMusicName=alertView.findViewById(R.id.chooseMusicName);
                TextView chooseMusicSinger=alertView.findViewById(R.id.chooseMusicSinger);
                ImageView chooseMusicImge=alertView.findViewById(R.id.chooseImage);
                final TextView chooseMusicRatingNum=alertView.findViewById(R.id.chooseMusicRatingtxt);
                final RatingBar chooseMusicRaiting=alertView.findViewById(R.id.chooseRactingBar);
                chooseMusicName.setText(mymusicDataVO.getaName());
                chooseMusicSinger.setText(mymusicDataVO.getaArtist());
                chooseMusicImge.setImageBitmap(mymusicDataVO.getAlbumID());

                chooseMusicRaiting.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
                        chooseMusicRatingNum.setText(String.valueOf(rating));
                    }
                });

                alertDialog.setCustomTitle(alertTitleView);
                alertDialog.setIcon(R.drawable.ic_queue_music_black_48dp);
                alertDialog.setView(alertView);

                alertDialog.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("뮤직","DB저장1");
                        MymusicDataVO mymusicDataVO=list.get(position);

                        if(chooseMusicRatingNum.getText()==null || chooseMusicRatingNum.getText().toString().equals("")){
                            Toast.makeText(view.getContext(),"별점을 매겨주세요!",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        sqLiteDatabase=myDBHelper.getWritableDatabase();
                        String sql="INSERT INTO musicList values('"+mymusicDataVO.getaName()+"',"+"'"+mymusicDataVO.getaArtist()+"',"+
                                "'"+mymusicDataVO.getPath()+"',"+"'"+chooseMusicRatingNum.getText().toString()+"',"+mymusicDataVO.getDulation()+",'"+mymusicDataVO.getAlbomIDNUM()+"');";
                        sqLiteDatabase.execSQL(sql);
                        Log.d("뮤직",mymusicDataVO.getaName());
                        Toast.makeText(v.getContext(),"DB저장됨.",Toast.LENGTH_SHORT).show();
                        sqLiteDatabase.close();
                        Log.d("뮤직","DB저장2");
                        MainActivity.fragmentPagerAdapter.notifyDataSetChanged();

                    }
                });
                alertDialog.setNegativeButton("취소",null);
                alertDialog.show();


            }
        });





    }


    @Override
    public int getItemCount() {
        return (list !=null)?list.size():0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout listLayout;
        private ImageView imgMusic;
        private TextView txtMusicName,txtSinger;
        private ImageButton btnMyMusicList;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            imgMusic=itemView.findViewById(R.id.imgMusic);
            txtMusicName=itemView.findViewById(R.id.txtMusicName);
            txtSinger=itemView.findViewById(R.id.txtSinger);
            btnMyMusicList=itemView.findViewById(R.id.btnMyMusicList);
            listLayout=itemView.findViewById(R.id.listLayout);

        }
    }




}
