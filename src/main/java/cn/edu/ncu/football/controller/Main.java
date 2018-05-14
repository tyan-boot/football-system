package cn.edu.ncu.football.controller;

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

@Service
public class Main {
    @FXML
    public TableColumn<ShotRank, String> teamCol;

    @FXML
    public TableView teamTableView;

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
    public Main(PlayerRepo repo, JdbcTemplate jdbcTemplate) {
        this.repo = repo;
        this.jdbcTemplate = jdbcTemplate;
    }

    private final JdbcTemplate jdbcTemplate;

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
        calenderTeam.setPromptText("4.1");
        calenderTeam.getItems().addAll("4.1", "4.2", "4.3", "4.4", "4.5", "4.6", "4.7", "4.8", "4.9", "4.10", "4.11", "4.12", "4.13", "4.14", "4.15", "4.16", "4.17", "4.18", "4.19", "4.20", "4.21", "4.22", "4.23", "4.24", "4.25", "4.26");
        calenderPlay.setPromptText("4.1");
        calenderPlay.getItems().addAll("4.1", "4.2", "4.3", "4.4", "4.5", "4.6", "4.7", "4.8", "4.9", "4.10", "4.11", "4.12", "4.13", "4.14", "4.15", "4.16", "4.17", "4.18", "4.19", "4.20", "4.21", "4.22", "4.23", "4.24", "4.25", "4.26");
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
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select\n" +
                "  player.name as player_name,\n" +
                "  team.name   as team_name,\n" +
                "  count(1)    as shot_count\n" +
                "from player\n" +
                "  left join shot_result on player_id = player.id\n" +
                "  left join team on player.team_id = team.id\n" +
                "group by (player_id)\n" +
                "order by shot_count desc");

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
}
