package com.example.backend.repository;

import com.example.backend.domain.Track;
import com.example.backend.domain.User;
import com.example.backend.service.track.TrackListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITrackRepository extends JpaRepository<Track,Integer> {

    @Query("SELECT t FROM Track t WHERE t.user = ?1")
    List<Track> findByUser(User user);

    @Modifying
    @Query("DELETE FROM Track t WHERE t.id =: id AND t.user.id =: userId")
    void deleteTrackByIdAndUserId(@Param("id") String id, @Param("userId") int userId);

}
