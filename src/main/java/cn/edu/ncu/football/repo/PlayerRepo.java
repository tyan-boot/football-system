package cn.edu.ncu.football.repo;

import cn.edu.ncu.football.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepo extends CrudRepository<Player, Integer> {
}
