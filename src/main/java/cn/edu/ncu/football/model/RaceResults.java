package cn.edu.ncu.football.model;

import cn.edu.ncu.football.ResultConverter;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Data
public class RaceResults {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    private Team team;

    @Convert(converter = ResultConverter.class)
    private RaceResult result;

    @OneToOne
    private Race race;

    private Time time;

    public static enum RaceResult {
        WIN(1, "胜"),
        LOSE(2, "输"),
        DRAW(3, "平");

        @Getter
        private Integer value;

        @Getter
        private String description;

        RaceResult(Integer value, String description) {
            this.value = value;
            this.description = description;
        }
    }
}
