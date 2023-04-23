package com.example.btlsoc.service;

import com.example.btlsoc.model.Song;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface SongService {
    void save(Song song);

    Song getSongById(String id);


    List<Song> getAllFile() throws IOException, GeneralSecurityException;

    String uploadFile(MultipartFile file, boolean isPublic);

    Song getFileById(String fileId) throws GeneralSecurityException, IOException;
}
