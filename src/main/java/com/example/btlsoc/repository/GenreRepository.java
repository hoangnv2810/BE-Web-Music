package com.example.btlsoc.repository;

import com.example.btlsoc.model.Genre;
import com.example.btlsoc.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
    @Query("select s from Song s where s.genre.id = ?1")
    List<Song> findSongsById(int genreId);
}
