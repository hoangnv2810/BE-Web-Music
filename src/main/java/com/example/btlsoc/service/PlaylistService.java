package com.example.btlsoc.service;

import com.example.btlsoc.model.Playlist;

import java.util.List;

public interface PlaylistService {
    List<Playlist> findAllPlaylists();
    List<Playlist> findByUser(int id);

    Playlist findByIdAndUserId(int id, int userId);

    Playlist save(Playlist playlist);

    void delete(int id);

}
