package com.example.music_player.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.music_player.SongPlayer;
import com.example.music_player.enumsAndGlobals.SortKey;
import com.example.music_player.metadata.SongMetadata;
import com.example.music_player.utils.FileUtil;
import com.example.music_player.adapters.SongAdapter;
import com.example.music_player.R;
import com.example.music_player.interfacesAndAbstracts.SortedFragment;
import com.example.music_player.utils.SongUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SongsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongsFragment extends Fragment implements SortedFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SongAdapter musicAdapter;
    private SortKey sortKey;
    private boolean prepared = false;

    private RecyclerView recyclerView;
    private  ProgressBar progressBar;
    TextView noSongFoundTextView;

    public SongsFragment() {
        // Required empty public constructor
    }

    public SongsFragment(SortKey sortKey){
        this.sortKey = sortKey;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment musics_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SongsFragment newInstance(String param1, String param2) {
        SongsFragment fragment = new SongsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.musics_fragment, container, false);
        recyclerView = view.findViewById(R.id.musics_recycleview);
        progressBar = view.findViewById(R.id.progress_bar);
        noSongFoundTextView = view.findViewById(R.id.no_song_found_textview);

        Thread importMusicsThread = new Thread(){
            @Override
            public void run(){
                SongMetadata[] songs = SongUtil.getSongs(view.getContext());

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());

                musicAdapter = new SongAdapter(songs, sortKey, view.getContext());
                sortElements(sortKey);

                Handler mainLooper = new Handler(Looper.getMainLooper());

                mainLooper.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(musicAdapter);
                        progressBar.setVisibility(View.GONE);
                        prepared = true;
                        setEmptyStatusVisibility();
                    }
                });
            }
        };

        importMusicsThread.start();

        FloatingActionButton floatingActionButton = view.findViewById(R.id.TEMP_BUTTON);

        floatingActionButton.setOnClickListener(v -> {
            SongPlayer.showPlayerActivity(view.getContext());
        });

        return view;
    }

    private void setEmptyStatusVisibility(){
        if (musicAdapter == null || noSongFoundTextView == null) return;

        if(musicAdapter.getItemCount() == 0){
            noSongFoundTextView.setVisibility(View.VISIBLE);
        }else{
            noSongFoundTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void sortElements(SortKey sortKey) {
        if(prepared && musicAdapter != null){
            musicAdapter.sortElements(sortKey);
        }
    }

    @Override
    public void enterSearchMode() {
        if(prepared && musicAdapter != null){
            musicAdapter.enterSearchMode();
        }
        setEmptyStatusVisibility();
    }

    @Override
    public void findItemsByName(String name) {
        if(prepared && musicAdapter != null){
            musicAdapter.findItemsByName(name);
        }
        setEmptyStatusVisibility();
    }

    @Override
    public void exitSearchMode() {
        if(prepared && musicAdapter != null){
            musicAdapter.exitSearchMode();
        }
        setEmptyStatusVisibility();
    }
}