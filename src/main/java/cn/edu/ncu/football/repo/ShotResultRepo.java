package cn.edu.ncu.football.repo;

import cn.edu.ncu.football.model.ShotResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShotResultRepo extends CrudRepository<ShotResult, Integer> {
}
