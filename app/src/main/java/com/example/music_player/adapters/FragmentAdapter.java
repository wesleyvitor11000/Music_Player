package com.example.music_player.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.music_player.enumsAndGlobals.SortKey;
import com.example.music_player.fragments.ArtistsFragment;
import com.example.music_player.fragments.MusicsFragment;
import com.example.music_player.fragments.PlaylistsFragment;
import com.example.music_player.interfaces.SortedFragment;

public class FragmentAdapter extends FragmentStateAdapter {

    private final int numberOfFragments = 3;
    private final SortedFragment[] fragments;
    private final SortKey[] sortKeys;

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, SortKey[] sortKeys) {
        super(fragmentManager, lifecycle);
        fragments = new SortedFragment[numberOfFragments];
        this.sortKeys = sortKeys;
    }

    public void sortElementsOnFragment(int position, SortKey sorttype){
        fragments[position].sortElements(sorttype);
    }

    public void enterSearchModeOnFragment(int position){
        fragments[position].enterSearchMode();
    }

    public void findItemByNameOnFragment(int position, String name){
        fragments[position].findItemsByName(name);
    }

    public void exitSearchModeOnFragment(int position){
        fragments[position].exitSearchMode();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        SortedFragment fragment;
        switch (position){
            case 1:
                fragment = new PlaylistsFragment();
                break;
            case 2:
                fragment = new ArtistsFragment();
                break;
            default:
                fragment = new MusicsFragment(sortKeys[0]);
                break;
        }

        fragments[position] = fragment;
        return (Fragment) fragment;
    }

    @Override
    public int getItemCount() {
        return numberOfFragments;
    }
}
