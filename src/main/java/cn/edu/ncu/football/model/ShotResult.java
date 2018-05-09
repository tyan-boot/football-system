package cn.edu.ncu.football.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ShotResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    private Player player;

    private Integer shotCount;
}
