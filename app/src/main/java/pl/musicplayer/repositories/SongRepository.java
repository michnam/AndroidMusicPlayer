package pl.musicplayer.repositories;

import pl.musicplayer.models.Song;

import java.util.ArrayList;
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
        songs.add(new Song(2131689472, "Beautiful day", "U2"));
        songs.add(new Song(2131689473, "Nothing else matters", "Metallica"));
        songs.add(new Song(2131689474, "Everybody hurts", "REM"));
//        songs.add(new Song(4, "Summer of 69'", "Bryan Adams"));
//        songs.add(new Song(5, "Szaman", "Paluch"));
    }

    public Song getById(int id) {
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
