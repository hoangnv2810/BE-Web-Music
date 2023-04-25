package com.example.btlsoc.repository;

import com.example.btlsoc.model.Playlist;
import com.example.btlsoc.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {

    @Query("select p from Playlist p where p.user.id = ?1")
    List<Playlist> findByUser(int id);

    @Query("select p from Playlist p where p.id = ?1 and p.user.id = ?2")
    Playlist findByIdAndUserId(int playlistId, int userId);
}
