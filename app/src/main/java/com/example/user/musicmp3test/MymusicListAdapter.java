package com.example.user.musicmp3test;

import android.content.ContentUris;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MymusicListAdapter extends RecyclerView.Adapter<MymusicListAdapter.CustomViewHolder2> {


    int layout;
    ArrayList<MymusicDataVO> list;
    View view;
    private  MyDBHelper myDBHelper;
    private SQLiteDatabase sqLiteDatabase;

     static boolean isSubMusicStart=false;
     static String submusicPath;


    public MymusicListAdapter(int layout, ArrayList<MymusicDataVO> list) {
        this.layout = layout;
        this.list = list;
    }

    @NonNull
    @Override
    public MymusicListAdapter.CustomViewHolder2 onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        view=LayoutInflater.from(viewGroup.getContext()).inflate(layout,viewGroup,false);

        CustomViewHolder2 viewHolder2=new CustomViewHolder2(view);


        return viewHolder2;
    }

    @Override
    public void onBindViewHolder(@NonNull MymusicListAdapter.CustomViewHolder2 customViewHolder2, final int position) {

       final MymusicDataVO subMymusicDataVO=list.get(position);

            customViewHolder2.subMusicName.setText(subMymusicDataVO.getaName());
            customViewHolder2.subSingerName.setText(subMymusicDataVO.getaArtist());

            //저장한 앨범 아이디를 다시 비트맵으로 바꿔주고 이미지 뷰 셋함.
            int albumID=Integer.parseInt(subMymusicDataVO.getAlbomIDNUM());

            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumID);

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(
                        view.getContext().getContentResolver(), albumArtUri);
                bitmap = Bitmap.createBitmap(bitmap);

            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
                bitmap = BitmapFactory.decodeResource(view.getContext().getResources(),
                        R.drawable.music2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            customViewHolder2.subImgMusic.setImageBitmap(bitmap);
            customViewHolder2.subRatingBar.setRating(Float.parseFloat(subMymusicDataVO.getVoiteNum()));
            customViewHolder2.subBtndataDelite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        MainActivity.music_btnStop.callOnClick();
                        myDBHelper=new MyDBHelper(v.getContext());
                        sqLiteDatabase=myDBHelper.getWritableDatabase();
                        String sql="delete from musicList where musicName='"+list.get(position).getaName()+"';";
                        sqLiteDatabase.execSQL(sql);
                        list.remove(position);
                        notifyItemChanged(position);
                        Toast.makeText(v.getContext(),subMymusicDataVO.getaName()+"가 MY MUSICLIST에 삭제완료 되었습니다.",Toast.LENGTH_SHORT).show();
                        sqLiteDatabase.close();
                        MainActivity.fragmentPagerAdapter.notifyDataSetChanged();

                }
            });

            customViewHolder2.itemView.setTag(position);
            customViewHolder2.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MainActivity.txtCurrentPlayMusic.setText("★★실행 중인 음악은 "+"'"+subMymusicDataVO.getaArtist()+"'"+"의 "+subMymusicDataVO.getaName()+" 입니다! 즐거운 하루 보내세요><★★");
                    MainActivity.txtCurrentPlayMusic.setSelected(true);
                    isSubMusicStart=true;
                    MymusicDataVO mymusicDataVO=list.get(position);
                    Log.d("뮤직",mymusicDataVO.getPath());
                    submusicPath=mymusicDataVO.getPath();
                    MainActivity.btnIsPause=false;
                    MainActivity.music_btnPlay.callOnClick();
                    MainActivity.music_LinearLayout.setVisibility(View.VISIBLE);

                }
            });


    }

    @Override
    public int getItemCount() {
        return (list !=null)?list.size():0;
    }


    public class CustomViewHolder2 extends RecyclerView.ViewHolder {

        TextView subMusicName ,subSingerName;
        ImageView subImgMusic;
        ImageButton subBtndataDelite;
        RatingBar subRatingBar;


        public CustomViewHolder2(@NonNull View itemView) {
            super(itemView);
            subMusicName=itemView.findViewById(R.id.subMusicName);
            subSingerName=itemView.findViewById(R.id.subSingerName);
            subImgMusic=itemView.findViewById(R.id.subImgMusic);
            subBtndataDelite=itemView.findViewById(R.id.subBtndataDelite);
            subRatingBar=itemView.findViewById(R.id.subRatingBar);


        }
    }
}
