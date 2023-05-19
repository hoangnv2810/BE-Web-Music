package com.example.btlsoc.controller;

import com.example.btlsoc.jwt.JwtTokenUtil;
import com.example.btlsoc.model.Playlist;
import com.example.btlsoc.model.Song;
import com.example.btlsoc.model.User;
import com.example.btlsoc.repository.PlaylistRepository;
import com.example.btlsoc.security.CustomUserDetailsService;
import com.example.btlsoc.service.PlaylistService;
import com.example.btlsoc.service.SongService;
import com.sun.security.auth.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private SongService songService;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @GetMapping
    public ResponseEntity<?> getAllPlaylists(Principal principal) {
        User user = (User) customUserDetailsService.loadUserByUsername(principal.getName());
        if (user != null) {
            List<Playlist> playlists = playlistService.findByUser(user.getId());
            return ResponseEntity.ok(playlists);
        } else {
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Set<Song>> getPlaylistById(@PathVariable(value = "id") int playlistId, Principal principal) {
        User user = (User) customUserDetailsService.loadUserByUsername(principal.getName());
        if (user != null) {
            Playlist playlist = playlistService.findByIdAndUserId(playlistId, user.getId());
            if (playlist != null) {
                return ResponseEntity.ok().body(playlist.getSongs());
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public Playlist createPlaylist(@RequestBody Map<String, String> request, Principal principal) {
        User user = (User) customUserDetailsService.loadUserByUsername(principal.getName());
        Playlist playlist = new Playlist();
        playlist.setName(request.get("name"));
        playlist.setDateCreated(new Date());
        playlist.setUser(user);
        return playlistService.save(playlist);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Playlist> updatePlaylist(@PathVariable(value = "id") int playlistId,
//                                                   @RequestBody Playlist playlistDetails) {
//        Playlist playlist = playlistService.findPlaylist(playlistId);
//        if (playlist != null) {
//            playlist.setName(playlistDetails.getName());
//            playlist.setDateCreated(playlistDetails.getDateCreated());
//            playlist.setSongs(playlistDetails.getSongs());
//            playlist.setUser(playlistDetails.getUser());
//
//            Playlist updatedPlaylist = playlistService.save(playlist);
//            return ResponseEntity.ok(updatedPlaylist);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable(value = "id") int playlistId, Principal principal) {
        User user = (User) customUserDetailsService.loadUserByUsername(principal.getName());
        if (user != null) {
            Playlist playlist = playlistService.findByIdAndUserId(playlistId, user.getId());
            if (playlist != null) {
                playlistRepository.deleteById(playlistId);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/song/{songId}")
    public ResponseEntity<Playlist> addSongToPlaylist(@PathVariable(value = "id") int playlistId,
                                                      @PathVariable(value = "songId") String songId, Principal principal) {
        User user = (User) customUserDetailsService.loadUserByUsername(principal.getName());
        Playlist playlist = playlistService.findByIdAndUserId(playlistId, user.getId());
        if (playlist != null) {
            playlist.getSongs().add(songService.getSongById(songId));
            Playlist updatedPlaylist = playlistService.save(playlist);
            return ResponseEntity.ok(updatedPlaylist);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{playlistId}/song/{songId}")
    public ResponseEntity<Playlist> removeSongFromPlaylist(@PathVariable(value = "playlistId") int playlistId,
                                                           @PathVariable(value = "songId") String songId,
                                                           Principal principal) {
        User user = (User) customUserDetailsService.loadUserByUsername(principal.getName());
        Playlist playlist = playlistService.findByIdAndUserId(playlistId, user.getId());
        if (playlist != null) {
            Set<Song> songs = playlist.getSongs();
            for (Song s : songs) {
                if (s.getId().equals(songId)) {
                    songs.remove(s);
                    Playlist updatedPlaylist = playlistService.save(playlist);
                    return ResponseEntity.ok(updatedPlaylist);
                }
            }
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
