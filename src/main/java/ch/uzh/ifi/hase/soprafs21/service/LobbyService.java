package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.GameEntities.Game;
import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.Message;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import org.hibernate.Session;
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

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {

        this.lobbyRepository = lobbyRepository;
    }

    public List<Lobby> getLobbies() {
        return this.lobbyRepository.findAll();
    }

    public Lobby createLobby(Lobby lobbyToCreate, User issuingUser){
        checkIfLobbyAlreadyExists(lobbyToCreate);
        Lobby newLobby = lobbyRepository.save(lobbyToCreate);

        //created new chat
        Chat newChat = new Chat();
        newLobby.setChat(newChat);
        newChat.setLobby(newLobby);

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

    public void removeUser(User userToRemove, long lobbyId){
        Lobby targetLobby = lobbyRepository.findByLobbyId(lobbyId);
        targetLobby.removeUser(userToRemove);

        //in case the lobby is now empty, it should delete the lobby
        //TODO: make the lobby delete everything (CHAT GAME ETC)
        if (targetLobby.isEmpty()){
            targetLobby.getChat();
            lobbyRepository.delete(targetLobby);
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

/*
    public Player getPlayer(User user, Game game){
        Game foundGame = findLobbyById(game.getGameId()).getGame();
        return foundGame.findCorrespondingPlayer(user);
    }
    */
}
