package com.example.btlsoc.service.Impl;

import com.example.btlsoc.model.Artist;
import com.example.btlsoc.model.Song;
import com.example.btlsoc.repository.ArtistRepository;
import com.example.btlsoc.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistServiceImpl implements ArtistService {
    @Autowired
    private ArtistRepository artistRepository;
    @Override
    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    @Override
    public Artist findById(int id) {
        return artistRepository.findById(id).get();
    }

    @Override
    public List<Song> findSongsById(int artistId) {
        return artistRepository.findSongsById(artistId);
    }

}
