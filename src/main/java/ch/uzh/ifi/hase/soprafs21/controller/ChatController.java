package ch.uzh.ifi.hase.soprafs21.controller;


import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import ch.uzh.ifi.hase.soprafs21.entity.Message;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.ChatDTO.MessageGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.ChatDTO.ReceivedMessageDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.ChatDTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ChatController {

    private final LobbyService lobbyService;
    private final UserService userService;
    private final GameService gameService;

    ChatController(LobbyService lobbyService, UserService userService, GameService gameService) {
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.gameService = gameService;
    }

    @GetMapping("/games/{gameID}/chats")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<MessageGetDTO> getAllMessagesFromChat(@PathVariable("gameID") long gameID){
        Chat chatToReturn = this.lobbyService.findLobbyById(gameID).getChat();
        List<MessageGetDTO> dto = new ArrayList<>();
        for(Message s: chatToReturn.messages){
            dto.add(ChatDTOMapper.INSTANCE.covertEntityToMessageDTO(s));
        }
        return dto;
    }

    @PostMapping("/games/{gameID}/chats")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void writeChatMessage(@RequestBody ReceivedMessageDTO receivedMessageDTO,
                                 @PathVariable("gameID") long gameID){
        Message msg = ChatDTOMapper.INSTANCE.convertReceivedMessageDTOtoMessage(receivedMessageDTO);

        //find issuing User
        User user = ChatDTOMapper.INSTANCE.convertReceivedMessageDTOtoUser(receivedMessageDTO);

        User foundUser = userService.findUserByEntity(user);
        gameService.isUserInGame(gameID, foundUser);

        //add authenticated username to message
        msg.setUsername(foundUser.getUsername());

        lobbyService.postMessageToChat(msg, gameID);
    }
}
