package com.aakash.recorder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

import static com.aakash.recorder.TabFragment1.RECORD_AUDIO;

public class TabFragment2 extends Fragment {

    ArrayAdapter adapter;
    Boolean record_status = true;
    Button playorpause;
    MediaPlayer mediaPlayer;
    ListView listView;
    int length;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.tab_fragment_2, container, false);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                    RECORD_AUDIO);
            try {
                Thread.sleep(5000);
                getfile();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else {
            getfile();
        }

        playorpause = view.findViewById(R.id.play_pause);
        playorpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(record_status)
                    pause_recording();
                else
                    resume_recording();

            }
        });
        listView = (ListView) view.findViewById(R.id.record_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

              String play_address = listView.getItemAtPosition(position).toString();
                Toast.makeText(getContext(),play_address,Toast.LENGTH_LONG).show();
                Log.i("Files",play_address);
                play_recording(play_address);
            }
        });
        return  view;
    }

    private void resume_recording() {
        try {
            playorpause.setText("Pause");
            mediaPlayer.seekTo(length);
            mediaPlayer.start();
            Toast.makeText(getContext(), "Resuming Audio", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // make something
        }
        record_status = true;
    }

    private void pause_recording() {
        try {
            playorpause.setText("Play");
            mediaPlayer.pause();
            length = mediaPlayer.getCurrentPosition();
            Toast.makeText(getContext(), "Pausing Audio", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // make something
        }
        record_status = false;
    }

    private void play_recording(String filetitle) {

        mediaPlayer = new MediaPlayer();
        String outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/saved_records/" + filetitle;
        playorpause.setText("Pause");
        record_status = true;
        try {
            mediaPlayer.setDataSource(outputFile);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(getContext(), "Playing Audio", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // make something
        }
    }

    private void getfile() {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/saved_records";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        String[] filename = new String[files.length];
        for (int i = 0; i < files.length; i++)
        {
            filename[i] = files[i].getName();
            Log.d("Files", "FileName:" + files[i].getName());
        }
        adapter = new ArrayAdapter<>(getActivity(),
                R.layout.listview, filename);
    }
}
