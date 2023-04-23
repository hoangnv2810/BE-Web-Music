package com.example.btlsoc.controller;

import com.example.btlsoc.config.GoogleFileManager;
import com.example.btlsoc.model.Song;
import com.example.btlsoc.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class SongController {
    @Autowired
    GoogleFileManager googleFileManager;

    @Autowired
    SongService songService;

    // Get all file on drive
    @GetMapping("/song/all")
    public ResponseEntity<?> getAllSong() {
        try {
            List<Song> listFile = songService.getAllFile();
            return ResponseEntity.ok().body(listFile);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/song/{id}")
    public ResponseEntity<?> getSong(@PathVariable(name = "id") String id) {
        try {
            Song song = songService.getFileById(id);
            return ResponseEntity.ok().body(song);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping( "/song/upload")
    public ResponseEntity<?> uploadSong(@RequestParam("file") MultipartFile fileUpload) {
        try {
            String fileId = songService.uploadFile(fileUpload, true);
            Song song = songService.getFileById(fileId);
            song.setLyrics("");
            song.setThumbnail("");
            song.setVip(false);
            songService.save(song);
            return ResponseEntity.ok().body(HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
