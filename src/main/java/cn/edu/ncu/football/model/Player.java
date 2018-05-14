package cn.edu.ncu.football.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Data
@ToString(exclude = {"team", "shotResults"})
@EqualsAndHashCode(exclude = {"team", "shotResults"})
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String grander;

    @ManyToOne
    private Team team;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private List<ShotResult> shotResults;

}
