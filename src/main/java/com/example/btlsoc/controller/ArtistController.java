package com.example.btlsoc.controller;

import com.example.btlsoc.model.Artist;
import com.example.btlsoc.model.Plan;
import com.example.btlsoc.model.Song;
import com.example.btlsoc.service.ArtistService;
import com.example.btlsoc.service.PlanService;
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
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @GetMapping("artist/{id}")
    public ResponseEntity<Artist> getArtist(@PathVariable(name = "id") int id){
        return new ResponseEntity<>(artistService.findById(id), HttpStatus.OK);
    }

    @GetMapping("artist/all")
    public ResponseEntity<List<Artist>> getArtists(){
        return new ResponseEntity<>(artistService.findAll(), HttpStatus.OK);
    }

    @GetMapping("artist/{artistId}/songs")
    public ResponseEntity<?> getSongsByArtist(@PathVariable(name = "artistId") int artistId) {
        Artist artist = artistService.findById(artistId);
        if (artist == null) {
            Map<String, String> message = new HashMap<>();
            message.put("message", "Không tìm thấy tên ca sĩ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        return new ResponseEntity<>(artistService.findSongsById(artistId), HttpStatus.OK);
    }

}
