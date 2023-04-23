package com.example.btlsoc.repository;

import com.example.btlsoc.model.Artist;
import com.example.btlsoc.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    @Query("select s from Song s where s.artist.id = ?1")
    List<Song> findSongsById(int artistId);
}
