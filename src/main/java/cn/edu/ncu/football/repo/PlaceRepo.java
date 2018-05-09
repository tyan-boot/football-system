package cn.edu.ncu.football.repo;

import cn.edu.ncu.football.model.Place;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepo extends CrudRepository<Place, Integer> {
}
