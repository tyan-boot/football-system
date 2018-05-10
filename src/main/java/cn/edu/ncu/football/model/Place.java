package cn.edu.ncu.football.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @OneToOne
    private Judge judge;

    @OneToOne
    private Judge assistJudge;

    @OneToOne
    private Judge assistJudge2;
}
