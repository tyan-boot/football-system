package cn.edu.ncu.football;

import cn.edu.ncu.football.model.*;
import cn.edu.ncu.football.repo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class FakeDataGenerator implements CommandLineRunner {
    private final TeamRepo teamRepo;

    private final PlayerRepo playerRepo;

    private final RaceRepo raceRepo;

    private final Logger logger;

    private final RaceResultRepo raceResultRepo;

    private final ShotResultRepo shotResultRepo;

    private final PlaceRepo placeRepo;

    private ArrayList<Team> teams = new ArrayList<>();

    private ArrayList<Player> players = new ArrayList<>();

    private ArrayList<Place> places = new ArrayList<>();

    @Autowired
    public FakeDataGenerator(TeamRepo teamRepo, PlayerRepo playerRepo, RaceRepo raceRepo, RaceResultRepo raceResultRepo, ShotResultRepo shotResultRepo, PlaceRepo placeRepo) {
        this.teamRepo = teamRepo;
        this.playerRepo = playerRepo;
        this.raceRepo = raceRepo;
        this.logger = LoggerFactory.getLogger(getClass());
        this.raceResultRepo = raceResultRepo;
        this.shotResultRepo = shotResultRepo;
        this.placeRepo = placeRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Creating mock data...");

        long count = teamRepo.count();

        if (count != 0)
            return;

        generateTeam();

        generatePlayer();

        generatePlace();

        generateRace();

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
        teams.clear();
        teamRepo.findAll().forEach(teams::add);

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
    public void generateRace() {
        teams.clear();
        players.clear();

        teamRepo.findAll().forEach(teams::add);
        playerRepo.findAll().forEach(players::add);

//        placeRepo.findAll().forEach(places::add);

        Random random = new Random();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2018);
        calendar.set(Calendar.MONTH, 4);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        Integer placeRemaining = 20;

        ArrayList<Place> doublePlaces = getDoublePlaces();

        // todo : generate fake race records
        for (int i = 0; i < 4; ++i) {
            ArrayList<Team> teams = new ArrayList<>(teamRepo.findTeamByType(i));

            for (int j = 0; j < teams.size(); ++j) {
                for (int k = j + 1; k < teams.size(); ++k) {
                    if (placeRemaining == 0) {
                        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
                        placeRemaining = 20;
                        doublePlaces = getDoublePlaces();
                    }

                    Team team1 = teams.get(j);
                    Team team2 = teams.get(k);

                    Place place = doublePlaces.get(random.nextInt(placeRemaining));
                    doublePlaces.remove(place);
                    placeRemaining--;

                    Race race = new Race();
                    race.setPlace(place);
                    race.setTeam1(team1);
                    race.setTeam2(team2);
                    race.setStatus(Race.RaceStatus.PROCESSING);
                    race.setHoldDate(new java.sql.Date(calendar.getTimeInMillis()));

                    RaceResults raceResult = generateRaceResult(race);
                    race.setRaceResults(raceResult);

                    raceRepo.save(race);
                }
            }
        }
    }

    private RaceResults generateRaceResult(Race race) {
        Random random = new Random();

        RaceResults raceResult = new RaceResults();

        Team team1 = race.getTeam1();
        Team team2 = race.getTeam2();

        Integer totalCount1 = generateShotResult(random, team1, raceResult);

        Integer totalCount2 = generateShotResult(random, team2, raceResult);

        if (totalCount1 > totalCount2) {
            raceResult.setResult(RaceResults.RaceResult.WIN);
        } else if (totalCount1 < totalCount2) {
            raceResult.setResult(RaceResults.RaceResult.LOSE);
        } else {
            raceResult.setResult(RaceResults.RaceResult.DRAW);
        }

        raceResultRepo.save(raceResult);

        return raceResult;
    }

    @Transactional
    protected Integer generateShotResult(Random random, Team team, RaceResults raceResults) {
        List<Player> players = playerRepo.findByTeam(team);
        Integer totalCount = 0;

        for (Player player : players) {
            ShotResult shotResult = new ShotResult();
            shotResult.setPlayer(player);

            Integer count = random.nextInt(3);
            totalCount += count;

            shotResult.setShotCount(count);
            raceResults.getShotResults().add(shotResult);

            shotResultRepo.save(shotResult);
        }

        return totalCount;
    }

    private ArrayList<Place> getDoublePlaces() {
        ArrayList<Place> arrayList = new ArrayList<>(places);
        arrayList.addAll(places);

        return arrayList;
    }

    @Transactional
    public void generatePlace() {
        for (int i = 0; i < 10; ++i) {
            Place place = new Place();
            place.setName("Place " + i);

            places.add(place);
            placeRepo.save(place);
        }
    }
}
