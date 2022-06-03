package com.example.backend.service.mapper;

import com.example.backend.domain.Track;
import com.example.backend.domain.User;
import com.example.backend.service.track.TrackDto;

import java.util.Collection;
import java.util.List;

public interface ITrackMapper {
    TrackDto toService(Track entity);

    Track toEntity(TrackDto dto, User user);

    List<TrackDto> toServiceList(Collection<Track> trackList);
}
