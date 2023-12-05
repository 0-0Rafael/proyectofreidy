// HomeFragment.java
package com.example.proyectusfinal;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.proyectusfinal.Adapters.SliderAdapter;
import com.example.proyectusfinal.Models.SliderModel;
import com.example.proyectusfinal.Utils.SliderTimer;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class HomeFragment extends Fragment {

    private ViewPager slider;
    private ArrayList<SliderModel> sliderModelList;
    private SliderAdapter sliderAdapter;
    private TabLayout sliderIndicator;
    private Timer timer;

    private ListView songListView;
    private MediaPlayer mediaPlayer;
    private List<Uri> songList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener la lista de canciones
        songList = SongManager.getSongList(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        slider = view.findViewById(R.id.slider);
        sliderIndicator = view.findViewById(R.id.slider_indicator);

        sliderModelList = new ArrayList<>();
        timer = new Timer();

        sliderModelList.add(new SliderModel(R.drawable.poster1, "PlayList #1"));
        sliderModelList.add(new SliderModel(R.drawable.poster2, "PlayList #2"));
        sliderModelList.add(new SliderModel(R.drawable.poster3, "PlayList #3"));
        sliderModelList.add(new SliderModel(R.drawable.poster4, "PlayList #4"));

        sliderAdapter = new SliderAdapter(getContext(), sliderModelList);

        slider.setAdapter(sliderAdapter);

        sliderIndicator.setupWithViewPager(slider);

        timer.scheduleAtFixedRate(new SliderTimer(getActivity(), slider, sliderModelList.size()), 3000, 5000);


        // Configurar la funcionalidad de las canciones
        songListView = view.findViewById(R.id.song_list_view);

        List<String> songNames = getSongNames(songList);

        if (songNames.isEmpty()) {
            songNames.add("No se encontraron canciones");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, songNames);
        songListView.setAdapter(adapter);

        // Manejar la reproducción de música cuando se hace clic en una canción
        songListView.setOnItemClickListener((parent, view1, position, id) -> {
            if (!songNames.get(position).equals("No se encontraron canciones")) {
                Uri selectedSongUri = songList.get(position);
                playSong(selectedSongUri);
            }
        });

        return view;
    }

    private List<String> getSongNames(List<Uri> uris) {
        List<String> songNames = new ArrayList<>();

        for (Uri uri : uris) {
            String songName = getFileNameFromUri(uri);
            songNames.add(songName);
        }

        return songNames;
    }

    private String getFileNameFromUri(Uri uri) {
        String fileName = null;

        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Audio.Media.DISPLAY_NAME};
            cursor = requireContext().getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
                fileName = cursor.getString(nameColumn);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return fileName;
    }

    private void playSong(Uri songUri) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.reset();
        }

        try {
            mediaPlayer.setDataSource(requireContext(), songUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}




