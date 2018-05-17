package cn.edu.ncu.football.controller;

import cn.edu.ncu.football.model.PointRank;
import cn.edu.ncu.football.model.ShotRank;
import cn.edu.ncu.football.repo.PlayerRepo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
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

    @Autowired
    public Main(PlayerRepo repo, JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.repo = repo;
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    private final JdbcTemplate jdbcTemplate;

    private final EntityManager entityManager;

    private Map<String, ObservableList<PointRank>> ranks = new HashMap<>();

    @FXML
    public void initialize() {
        groupType.setPromptText("成年组");
        groupType.getItems().addAll("成年组", "校园组");

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

        subGroupType.setPromptText("男子甲组");
        subGroupType.getItems().addAll("男子甲组", "男子乙组", "女子组");

        calenderTeam.getItems().addAll("5.1", "5.2", "5.3", "5.4", "5.5", "5.6", "5.7", "5.8", "5.9", "5.10", "5.11", "5.12", "5.13", "5.14", "5.15", "5.16", "5.17", "5.18", "5.19", "5.20", "5.21", "5.22", "5.23", "5.24", "5.25", "5.26");
        calenderPlay.getItems().addAll("5.1", "5.2", "5.3", "5.4", "5.5", "5.6", "5.7", "5.8", "5.9", "5.10", "5.11", "5.12", "5.13", "5.14", "5.15", "5.16", "5.17", "5.18", "5.19", "5.20", "5.21", "5.22", "5.23", "5.24", "5.25", "5.26");

        calenderTeam.getSelectionModel().select(0);
        calenderPlay.getSelectionModel().select(0);
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

    @FXML
    public void loadRank(MouseEvent event) {
        if (ranks.isEmpty()) {
            String sql = "select\n" +
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
                    pointRank.setPrueInCount(Integer.valueOf(String.valueOf(item.get("pureInCount"))));
                    pointRank.setAvgInCount(Double.valueOf(String.valueOf(item.get("avgInCount"))));
                    pointRank.setAvgLostCount(Double.valueOf(String.valueOf(item.get("avgLostCount"))));
                    pointRank.setAvgPrueWin(Double.valueOf(String.valueOf(item.get("avgPrueWin"))));
                    pointRank.setAvgPoint(Double.valueOf(String.valueOf(item.get("avgPoint"))));
                    pointRank.setPoint((Integer.valueOf(String.valueOf(item.get("point")))));

                    r.add(pointRank);
                }

                ranks.put("5." + day, r);
            }
        }

        rankTableView.setItems(ranks.get(calenderTeam.getSelectionModel().getSelectedItem()));
    }
}
