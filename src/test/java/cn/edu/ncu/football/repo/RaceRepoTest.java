package cn.edu.ncu.football.repo;

import cn.edu.ncu.football.FakeDataGenerator;
import cn.edu.ncu.football.model.*;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RaceRepoTest {
    @Autowired
    private PlayerRepo playerRepo;

    @Autowired
    private TeamRepo teamRepo;

    @Autowired
    private RaceRepo raceRepo;

    @Autowired
    private RaceResultRepo raceResultRepo;

    @Autowired
    private ShotResultRepo shotResultRepo;

    @Autowired
    private FakeDataGenerator fakeDataGenerator;

    @Test
    public void testRace() {
        Race race = raceRepo.findById(898).orElse(null);

        if (race == null) return;

        RaceResults raceResults = race.getRaceResults();

        Set<ShotResult> shotResults = raceResults.getShotResults();

        System.out.println(shotResults);
    }
}