package cn.edu.ncu.football.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Data
@ToString(exclude = "team")
@EqualsAndHashCode(exclude = "team")
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String grander;

    @ManyToOne
    private Team team;
}
