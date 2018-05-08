package cn.edu.ncu.football.repo;

import cn.edu.ncu.football.model.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepo extends CrudRepository<Team, Integer> {
}
