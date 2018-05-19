package cn.edu.ncu.football.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString(exclude = "players")
@EqualsAndHashCode(exclude = "players")
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private Integer type;

    @OneToMany(mappedBy = "team")
    private Set<Player> players = new HashSet<>();
}
