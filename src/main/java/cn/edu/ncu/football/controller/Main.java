package cn.edu.ncu.football.controller;

import cn.edu.ncu.football.model.Player;
import cn.edu.ncu.football.model.Team;
import cn.edu.ncu.football.repo.PlayerRepo;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Main {
    @FXML
    public TableColumn<Player, String> teamCol;

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
    private TableView<Player> playerTableView;

    private final PlayerRepo repo;

    @Autowired
    public Main(PlayerRepo repo) {
        this.repo = repo;
    }

    @FXML
    public void initialize() {
        teamCol.setCellValueFactory(cellData -> {
            Player player = cellData.getValue();
            Team team = player.getTeam();

            if (team == null) {
                return null;
            } else {
                return new SimpleStringProperty(team.getName());
            }
        });

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
        calenderTeam.getItems().addAll("4.1","4.2","4.3","4.4","4.5","4.6","4.7","4.8","4.9","4.10","4.11","4.12","4.13","4.14","4.15","4.16","4.17","4.18","4.19","4.20","4.21","4.22","4.23","4.24","4.25","4.26");
        calenderPlay.setPromptText("4.1");
        calenderPlay.getItems().addAll("4.1","4.2","4.3","4.4","4.5","4.6","4.7","4.8","4.9","4.10","4.11","4.12","4.13","4.14","4.15","4.16","4.17","4.18","4.19","4.20","4.21","4.22","4.23","4.24","4.25","4.26");
    }

    @FXML
    public void loadPlayerClick(MouseEvent actionEvent) {
        Iterable<Player> personIter = repo.findAll();

        ObservableList<Player> observableList = FXCollections.observableArrayList();

        personIter.forEach(observableList::add);

        playerTableView.setItems(observableList);
    }

    @FXML
    public void loadTeamClick(MouseEvent event) {
        // todo : 加载队伍信息, 并显示出来
    }
}
