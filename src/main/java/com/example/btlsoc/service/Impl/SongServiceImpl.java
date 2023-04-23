package com.example.btlsoc.service.Impl;


import com.example.btlsoc.config.GoogleFileManager;
import com.example.btlsoc.model.Song;
import com.example.btlsoc.repository.SongRepository;
import com.example.btlsoc.service.SongService;
import com.example.btlsoc.util.ConvertByteToMB;
import com.google.api.services.drive.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;


@Service
public class SongServiceImpl implements SongService {
    @Autowired
    GoogleFileManager googleFileManager;

    @Autowired
    private SongRepository songRepository;


    @Override
    public void save(Song song) {
        songRepository.save(song);
    }

    @Override
    public Song getSongById(String id) {
        return songRepository.findById(id).get();
    }

    @Override
    public List<Song> getAllFile() throws IOException, GeneralSecurityException {

        List<Song> responseList = null;
        List<File> files = googleFileManager.listEverything();
        Song dto = null;

        if (files != null) {
            responseList = new ArrayList<>();
            for (File f : files) {
                if (f.getSize() != null) {
                    dto = getSongById(f.getId());
                    responseList.add(dto);
                }
            }
        }
        return responseList;
    }

    @Override
    public String uploadFile(MultipartFile file, boolean isPublic) {
        String type = "";
        String role = "";
        if (isPublic) {
            // Public file of folder for everyone
            type = "anyone";
            role = "reader";
        } else {
            // Private
            type = "private";
            role = "private";
        }
        return googleFileManager.uploadFile(file, type, role);
    }

    @Override
    public Song getFileById(String fileId) throws GeneralSecurityException, IOException {
        File file = googleFileManager.getSongById(fileId);
        if (file != null) {
            Song dto = getSongById(file.getId());
            return dto;
        }
        return null;
    }
}
