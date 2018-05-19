package cn.edu.ncu.football.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@ToString(exclude = "raceResult")
@EqualsAndHashCode(exclude = "raceResult")
public class ShotResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    private Player player;

    private Integer shotCount;

    @ManyToOne
    private RaceResults raceResult;
}
