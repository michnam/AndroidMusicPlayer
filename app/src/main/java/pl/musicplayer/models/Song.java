package pl.musicplayer.models;

import android.annotation.SuppressLint;

public class Song {
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private int id;
    private String title;
    private String author;
    private String path;
    public static String pathToAppFolder = "/storage/emulated/0/Android/media/pl.musicplayer/";

    public Song(String title, int id) {
        this.path = pathToAppFolder + title;
        this.id = id;
        String[] tmp = title.split("_");
        this.author = tmp[0];
        if (tmp[1].contains("-"))
            this.title = tmp[1].replace("-", " ").split("\\.")[0];
        else
            this.title = tmp[1].split("\\.")[0];
    }
}
