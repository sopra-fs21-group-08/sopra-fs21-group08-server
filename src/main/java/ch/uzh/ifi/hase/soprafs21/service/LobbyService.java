package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@Transactional
public class LobbyService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final LobbyRepository lobbyRepository;

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    public Lobby createLobby(Lobby lobbyToCreat){
        checkIfLobbyAlreadyExists(lobbyToCreat);
        Lobby newLobby = lobbyRepository.save(lobbyToCreat);
        lobbyRepository.flush();
        return newLobby;
    }


    public Lobby joinLobby(User userToJoin, long lobbyId){
        //check it the lobby exits
        checkIfLobbyExists();
        Lobby foundLobby = this.lobbyRepository.findByLobbyId(lobbyId);

        // adds the user to the lobby with the matching LobbyID
        foundLobby.addUser(userToJoin);
        return foundLobby;

    }

    private void checkIfLobbyExists() {
        // implement this in the future
        // will check if the id of the lobby is found
    }


    private void checkIfLobbyAlreadyExists(Lobby lobbyToCreat) {
        // does nothing so far
        // will check if the chosen username is taken
    }
}
