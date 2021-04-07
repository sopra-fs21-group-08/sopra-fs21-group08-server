package ch.uzh.ifi.hase.soprafs21.GameEntities;


import javax.persistence.*;


@Entity
@Table(name= "GAME")
public class Game {

    @Id
    @GeneratedValue
    private Long id;

}
