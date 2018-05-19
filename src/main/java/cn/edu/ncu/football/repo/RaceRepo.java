package cn.edu.ncu.football.repo;

import cn.edu.ncu.football.model.Race;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RaceRepo extends CrudRepository<Race, Integer> {
    List<Race> findByTeam1IdAndHoldDate(Integer id, Date date);

    List<Race> findByTeam2IdAndHoldDate(Integer id, Date date);
}
