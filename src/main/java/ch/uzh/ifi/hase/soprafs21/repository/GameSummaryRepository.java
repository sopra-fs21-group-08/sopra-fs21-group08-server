package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.GameEntities.GameSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("gameSummaryRepository")
public interface GameSummaryRepository extends JpaRepository<GameSummary, Long> {

    GameSummary findBySummaryId(long id);

}



