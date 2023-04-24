package com.example.btlsoc.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "songs")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Song {
    @Id
    @Column(name = "song_id")
    private String id;
    private String name;
    private String lyrics;
    private String thumbnail;
    private String audio;
    private String size;
    @Column(name = "is_vip")
    private boolean isVip;

    @ManyToOne
    @JoinColumn(name = "genre_id", referencedColumnName = "genre_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "artist_id", referencedColumnName = "artist_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Artist artist;

}
