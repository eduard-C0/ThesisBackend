package com.example.backend.service.track;

import com.example.backend.domain.Track;
import com.example.backend.domain.User;
import com.example.backend.service.user.UserDto;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface ITrackService {

    Boolean saveTrack(TrackDto track, User user) throws UnsupportedEncodingException, MessagingException;

    List<TrackDto> getAllTracks(User user);

    Boolean removeTrack(TrackDto track, User user);
}
