package cn.edu.ncu.football.repo;

import cn.edu.ncu.football.model.Player;
import cn.edu.ncu.football.model.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepo extends CrudRepository<Player, Integer> {
    List<Player> findByTeam(Team team);
}
