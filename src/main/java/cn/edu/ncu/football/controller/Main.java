package cn.edu.ncu.football.controller;

import cn.edu.ncu.football.model.Player;
import cn.edu.ncu.football.repo.PlayerRepo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Main {
    @FXML
    private Button button;

    private final PlayerRepo repo;


    @Autowired
    public Main(PlayerRepo repo) {
        this.repo = repo;
    }

    @FXML
    public void handlerClick(ActionEvent actionEvent) {
        Iterable<Player> person = repo.findAll();

        person.forEach(p -> System.out.println(p.getId()));
    }
}
