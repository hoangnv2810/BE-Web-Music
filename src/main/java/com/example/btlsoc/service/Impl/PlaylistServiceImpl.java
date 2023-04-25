package com.example.btlsoc.service.Impl;

import com.example.btlsoc.model.Playlist;
import com.example.btlsoc.repository.PlaylistRepository;
import com.example.btlsoc.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;

    @Override
    public List<Playlist> findAllPlaylists(){
        return playlistRepository.findAll();
    }

    @Override
    public List<Playlist> findByUser(int id) {
        return playlistRepository.findByUser(id);
    }

    @Override
    public Playlist findByIdAndUserId(int id, int userId){
        return playlistRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public Playlist save(Playlist playlist){
        return playlistRepository.save(playlist);
    }

    @Override
    public void delete(int id){
        playlistRepository.deleteById(id);
    }


}
