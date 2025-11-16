package server.database;

import commons.LeaderboardEntrySP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaderboardEntrySPRepository extends JpaRepository<LeaderboardEntrySP, Long> {

    /**
     * Here a query is created with a specified criteria
     *
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT * FROM LEADERBOARD_ENTRYSP ORDER BY score DESC LIMIT 10")
    public List<LeaderboardEntrySP> findAllByOrderByScoreDescSP();
}
