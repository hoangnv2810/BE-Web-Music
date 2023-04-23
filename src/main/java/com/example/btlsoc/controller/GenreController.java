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

import java.util.List;

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

}
