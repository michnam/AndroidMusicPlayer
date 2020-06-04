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

    public Song(String title, int id) {
        this.path = "/storage/emulated/0/Android/media/pl.musicplayer/" + title;
        this.id = id;
        this.author = title.split("_")[0];
        this.title = title.split("_")[1].replace("-", " ").split("\\.")[0];
    }
}
