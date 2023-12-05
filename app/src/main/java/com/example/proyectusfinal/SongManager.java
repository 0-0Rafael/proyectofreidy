//SongManager.java
package com.example.proyectusfinal;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SongManager {
    public static List<Uri> getSongList(Context context) {
        List<Uri> songList = new ArrayList<>();

        ContentResolver contentResolver = context.getContentResolver();
        Uri songUriInternal = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        Uri songUriExternal = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        // Consultar solo archivos de audio
        String selection = MediaStore.Audio.Media.MIME_TYPE + "=? or " + MediaStore.Audio.Media.MIME_TYPE + "=?";
        String[] selectionArgs = new String[]{"audio/mpeg", "audio/mp3"};

        Cursor songCursorInternal = contentResolver.query(songUriInternal, null, selection, selectionArgs, null);
        Cursor songCursorExternal = contentResolver.query(songUriExternal, null, selection, selectionArgs, null);

        addSongsToList(songCursorInternal, songList, songUriInternal);
        addSongsToList(songCursorExternal, songList, songUriExternal);

        if (songCursorInternal != null) {
            songCursorInternal.close();
        }

        if (songCursorExternal != null) {
            songCursorExternal.close();
        }

        Log.d("SongManager", "Número de canciones encontradas: " + songList.size());
        for (Uri uri : songList) {
            Log.d("SongManager", "Canción URI: " + uri.toString());
        }

        return songList;
    }

    private static void addSongsToList(Cursor cursor, List<Uri> songList, Uri baseUri) {
        if (cursor != null && cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);

            do {
                long id = cursor.getLong(idColumn);
                Uri contentUri = ContentUris.withAppendedId(baseUri, id);
                songList.add(contentUri);
            } while (cursor.moveToNext());
        }
    }
}
