package com.example.btlsoc.repository;

import com.example.btlsoc.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, String> {
}
