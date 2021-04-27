package ch.uzh.ifi.hase.soprafs21.repository;


import ch.uzh.ifi.hase.soprafs21.network.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository("stationRepository")
public interface StationRepository extends JpaRepository<Station, Serializable>{

    Station findByStationId(long id);
}
