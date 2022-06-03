package com.example.backend.service.mapper;

import com.example.backend.domain.Track;
import com.example.backend.domain.User;
import com.example.backend.service.track.TrackDto;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrackMapperImplementation implements ITrackMapper {

    @Override
    public TrackDto toService(Track entity) {
        return TrackDto.builder()
                .id(entity.getId())
                .albumId(entity.getAlbumId())
                .albumName(entity.getAlbumName())
                .artistId(entity.getArtistId())
                .artistName(entity.getArtistName())
                .isStreamable(entity.getIsStreamable())
                .type(entity.getType())
                .playbackSeconds(entity.getPlaybackSeconds())
                .previewURL(entity.getPreviewURL())
                .name(entity.getName())
                .build();
    }

    @Override
    public Track toEntity(TrackDto dto, User user) {
        return   Track.builder()
                .trackId(dto.getTrackId())
                .id(dto.getId())
                .albumId(dto.getAlbumId())
                .albumName(dto.getAlbumName())
                .artistId(dto.getArtistId())
                .artistName(dto.getArtistName())
                .isStreamable(dto.getIsStreamable())
                .type(dto.getType())
                .playbackSeconds(dto.getPlaybackSeconds())
                .previewURL(dto.getPreviewURL())
                .name(dto.getName())
                .user(user)
                .build();
    }

    @Override
    public List<TrackDto> toServiceList(Collection<Track> trackList) {
        return trackList
                .stream()
                .map(this::toService)
                .collect(Collectors.toList());
    }
}
