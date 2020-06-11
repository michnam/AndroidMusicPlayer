package pl.musicplayer.repositories;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.musicplayer.models.Song;

/**
 * Naming mp3 files
 * All files should be named like band_title.mp3. If title contains whitespaces use band_long-title.mp3 convention.
 *
 * Storing mp3 files
 * All files should be stored inside /storage/emulated/0/Android/media/pl.musicplayer/ folder.
 */
public class SongRepository extends SQLiteOpenHelper {
    private final String TAG = "SongRepository";
    private static final String DATABASE_NAME = "AndroidMusicPlayer";
    public static int currentSong = 0;

    public SongRepository(Context context) {
        super(context, DATABASE_NAME, null, 1);
        checkForNewSongs(context);
    }

    /**
     * Compare number of local songs and songs in database
     * If these numbers are not equal drop database and insert all songs
     * @param context
     */
    private void checkForNewSongs(Context context) {
        List<Song> localSongs = new ArrayList<>();
        List<Song> dbSongs = this.getAllSongs();
        File file = new File(context.getExternalMediaDirs()[0].getPath());
        File[] files = file.listFiles();

        assert files != null;
        for (int i = 0; i < files.length; i++) {
            localSongs.add(new Song(files[i].getName(), i));
        }

        if (localSongs.size() != dbSongs.size()) {
            this.onUpgrade(this.getReadableDatabase(), 1, 1);
            for (Song song : localSongs)
                this.insertFilePath(song.getTitle());
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table song " +
                "(id integer primary key," +
                "name text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS song");
        onCreate(db);
    }

    private void insertFilePath(String name) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            db.insert("song", null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSong(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        id++;// necessary because sqlite indexing starts on 1
        db.delete("song","id = ?", new String[] {Integer.toString(id)});
    }

    public Song getSongById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        id++; // necessary because sqlite indexing starts on 1
        @SuppressLint("Recycle") Cursor res = db.rawQuery("select * from song where id=?", new String[] {String.valueOf(id)});
        res.moveToFirst();
        return new Song(res.getString(res.getColumnIndex("name")), Integer.parseInt(res.getString(res.getColumnIndex("id"))) - 1);
    }

    public Song getCurrentSong() {
        return getSongById(currentSong);
    }

    public Song getNextSong() {
        currentSong++;
        if (currentSong >= getAllSongs().size())
            currentSong = 0;
        return getCurrentSong();
    }

    public Song getPreviousSong() {
        currentSong--;
        if (currentSong < 0)
            currentSong = getAllSongs().size() - 1;
        return getCurrentSong();
    }

    public ArrayList<Song> getAllSongs() {
        ArrayList<Song> songs = new ArrayList<Song>();

        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor res =  db.rawQuery( "select * from song", null );
        res.moveToFirst();

        int counter = 0;
        while(!res.isAfterLast()){
            songs.add(new Song(res.getString(res.getColumnIndex("name")), counter));
            res.moveToNext();
            counter++;
        }
        return songs;
    }

    public List<Song> searchByTitle(String phrase) {
        ArrayList<Song> songs = new ArrayList<Song>();

        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor res = db.rawQuery("select * from song where name like ?", new String[] {"%" + phrase + "%"});
        res.moveToFirst();
        while(!res.isAfterLast()){
            songs.add(new Song(res.getString(res.getColumnIndex("name")), Integer.parseInt(res.getString(res.getColumnIndex("id"))) - 1));
            res.moveToNext();
        }
        if(songs.size() > 0)
            Log.i(TAG, "searchByTitle -> song id: " + songs.get(0).getId());
        return songs;
    }
}
