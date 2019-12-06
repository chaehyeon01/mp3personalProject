package com.example.user.musicmp3test;

import android.graphics.Bitmap;

public class MymusicDataVO {


    private String aName;
    private String aAlbum;
    private String aArtist;
    private String path;
    private long dulation;
    private Bitmap albumID;
    private String voiteNum;
    private String albomIDNUM;



    public MymusicDataVO(String aName, String aArtist, String path, String voiteNum, long dulation,String albomIDNUM) {
        this.aName = aName;
        this.aArtist = aArtist;
        this.path = path;
        this.dulation = dulation;
        this.voiteNum = voiteNum;
        this.albomIDNUM=albomIDNUM;
    }

    public MymusicDataVO(String aName, String aAlbum, String aArtist, String path, long dulation, Bitmap albumID,String albomIDNUM) {
        this.aName = aName;
        this.aAlbum = aAlbum;
        this.aArtist = aArtist;
        this.path = path;
        this.dulation = dulation;
        this.albumID = albumID;
        this.albomIDNUM=albomIDNUM;
    }

    public String getaName() {
        return aName;
    }

    public void setaName(String aName) {
        this.aName = aName;
    }

    public String getaAlbum() {
        return aAlbum;
    }

    public void setaAlbum(String aAlbum) {
        this.aAlbum = aAlbum;
    }

    public String getaArtist() {
        return aArtist;
    }

    public void setaArtist(String aArtist) {
        this.aArtist = aArtist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDulation() {
        return dulation;
    }

    public void setDulation(long dulation) {
        this.dulation = dulation;
    }

    public Bitmap getAlbumID() {
        return albumID;
    }

    public void setAlbumID(Bitmap albumID) {
        this.albumID = albumID;
    }

    public String getVoiteNum() {
        return voiteNum;
    }

    public void setVoiteNum(String voiteNum) {
        this.voiteNum = voiteNum;
    }


    public String getAlbomIDNUM() {
        return albomIDNUM;
    }

    public void setAlbomIDNUM(String albomIDNUM) {
        this.albomIDNUM = albomIDNUM;
    }
}
