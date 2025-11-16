package server.api;

import commons.LeaderboardEntrySP;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.database.LeaderboardEntrySPRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class LeaderboardEntrySPControllerTest {

    @InjectMocks
    LeaderboardEntrySPController userSP;
    @Mock
    LeaderboardEntrySPRepository repo;

    LeaderboardEntrySPController userSPTestRepo;
    TestLeaderboardSPRepo testRepoObj;

    List<LeaderboardEntrySP> list;

    LeaderboardEntrySP user;

    @BeforeEach
    private void setup() {
        testRepoObj = new TestLeaderboardSPRepo();
        userSPTestRepo = new LeaderboardEntrySPController(testRepoObj);
        MockitoAnnotations.initMocks(this);
        list = new ArrayList<LeaderboardEntrySP>();
        user = new LeaderboardEntrySP("Kolio", 155);
        list.add(user);
    }

    @Test
    void testGetAll() {
        when(repo.findAll()).thenReturn(list);
        List<LeaderboardEntrySP> test = userSP.testGetAll();
        assertNotNull(test);
        assertEquals(list, test);
    }

    @Test
    void testPost() {
        ResponseEntity<LeaderboardEntrySP> response = new ResponseEntity<LeaderboardEntrySP>(user, HttpStatus.valueOf(200));
        LeaderboardEntrySP notEqual = new LeaderboardEntrySP("Hristo", 160);
        when(repo.save(any())).thenReturn(user);
        assertNotNull(userSP.testPost(user));
        assertEquals(user, userSP.testPost(user).getBody());
        assertNotEquals(notEqual, userSP.testPost(user).getBody());
        assertEquals(HttpStatus.valueOf(200), userSP.testPost(user).getStatusCode());
    }

    @Test
    void deleteEntryTest() {
        userSPTestRepo.testPost(user);
        userSPTestRepo.deleteEntryTest(0);
        String res = testRepoObj.results.peek();
        assertEquals("deleted", res);
    }

    @Test
    void updateEntry() {
        LeaderboardEntrySP updated = new LeaderboardEntrySP("Hristo", 160);
        Long O = new Long(0);
        userSPTestRepo.updateEntry(updated, O);
        var res = testRepoObj.getById(O);
        assertNotEquals("Kolio", res.getName());
        assertNotEquals("160", res.getScore());
    }

    @Test
    void testTop10() throws SQLException, ClassNotFoundException {
        LeaderboardEntrySP entry1 = new LeaderboardEntrySP("Hristo", 1);
        LeaderboardEntrySP entry2 = new LeaderboardEntrySP("Hristo", 2);
        LeaderboardEntrySP entry3 = new LeaderboardEntrySP("Hristo", 3);
        LeaderboardEntrySP entry4 = new LeaderboardEntrySP("Hristo", 4);
        LeaderboardEntrySP entry5 = new LeaderboardEntrySP("Hristo", 5);
        LeaderboardEntrySP entry6 = new LeaderboardEntrySP("Hristo", 6);
        LeaderboardEntrySP entry7 = new LeaderboardEntrySP("Hristo", 7);
        LeaderboardEntrySP entry8 = new LeaderboardEntrySP("Hristo", 8);
        LeaderboardEntrySP entry9 = new LeaderboardEntrySP("Hristo", 9);
        LeaderboardEntrySP entry10 = new LeaderboardEntrySP("Hristo", 10);
        LeaderboardEntrySP entry11 = new LeaderboardEntrySP("Hristo", 11);
        LeaderboardEntrySP entry12 = new LeaderboardEntrySP("Hristo", 12);

        List<LeaderboardEntrySP> leaderboard = new ArrayList<>();

        leaderboard.add(entry1);
        leaderboard.add(entry2);
        leaderboard.add(entry3);
        leaderboard.add(entry4);
        leaderboard.add(entry5);
        leaderboard.add(entry6);
        leaderboard.add(entry7);
        leaderboard.add(entry8);
        leaderboard.add(entry9);
        leaderboard.add(entry10);
        leaderboard.add(entry11);
        leaderboard = leaderboard.stream().sorted(Comparator.comparing(LeaderboardEntrySP::getScore).reversed()).collect(Collectors.toList());

        userSPTestRepo.testPost(entry11);
        userSPTestRepo.testPost(entry1);
        userSPTestRepo.testPost(entry2);
        userSPTestRepo.testPost(entry3);
        userSPTestRepo.testPost(entry4);
        userSPTestRepo.testPost(entry5);
        userSPTestRepo.testPost(entry6);
        userSPTestRepo.testPost(entry7);
        userSPTestRepo.testPost(entry8);
        userSPTestRepo.testPost(entry9);
        userSPTestRepo.testPost(entry10);

        var res = userSPTestRepo.getTop10();
        assertNotEquals(leaderboard, res);
        leaderboard.remove(entry2);
        leaderboard = leaderboard.stream().sorted(Comparator.comparing(LeaderboardEntrySP::getScore).reversed()).collect(Collectors.toList());
        assertNotEquals(leaderboard, res);
        leaderboard.add(entry2);
        leaderboard.remove(entry1);
        leaderboard = leaderboard.stream().sorted(Comparator.comparing(LeaderboardEntrySP::getScore).reversed()).collect(Collectors.toList());
        assertEquals(leaderboard, res);
    }
}