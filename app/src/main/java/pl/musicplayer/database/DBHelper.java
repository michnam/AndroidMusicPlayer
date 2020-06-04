package pl.musicplayer.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.musicplayer.models.Song;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "AndroidMusicPlayer";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.onUpgrade(this.getReadableDatabase(), 1, 1);
        File file = new File(context.getExternalMediaDirs()[0].getPath());
        File[] files = file.listFiles();

        for (File value : files) {
            this.insertFilePath(value.getName());
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

    public boolean insertFilePath(String name) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            db.insert("song", null, contentValues);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getFilePath(int id) {
        // tmp solution
        List<Song> songs = this.getAllSongs();
        for (Song song : songs) {
            if (song.getId() == id) {
                return song.getPath();
            }
        }
        return null;

//        SQLiteDatabase db = this.getReadableDatabase();
//        @SuppressLint("Recycle") Cursor res = db.rawQuery("select * from song where id=" + id + "", null);
//        return res.getString(res.getColumnIndex("name"));
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
}
