package com.example.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="tracks")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Track implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trackId;
    private String id;
    private String type;
    private String playbackSeconds;
    private String name;
    private String artistName;
    private String artistId;
    private String albumName;
    private String albumId;
    private String previewURL;
    private Boolean isStreamable;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
