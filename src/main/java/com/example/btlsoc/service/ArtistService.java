package com.example.btlsoc.service;

import com.example.btlsoc.model.Artist;
import com.example.btlsoc.model.Song;

import java.util.List;

public interface ArtistService {
    List<Artist> findAll();

    Artist findById(int id);

    List<Song> findSongsById(int artistId);
}
