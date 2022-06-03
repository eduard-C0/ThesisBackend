package com.example.backend.service.track;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TrackDto {
    Long trackId;
    String userId;
    String type;
    String id;
    String playbackSeconds;
    String name;
    String artistName;
    String artistId;
    String albumName;
    String albumId;
    String previewURL;
    Boolean isStreamable;
}
