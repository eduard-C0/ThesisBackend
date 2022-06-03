package com.example.backend.service.track;

import com.example.backend.domain.Track;
import com.example.backend.domain.User;
import com.example.backend.repository.ITrackRepository;
import com.example.backend.repository.IUserRepository;
import com.example.backend.service.mapper.TrackMapperImplementation;
import com.example.backend.service.mapper.UserMapperImplementation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@AllArgsConstructor
@Service
public class TrackServiceImplementation implements ITrackService {

    private TrackMapperImplementation trackMapper;
    private final ITrackRepository trackRepository;

    @Override
    public Boolean saveTrack(TrackDto trackDto, User user) throws UnsupportedEncodingException, MessagingException {
        Track trackEntity = trackMapper.toEntity(trackDto,user);
        try {
            trackRepository.save(trackEntity);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public List<TrackDto> getAllTracks(User user) {
        return trackMapper.toServiceList(trackRepository.findByUser(user));
    }

    @Override
    @Transactional
    public Boolean removeTrack(TrackDto trackDto, User user) {
        Track trackEntity = trackMapper.toEntity(trackDto,user);
        try {
            trackRepository.deleteByTrackId(trackEntity.getId(), user.getId());
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
