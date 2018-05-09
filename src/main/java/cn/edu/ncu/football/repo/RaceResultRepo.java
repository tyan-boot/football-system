package cn.edu.ncu.football.repo;

import cn.edu.ncu.football.model.RaceResults;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RaceResultRepo extends CrudRepository<RaceResults, Integer> {
}
