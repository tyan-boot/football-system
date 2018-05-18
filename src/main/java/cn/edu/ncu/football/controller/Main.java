package cn.edu.ncu.football.controller;

import cn.edu.ncu.football.model.*;
import cn.edu.ncu.football.repo.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class Main {
    @FXML
    public TableColumn<ShotRank, String> teamCol;

    @FXML
    public TableView<PointRank> rankTableView;

    @FXML
    public ComboBox<String> groupType;

    @FXML
    public ComboBox<String> subGroupType;

    @FXML
    public ProgressBar progressBar;

    @FXML
    public Label status;

    @FXML
    public ComboBox<String> calenderTeam;

    @FXML
    public ComboBox<String> calenderPlay;

    @FXML
    private TableView<ShotRank> playerTableView;

    private final PlayerRepo repo;

    private final TeamRepo teamRepo;

    private final RaceRepo raceRepo;

    private final RaceResultRepo raceResultRepo;

    private final ShotResultRepo shotResultRepo;

    @Autowired
    public Main(PlayerRepo repo, JdbcTemplate jdbcTemplate, EntityManager entityManager, TeamRepo teamRepo, RaceRepo raceRepo, RaceResultRepo raceResultRepo, ShotResultRepo shotResultRepo) {
        this.repo = repo;
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
        this.teamRepo = teamRepo;
        this.raceRepo = raceRepo;
        this.raceResultRepo = raceResultRepo;
        this.shotResultRepo = shotResultRepo;
    }

    private final JdbcTemplate jdbcTemplate;

    private final EntityManager entityManager;

    private List<Map<String, ObservableList<PointRank>>> ranks = new ArrayList<>();

    @FXML
    public void initialize() {
        groupType.getItems().addAll("成年组", "校园组");
        groupType.getSelectionModel().select(0);

        groupType.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals("成年组")) {
                    subGroupType.setDisable(true);
                } else {
                    subGroupType.setDisable(false);
                }
            }
        });

        subGroupType.getItems().addAll("男子甲组", "男子乙组", "女子组");
        subGroupType.getSelectionModel().select(0);

        calenderPlay.getItems().addAll("5.1", "5.2", "5.3", "5.4", "5.5", "5.6", "5.7", "5.8", "5.9", "5.10", "5.11", "5.12", "5.13", "5.14", "5.15", "5.16", "5.17", "5.18", "5.19", "5.20", "5.21", "5.22", "5.23", "5.24", "5.25", "5.26", "5.27", "5.28", "5.29", "5.30", "5.31");
        calenderTeam.getItems().addAll("5.1", "5.2", "5.3", "5.4", "5.5", "5.6", "5.7", "5.8", "5.9", "5.10", "5.11", "5.12", "5.13", "5.14", "5.15", "5.16", "5.17", "5.18", "5.19", "5.20", "5.21", "5.22", "5.23", "5.24", "5.25", "5.26", "5.27", "5.28", "5.29", "5.30", "5.31");

        calenderTeam.getSelectionModel().select(0);
        calenderPlay.getSelectionModel().select(0);

        for (int i = 0; i < 4; ++i) {
            ranks.add(new HashMap<>());
        }
    }

    @FXML
    public void loadPlayerClick(MouseEvent actionEvent) {
        /*Iterable<Player> personIter = repo.findAll();

        ObservableList<Player> observableList = FXCollections.observableArrayList();

        personIter.forEach(observableList::add);

        playerTableView.setItems(observableList);*/
    }

    @FXML
    public void loadTeamClick(MouseEvent event) {
        // todo : 加载队伍信息, 并显示出来
    }

    @FXML
    public void loadShotInfo(MouseEvent event) {
        String dateInfo = calenderPlay.getSelectionModel().getSelectedItem();
        String[] split = dateInfo.split("\\.");

        Integer month = Integer.parseInt(split[0]);
        Integer day = Integer.parseInt(split[1]);

        LocalDate date = LocalDate.of(2018, month, day);

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select\n" +
                "  player.name as player_name,\n" +
                "  team.name   as team_name,\n" +
                "  count(1)    as shot_count\n" +
                "from race\n" +
                "  left join race_results on race.race_results_id = race_results.id\n" +
                "  left join shot_result on shot_result.race_result_id = race_results.id\n" +
                "  left join player on player.id = shot_result.player_id\n" +
                "  left join team on player.team_id = team.id\n" +
                "where hold_date = ? \n" +
                "group by player.id\n" +
                "order by shot_count desc;", date.toString());


        ObservableList<ShotRank> observableList = FXCollections.observableArrayList();
        Integer rank = 1;

        while (sqlRowSet.next()) {
            ShotRank shotRank = new ShotRank();

            shotRank.setRank(rank++);
            shotRank.setPlayerName(sqlRowSet.getString("player_name"));
            shotRank.setTeamName(sqlRowSet.getString("team_name"));
            shotRank.setShotCount(sqlRowSet.getInt("shot_count"));

            observableList.add(shotRank);
        }

        playerTableView.setItems(observableList);
    }

    private ObservableList<PointRank> loadRankData(Integer day, Integer type) {
        if (ranks.get(type).containsKey("5." + day)) {
            return ranks.get(type).get("5." + day);
        }

        List<Team> teams = teamRepo.findTeamByType(type);

        LocalDate localDate = LocalDate.of(2018, 5, day);

        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        ObservableList<PointRank> r = FXCollections.observableArrayList();
        ObservableList<PointRank> prevData = FXCollections.observableArrayList();

        if (day > 1) {
            prevData = loadRankData(day - 1, type);
        }

        for (Team team : teams) {
            Integer raceCount = 0,
                    win = 0,
                    draw = 0,
                    lose = 0,
                    inCount = 0,
                    lostCount = 0,
                    point = 0,
                    diff = 0;

            if (day > 1) {
                PointRank pointRank1 = prevData.filtered(pointRank -> pointRank.getTeamName().equals(team.getName())).get(0);

                raceCount += pointRank1.getRace();
                win += pointRank1.getWin();
                draw += pointRank1.getDraw();
                lose += pointRank1.getLose();
                inCount += pointRank1.getInCount();
                lostCount += pointRank1.getLostCount();
                point += pointRank1.getPoint();
            }

            List<Race> race1 = raceRepo.findByTeam1IdAndHoldDate(team.getId(), date);
            List<Race> race2 = raceRepo.findByTeam2IdAndHoldDate(team.getId(), date);

            PointRank pointRank = new PointRank();
            pointRank.setTeamName(team.getName());

            for (Race race : race1) {
                RaceResults raceResults = race.getRaceResults();
                switch (raceResults.getResult()) {
                    case WIN:
                        win++;
                        point += 3;
                        break;
                    case LOSE:
                        lose++;
                    case DRAW:
                        point++;
                        draw++;
                }
                raceCount++;

                Set<ShotResult> shotResults = raceResults.getShotResults();

                for (ShotResult shotResult : shotResults) {
                    Player player = shotResult.getPlayer();

                    if (player.getTeam().equals(team)) {
                        inCount += shotResult.getShotCount();
                    } else {
                        lostCount += shotResult.getShotCount();
                    }
                }
            }

            for (Race race : race2) {
                RaceResults raceResults = race.getRaceResults();
                switch (raceResults.getResult()) {
                    case WIN:
                        lose++;
                        point += 1;
                        break;
                    case LOSE:
                        win++;
                        point += 3;
                    case DRAW:
                        point++;
                        draw++;
                }
                raceCount++;

                Set<ShotResult> shotResults = raceResults.getShotResults();

                for (ShotResult shotResult : shotResults) {
                    Player player = shotResult.getPlayer();

                    if (player.getTeam().equals(team)) {
                        inCount += shotResult.getShotCount();
                    } else {
                        lostCount += shotResult.getShotCount();
                    }
                }
            }

            pointRank.setRace(raceCount);
            pointRank.setWin(win);
            pointRank.setDraw(draw);
            pointRank.setLose(lose);
            pointRank.setInCount(inCount);
            pointRank.setLostCount(lostCount);
            pointRank.setPureInCount(inCount - lostCount);
            pointRank.setAvgInCount(inCount.doubleValue() / raceCount.doubleValue());
            pointRank.setAvgLostCount(lostCount.doubleValue() / raceCount.doubleValue());
            pointRank.setAvgPrueWin((win - lose) / raceCount.doubleValue());
            pointRank.setAvgPoint(point.doubleValue() / raceCount.doubleValue());
            pointRank.setPoint(point);

            r.add(pointRank);
        }

        r.sort((o1, o2) -> o2.getPoint() - o1.getPoint());

        Integer rank = 1;

        for (PointRank pr : r) {
            pr.setRank(rank++);

            FilteredList<PointRank> filtered = prevData.filtered(it -> it.getTeamName().equals(pr.getTeamName()));

            if (!filtered.isEmpty()) {
                PointRank prevRank = filtered.get(0);
                pr.setDiff(prevRank.getRank() - pr.getRank());
            } else {
                pr.setDiff(0);
            }
        }

        ranks.get(type).put("5." + day, r);
        return r;
    }

    @FXML
    public void loadRank(MouseEvent event) {
        String[] split = calenderTeam.getSelectionModel().getSelectedItem().split("\\.");
        Integer day = Integer.valueOf(split[1]);

        Integer type = 0;

        if (groupType.getSelectionModel().getSelectedItem().equals("成年组")) {
            type = 3;
        } else {
            String subType = subGroupType.getSelectionModel().getSelectedItem();

            switch (subType) {
                case "男子甲组":
                    type = 0;
                    break;
                case "男子乙组":
                    type = 1;
                    break;
                case "女子组":
                    type = 2;
                    break;
            }
        }

        if (ranks.get(type).containsKey("5." + day)) {
            rankTableView.setItems(ranks.get(type).get("5." + day));
        } else {
            ObservableList<PointRank> r = loadRankData(day, type);
            ranks.get(type).put("5." + day, r);
            rankTableView.setItems(r);
        }
//        rankTableView.setItems(r);

        System.out.println(ranks);

            /*String sql = "select\n" +
                    "  team.name                                        as teamName,\n" +
                    "  ifnull(T1.racecount, 0)                          as race,\n" +
                    "  ifnull(T1.win, 0)                                as win,\n" +
                    "  ifnull(T1.lose, 0)                               as lose,\n" +
                    "  ifnull(T1.draw, 0)                               as draw,\n" +
                    "  ifnull(T3.inCount, 0)                            as inCount,\n" +
                    "  ifnull(T2.lostCount, 0)                          as lostCount,\n" +
                    "  ifnull(T3.inCount - T2.lostCount, 0)             as pureInCount,\n" +
                    "  ifnull(T3.inCount / t1.racecount, 0)             as avgInCount,\n" +
                    "  ifnull(T2.lostCount / t1.racecount, 0)           as avgLostCount,\n" +
                    "  ifnull((T1.win - T1.lose) / T1.racecount, 0)     as avgPrueWin,\n" +
                    "  ifnull((3 * T1.win + T1.lose) / T1.racecount, 0) as avgPoint,\n" +
                    "  ifnull(3 * T1.win + T1.lose, 0)                  as point\n" +
                    "from team\n" +
                    "  left join (select\n" +
                    "               team.id,\n" +
                    "               team.name,\n" +
                    "               count(1)        as racecount,\n" +
                    "               sum(case when result = 1\n" +
                    "                 then 1\n" +
                    "                   else 0 end) as 'win',\n" +
                    "               sum(case when result = 2\n" +
                    "                 then 1\n" +
                    "                   else 0 end) as 'lose',\n" +
                    "               sum(case when result = 3\n" +
                    "                 then 1\n" +
                    "                   else 0 end) as 'draw'\n" +
                    "             from team\n" +
                    "               left join race on race.team1_id = team.id\n" +
                    "               left join race_results on race_results_id = race_results.id\n" +
                    "             where hold_date = ? \n" +
                    "             group by team1_id) as T1 on team.id = T1.id\n" +
                    "  left join (select\n" +
                    "               team1_id,\n" +
                    "               sum(shot_count) as lostCount\n" +
                    "             from race\n" +
                    "               left join team on team2_id = team.id\n" +
                    "               left join player on player.team_id = team.id\n" +
                    "               left join shot_result\n" +
                    "                 on race.race_results_id = shot_result.race_result_id and shot_result.player_id = player.id\n" +
                    "             where hold_date = ? \n" +
                    "             group by team1_id) as T2 on team.id = T2.team1_id\n" +
                    "  left join (select\n" +
                    "               team1_id,\n" +
                    "               sum(shot_count) as inCount\n" +
                    "             from race\n" +
                    "               left join team on team1_id = team.id\n" +
                    "               left join player on player.team_id = team.id\n" +
                    "               left join shot_result\n" +
                    "                 on race.race_results_id = shot_result.race_result_id and shot_result.player_id = player.id\n" +
                    "             where hold_date = ? \n" +
                    "             group by team1_id) as T3 on team.id = T3.team1_id";

            for (int day = 1; day < 28; ++day) {
                LocalDate date = LocalDate.of(2018, 5, day);

                String dataStr = date.toString();

                List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, dataStr, dataStr, dataStr);

                ObservableList<PointRank> r = FXCollections.observableArrayList();

                for (Map<String, Object> item : maps) {
                    PointRank pointRank = new PointRank();
                    pointRank.setTeamName((String) item.get("teamName"));

                    pointRank.setRace(Integer.valueOf(String.valueOf(item.get("race"))));
                    pointRank.setWin(Integer.valueOf(String.valueOf(item.get("win"))));
                    pointRank.setLose(Integer.valueOf(String.valueOf(item.get("lose"))));
                    pointRank.setDraw(Integer.valueOf(String.valueOf(item.get("draw"))));
                    pointRank.setInCount(Integer.valueOf(String.valueOf(item.get("inCount"))));
                    pointRank.setLostCount(Integer.valueOf(String.valueOf(item.get("lostCount"))));
                    pointRank.setPureInCount(Integer.valueOf(String.valueOf(item.get("pureInCount"))));
                    pointRank.setAvgInCount(Double.valueOf(String.valueOf(item.get("avgInCount"))));
                    pointRank.setAvgLostCount(Double.valueOf(String.valueOf(item.get("avgLostCount"))));
                    pointRank.setAvgPrueWin(Double.valueOf(String.valueOf(item.get("avgPrueWin"))));
                    pointRank.setAvgPoint(Double.valueOf(String.valueOf(item.get("avgPoint"))));
                    pointRank.setPoint((Integer.valueOf(String.valueOf(item.get("point")))));

                    r.add(pointRank);
                }

                ranks.put("5." + day, r);
            }*/
    }

//        rankTableView.setItems(ranks.get(calenderTeam.getSelectionModel().getSelectedItem()));

}
