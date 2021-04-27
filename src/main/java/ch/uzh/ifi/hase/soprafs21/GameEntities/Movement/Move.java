package ch.uzh.ifi.hase.soprafs21.GameEntities.Movement;


import javax.persistence.*;

@Entity
@Table(name = "MOVE")
public class Move {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moveId;

    

}
