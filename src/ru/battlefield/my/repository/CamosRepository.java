package ru.battlefield.my.repository;

import org.springframework.data.repository.CrudRepository;
import ru.battlefield.my.domain.Camo;

import java.util.List;

/**
 * Created by danil on 21.11.2017.
 */
public interface CamosRepository extends CrudRepository<Camo,Long>{
    /**
     * Запрос по имени для Camo
     * @param name искомое имя
     * @return первое Camo найденное по данному имени
     */
    Camo findByName(String name);
    /**
     * Запрос по необходимому уровню для Camo
     * @param necessaryLvl уровень для поиска
     * @return все Camo уровень для открытия которых равен переданному значению уровня
     */
    List<Camo> findAllByNecessaryLvlIsLessThanEqual(int necessaryLvl);
}
