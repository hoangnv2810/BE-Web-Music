package com.example.btlsoc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "playlists")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_id")
    private int id;
    private String name;
    private Date dateCreated;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "playlists_details",
            joinColumns = @JoinColumn(name = "playlist_id", referencedColumnName = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id", referencedColumnName = "song_id"))
    private List<Song> songs;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    public User user;
}
