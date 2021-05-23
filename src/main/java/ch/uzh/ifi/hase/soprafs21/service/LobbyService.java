package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyConnectorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class LobbyService {

    private final Logger log = LoggerFactory.getLogger(LobbyService.class);

    private final LobbyRepository lobbyRepository;
    private final LobbyConnectorRepository lobbyConnectorRepository;


    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository,
                        @Qualifier("lobbyConnectorRepository") LobbyConnectorRepository lobbyConnectorRepository) {

        this.lobbyRepository = lobbyRepository;
        this.lobbyConnectorRepository = lobbyConnectorRepository;
    }

    public List<Lobby> getLobbies() {
        return this.lobbyRepository.findAll();
    }

    /**
     * Creates a brand new lobby
     * @param lobbyToCreate
     * @param issuingUser
     * @return
     */
    public Lobby createLobby(Lobby lobbyToCreate, User issuingUser){

        checkIfLobbyAlreadyExists(lobbyToCreate);
        Lobby newLobby = lobbyRepository.save(lobbyToCreate);

        //created new chat
        Chat newChat = new Chat();
        newLobby.setChat(newChat);

        // add issuing user to lobby
        newLobby.addUser(issuingUser);

        //flush lobby
        lobbyRepository.flush();
        return newLobby;

    }

    /**
     * Creates a lobby from an old lobby by taking the chat and reusing it.
     * @param lobbyToCreate
     * @param issuingUser
     * @param lastChat
     * @return
     */


    public Lobby createLobby(Lobby lobbyToCreate, User issuingUser, Chat lastChat){

        checkIfLobbyAlreadyExists(lobbyToCreate);
        Lobby newLobby = lobbyRepository.save(lobbyToCreate);


        newLobby.setChat(lastChat);

        // add issuing user to lobby
        newLobby.addUser(issuingUser);

        //flush lobby
        lobbyRepository.flush();
        return newLobby;

    }



    public Lobby joinLobby(User userToJoin, long lobbyId){
        //check it the lobby exits
        checkIfLobbyExists(lobbyId);
        Lobby foundLobby = this.lobbyRepository.findByLobbyId(lobbyId);

        if (foundLobby.getSize() >= foundLobby.getMaxSize()){
            throw new IllegalStateException("Lobby is already full");
        }
        // adds the user to the lobby with the matching LobbyID
        foundLobby.addUser(userToJoin);
        return foundLobby;

    }

    /**
     * Used when players jump from one lobby to the next with the Play Again function
     * @param lobbyId
     */
    public void playAgain(long lobbyId, User issuingUser) {

        //check it the lobby exits
        checkIfLobbyExists(lobbyId);
        //finds the lobby
        Lobby currentLobby = lobbyRepository.findByLobbyId(lobbyId);

        //firstly leaves the last lobby
        leaveLobby(issuingUser, currentLobby.getLobbyId());

        //finds a potential next lobby through the connector
        LobbyConnector lobbyConnector = this.lobbyConnectorRepository.findByLastLobbyId(lobbyId);

        //case 1: there is no next lobby yet
        if(lobbyConnector == null){

            //create the new connection
            lobbyConnector = new LobbyConnector();
            lobbyConnector.setLastLobbyId(lobbyId);

            Lobby nextLobby = this.createLobby(currentLobby, issuingUser);

        }
        //case 2: there already is a next lobby
        else{

        }





    }

    public List<User> getUsers(long lobbyId) {
        return this.lobbyRepository.findByLobbyId(lobbyId).getUsers();
    }

    private void checkIfLobbyExists(long id) {
        // implement this in the future
        // checks if the id of the lobby is found
        if(lobbyRepository.findByLobbyId(id)==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby doesn't exist dude");
        }
    }

    private void checkIfLobbyAlreadyExists(Lobby lobbyToCreate) {
        // does nothing so far
    }

    public void leaveLobby(User userToRemove, long lobbyId) {
        Lobby targetLobby = lobbyRepository.findByLobbyId(lobbyId);
        if (!targetLobby.getUsers().contains(userToRemove)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User isn't in this lobby");
        }
        targetLobby.removeUser(userToRemove);

        //in case the lobby is now empty, it should delete the lobby
        //TODO: make the lobby delete everything (CHAT GAME ETC)
        if (targetLobby.isEmpty()){
            targetLobby.setChat(null);
            targetLobby.setGame(null);
            lobbyRepository.delete(targetLobby);
            lobbyRepository.flush();
        }
    }

    //TODO: change to find lobby by entity.
    public Lobby findLobbyById(long lobbyId) {
        return lobbyRepository.findByLobbyId(lobbyId);
    }

    public void postMessageToChat(Message msg, long gameID) {
        //finding lobby and chat
        Chat chat = findLobbyById(gameID).getChat();

        //adding message to chat
        chat.addMessage(msg);

        //lobbyRepository.flush();
    }

    public void setLobbyGame(Lobby lobby, Game game) {
        lobby.setGame(game);
    }

}
