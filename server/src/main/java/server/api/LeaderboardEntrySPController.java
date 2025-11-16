package server.api;

import commons.LeaderboardEntrySP;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import server.database.LeaderboardEntrySPRepository;

import java.sql.*;


@RestController
@RequestMapping("/api/leaderboardsp")
public class LeaderboardEntrySPController {

    private LeaderboardEntrySPRepository repo;

    public LeaderboardEntrySPController(LeaderboardEntrySPRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/")
    public List<LeaderboardEntrySP> testGetAll() {
        return repo.findAll();
    }

    /**
     * Gets the top 10 in the Single player database
     *
     * @return returns a list of the top 10 entries
     * @throws ClassNotFoundException
     * @throws SQLException           commented code is a different non-working way to implement the same thing It will be useful if we manage to also get this thing working :)
     */
    @GetMapping("/top10")
    public List<LeaderboardEntrySP> getTop10() throws ClassNotFoundException, SQLException {
        List<LeaderboardEntrySP> result;
        result = repo.findAllByOrderByScoreDescSP();
        return result;
    }

    @PostMapping("/post")
    public ResponseEntity<LeaderboardEntrySP> testPost(@Validated @RequestBody LeaderboardEntrySP entrySP) {
        LeaderboardEntrySP saved = repo.save(entrySP);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEntryTest(@PathVariable long id) {
        repo.deleteById(id);
    }

    @PutMapping("/put/{id}")
    public LeaderboardEntrySP updateEntry(@RequestBody LeaderboardEntrySP updatedEntry, @PathVariable Long id) {
        return repo.findById(id)
                .map(leaderboardEntrySP -> {
                    leaderboardEntrySP.setName(updatedEntry.getName());
                    leaderboardEntrySP.setScore(updatedEntry.getScore());
                    return repo.save(leaderboardEntrySP);
                })
                .orElseGet(() -> {
                    updatedEntry.setId(id);
                    return repo.save(updatedEntry);
                });
    }
}
