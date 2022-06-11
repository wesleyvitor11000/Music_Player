package com.example.music_player.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.music_player.MainActivity;
import com.example.music_player.R;
import com.example.music_player.adapters.PlaylistAdapter;
import com.example.music_player.enumsAndGlobals.SortKey;
import com.example.music_player.interfacesAndAbstracts.InputEnterCallback;
import com.example.music_player.interfacesAndAbstracts.SortedFragment;
import com.example.music_player.metadata.PlaylistMetadata;
import com.example.music_player.utils.FileUtil;
import com.example.music_player.utils.PlaylistsUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaylistsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaylistsFragment extends Fragment implements SortedFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private PlaylistAdapter playlistAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlaylistsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaylistsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaylistsFragment newInstance(String param1, String param2) {
        PlaylistsFragment fragment = new PlaylistsFragment();
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
        View view =  inflater.inflate(R.layout.playlists_fragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.playlists_recycleview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);

        ArrayList<PlaylistMetadata> playlists = PlaylistsUtil.getPlaylists(view.getContext());
        playlistAdapter = new PlaylistAdapter(playlists, view.getContext());

        PlaylistsUtil.addOnPlaylistsChange(() -> playlistAdapter.updatePlaylists(PlaylistsUtil.getPlaylists()));

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(playlistAdapter);

        FloatingActionButton addPlaylistButton = view.findViewById(R.id.add_playlist_floating_button);

        addPlaylistButton.setOnClickListener(v -> {
            MainActivity.showTextInputDialog(view.getContext(), "Playlist name", new InputEnterCallback() {
                @Override
                public void onInputEnter(String input) {
                    PlaylistMetadata playlist = new PlaylistMetadata(input, 0);
                    PlaylistsUtil.addPlaylist(playlist, view.getContext());

                    playlistAdapter.notifyInsertion();
                }

                @Override public void onInputEnter(int position) {}
            });
        });



        return view;
    }

    @Override
    public void sortElements(SortKey sortKey) {

    }

    @Override
    public void enterSearchMode() {

    }

    @Override
    public void findItemsByName(String name) {

    }

    @Override
    public void exitSearchMode() {

    }
}