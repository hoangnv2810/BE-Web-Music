package com.example.btlsoc.service;

import com.example.btlsoc.model.Genre;
import com.example.btlsoc.model.Song;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();

    Genre findById(int id);

    List<Song> findSongsById(int genreId);
}
