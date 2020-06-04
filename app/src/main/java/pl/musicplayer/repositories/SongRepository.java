package pl.musicplayer.repositories;

import pl.musicplayer.models.Song;

import java.util.List;

/**
 * Naming mp3 files
 * All files should be named like band_title.mp3. If title contains whitespaces use band_long-title.mp3 convention.
 *
 * Storing mp3 files
 * All files should be stored inside /storage/emulated/0/Android/media/pl.musicplayer/ folder.
 */
public class SongRepository {
    public static int currentSongId = 0;

    public static List<Song> searchByTitle(String titlePhrase) {
//        List<Song> result = new ArrayList<>();
//        for(Song song : songs) {
//            if(song.getTitle().contains(titlePhrase)) {
//                result.add(song);
//            }
//        }
//        return result;
        return null;
    }
}
