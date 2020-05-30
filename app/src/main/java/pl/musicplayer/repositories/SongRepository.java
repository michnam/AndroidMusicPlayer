package pl.musicplayer.repositories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import pl.musicplayer.models.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SongRepository {
    private List<Song> songs = new ArrayList<>();
    public List<Song> getSongs() {
        return songs;
    }
    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }


    public SongRepository() {
        getAllSongs();
    }

    private void getAllSongs() {
        @SuppressLint("SdCardPath") File rootFolder = new File("/sdcard/Music/AndroidMusicPlayer");
        File[] files = rootFolder.listFiles();

        int counter = 0;
        for (File file : files) {
            this.songs.add(new Song(file.getName(), counter));
            System.out.println("ADDED " + file.getName() + "; " + counter);
            counter++;
        }
    }

    public Song getById(int id) {
        System.out.println("GET BY ID: " + id);
        for(Song song : songs) {
            if(song.getId() == id) {
                return song;
            }
        }
        throw new Error("Could not find song");
    }

    public List<Song> searchByTitle(String titlePhrase) {
        List<Song> songs = getSongs();
        List<Song> result = new ArrayList<>();
        for(Song song : songs) {
            if(song.getTitle().contains(titlePhrase)) {
                result.add(song);
            }
        }
        return result;
    }
}
