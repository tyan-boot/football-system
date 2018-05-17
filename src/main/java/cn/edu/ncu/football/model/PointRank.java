package cn.edu.ncu.football.model;

import lombok.Data;

@Data
public class PointRank {
    private Integer rank;

    private Integer diff;

    private String teamName;

    private Integer race;

    private Integer win;

    private Integer draw;

    private Integer lose;

    private Integer inCount;

    private Integer lostCount;

    private Integer prueInCount;

    private Double avgInCount;

    private Double avgLostCount;

    private Double avgPrueWin;

    private Double avgPoint;

    private Integer point;
}
