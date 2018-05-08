package cn.edu.ncu.football.controller;

import cn.edu.ncu.football.model.Player;
import cn.edu.ncu.football.model.Team;
import cn.edu.ncu.football.repo.PlayerRepo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
