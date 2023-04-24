package com.example.btlsoc.service.Impl;

import com.example.btlsoc.model.Artist;
import com.example.btlsoc.model.Genre;
import com.example.btlsoc.model.Song;
import com.example.btlsoc.repository.GenreRepository;
import com.example.btlsoc.service.ArtistService;
import com.example.btlsoc.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    @Autowired
    private GenreRepository genreRepository;
    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public Genre findById(int id) {
        return genreRepository.findById(id).get();
    }

    @Override
    public List<Song> findSongsById(int genreId) {
        return genreRepository.findSongsById(genreId);
    }
}
