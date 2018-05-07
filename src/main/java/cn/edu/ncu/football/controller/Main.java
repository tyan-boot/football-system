package cn.edu.ncu.football.controller;

import cn.edu.ncu.football.TestBean;
import cn.edu.ncu.football.model.Person;
import cn.edu.ncu.football.repo.PersonRepo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class Main {
    @FXML
    private Button button;

    private final PersonRepo repo;


    @Autowired
    public Main(TestBean testBean, ConfigurableApplicationContext applicationContext, PersonRepo repo) {
        this.repo = repo;
    }

    @FXML
    public void handlerClick(ActionEvent actionEvent) {
        Iterable<Person> person = repo.findAll();

        person.forEach(p -> {
            System.out.println(p.getId());
        });
    }
}
