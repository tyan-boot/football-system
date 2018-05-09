package cn.edu.ncu.football.model;

import cn.edu.ncu.football.ResultConverter;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class RaceResults {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Convert(converter = ResultConverter.class)
    private RaceResult result;

    @OneToMany(cascade = {CascadeType.MERGE})
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<ShotResult> shotResults = new HashSet<>();

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
