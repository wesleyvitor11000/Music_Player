package com.example.music_player.interfaces;

import com.example.music_player.enumsAndGlobals.SortKey;

public interface SortedFragment {
    void sortElements(SortKey sortType);
    void enterSearchMode();
    void findItemsByName(String name);
    void exitSearchMode();
}
