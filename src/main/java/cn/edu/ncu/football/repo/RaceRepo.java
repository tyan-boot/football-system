package cn.edu.ncu.football.repo;

import cn.edu.ncu.football.model.Race;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RaceRepo extends CrudRepository<Integer, Race> {
}
