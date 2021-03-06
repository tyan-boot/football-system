package cn.edu.ncu.football.model;

import cn.edu.ncu.football.StatusConverter;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
public class Race {
    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne
    private Team team1;

    @OneToOne
    private Team team2;

    private Date holdDate;

    @OneToOne(cascade = {CascadeType.MERGE})
    private RaceResults raceResults;

    @Convert(converter = StatusConverter.class)
    private RaceStatus status;

    @OneToOne
    private Place place;

    public static enum RaceStatus {
        PENDING(1, "未举行"),
        PROCESSING(2, "正在进行"),
        END(3, "已结束");

        @Getter
        private Integer value;

        @Getter
        private String description;

        RaceStatus(Integer value, String description) {
            this.value = value;
            this.description = description;
        }
    }
}
