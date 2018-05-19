package cn.edu.ncu.football.model;

import lombok.Data;

@Data
public class ShotRank {
    private Integer rank;

    private String playerName;

    private String teamName;

    private Integer shotCount;
}
