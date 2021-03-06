package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.LobbyConnector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("lobbyConnectorRepository")
public interface LobbyConnectorRepository extends JpaRepository<LobbyConnector, Long> {

    LobbyConnector findByLastLobbyId(long id);
    Boolean existsByLastLobbyId(long id);
}