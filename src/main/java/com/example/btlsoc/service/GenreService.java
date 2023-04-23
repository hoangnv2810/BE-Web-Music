package com.example.btlsoc.service;

import com.example.btlsoc.model.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();

    Genre findById(int id);
}
