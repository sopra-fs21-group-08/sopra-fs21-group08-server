package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import ch.uzh.ifi.hase.soprafs21.rest.ChatDTO.ChatGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.ChatDTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController

public class ChatController {

    private final LobbyService lobbyService;

    ChatController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @GetMapping("/games/{gameID}/chats")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ChatGetDTO getAllMessagesFromChat(@PathVariable("gameID") long gameID){
        Chat chatToReturn = this.lobbyService.findLobbyById(gameID).getChat();
        return ChatDTOMapper.INSTANCE.covertEntityToChatGetDTO(chatToReturn);
    }
}
