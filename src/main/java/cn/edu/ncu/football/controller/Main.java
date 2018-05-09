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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

        subGroupType.setPromptText("男子组");
        subGroupType.getItems().addAll("男子甲组", "男子乙组", "女子组");
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
