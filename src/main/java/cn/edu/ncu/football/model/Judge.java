package cn.edu.ncu.football.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 主裁判
 */
@Data
@Entity
public class Judge {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String grander;//性别

    @OneToOne
    private Team team;
}
