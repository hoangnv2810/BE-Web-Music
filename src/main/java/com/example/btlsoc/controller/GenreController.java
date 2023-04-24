package com.example.btlsoc.controller;

import com.example.btlsoc.model.Artist;
import com.example.btlsoc.model.Genre;
import com.example.btlsoc.service.ArtistService;
import com.example.btlsoc.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GenreController {
    @Autowired
    private GenreService genreService;

    @GetMapping("genre/{id}")
    public ResponseEntity<Genre> getArtist(@PathVariable(name = "id") int id){
        return new ResponseEntity<>(genreService.findById(id), HttpStatus.OK);
    }

    @GetMapping("genre/all")
    public ResponseEntity<List<Genre>> getPlans(){
        return new ResponseEntity<>(genreService.findAll(), HttpStatus.OK);
    }

    @GetMapping("genre/{genreId}/songs")
    public ResponseEntity<?> getSongsByGenre(@PathVariable(name = "genreId") int genreId) {
        Genre genre = genreService.findById(genreId);
        if (genre == null) {
            Map<String, String> message = new HashMap<>();
            message.put("message", "Không tìm thấy thể loại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        return new ResponseEntity<>(genreService.findSongsById(genreId), HttpStatus.OK);
    }

}
