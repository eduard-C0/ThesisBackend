package com.example.backend.controller;

import com.example.backend.config.JwtTokenService;
import com.example.backend.domain.User;
import com.example.backend.service.mapper.TrackMapperImplementation;
import com.example.backend.service.track.TrackDto;
import com.example.backend.service.track.TrackServiceImplementation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/favorite")
@AllArgsConstructor
@CrossOrigin
public class TrackController {

    private final TrackMapperImplementation trackMapper;
    private final TrackServiceImplementation trackService;
    private final JwtTokenService jwtTokenService;

    @PostMapping({"/addToFavorite"})
    public ResponseEntity<ResponseMessage> addTrack(@RequestBody TrackDto track, @RequestHeader("Authorization") String token) throws UnsupportedEncodingException, MessagingException {
        ResponseMessage responseMessage = new ResponseMessage("", "");
        User user = jwtTokenService.getUserFromToken(token);
        Boolean isSaved = trackService.saveTrack(track, user);
        if (isSaved) {
            System.out.println("Added to Favorite!");
            responseMessage.setMessage("Added to Favorite!");
            responseMessage.setCode("200");
            return ResponseEntity.ok(responseMessage);
        }
        System.out.println("Error");
        responseMessage.setMessage("Error");
        responseMessage.setCode("400");
        return ResponseEntity.ok(responseMessage);
    }

    @PostMapping({"/getAllFavorites"})
    public List<TrackDto> addTrack(@RequestHeader("Authorization") String token) throws UnsupportedEncodingException, MessagingException {
        User user = jwtTokenService.getUserFromToken(token);
        List<TrackDto> favorites = trackService.getAllTracks(user);
        if (!favorites.isEmpty()) {
            return favorites;
        }
        System.out.println("Error");
        return Collections.emptyList();
    }

    @PostMapping({"/removeFromFavorites"})
    public ResponseEntity<ResponseMessage> removeTrack(@RequestBody TrackDto track, @RequestHeader("Authorization") String token) throws UnsupportedEncodingException, MessagingException {
        ResponseMessage responseMessage = new ResponseMessage("", "");
        User user = jwtTokenService.getUserFromToken(token);
        Boolean isRemoved = trackService.removeTrack(track, user);
        if (isRemoved) {
            System.out.println("Removed from Favorites!");
            responseMessage.setMessage("Removed from Favorites!");
            responseMessage.setCode("200");
            return ResponseEntity.ok(responseMessage);
        }
        System.out.println("Error");
        responseMessage.setMessage("Error");
        responseMessage.setCode("400");
        return ResponseEntity.ok(responseMessage);
    }


}
