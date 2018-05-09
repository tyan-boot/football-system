package cn.edu.ncu.football;

import cn.edu.ncu.football.model.Player;
import cn.edu.ncu.football.model.Team;
import cn.edu.ncu.football.repo.PlayerRepo;
import cn.edu.ncu.football.repo.RaceRepo;
import cn.edu.ncu.football.repo.TeamRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Random;

@Component
public class FakeDataGenerator implements CommandLineRunner {
    private final TeamRepo teamRepo;

    private final PlayerRepo playerRepo;

    private final RaceRepo raceRepo;

    private final Logger logger;

    private ArrayList<Team> teams = new ArrayList<>();

    private ArrayList<Player> players = new ArrayList<>();

    @Autowired
    public FakeDataGenerator(TeamRepo teamRepo, PlayerRepo playerRepo, RaceRepo raceRepo) {
        this.teamRepo = teamRepo;
        this.playerRepo = playerRepo;
        this.raceRepo = raceRepo;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Creating mock data...");

        long count = teamRepo.count();

        if (count != 0)
            return;

        generateTeam();

        generatePlayer();

        logger.info("Data initialized.");
    }

    @Transactional
    protected void generateTeam() {
        Random random = new Random();

        // create team
        for (int i = 0; i < 72; ++i) {
            logger.info("Create team {}", i);

            Team team = new Team();
            team.setName("Team " + i);
            team.setType(random.nextInt(4));
            teams.add(team);
        }
        teamRepo.saveAll(teams);
    }

    @Transactional
    protected void generatePlayer() {
        // create player
        for (int i = 0; i < 792; ++i) {
            logger.info("Create user {}", i);

            Player player = new Player();
            Team team = teams.get(i % 72);

            player.setName("Player " + i);
            player.setGrander("f");
            player.setTeam(team);

            players.add(player);
        }
        playerRepo.saveAll(players);
    }

    @Transactional
    protected void generateRace() {
        // todo : generate fake race records
    }
}
