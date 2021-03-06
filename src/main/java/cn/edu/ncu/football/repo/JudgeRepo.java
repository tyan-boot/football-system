package cn.edu.ncu.football.repo;

import cn.edu.ncu.football.model.Judge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JudgeRepo extends CrudRepository<Judge, Integer> {
}
