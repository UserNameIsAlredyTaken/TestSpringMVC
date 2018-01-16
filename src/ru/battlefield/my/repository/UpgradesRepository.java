package ru.battlefield.my.repository;

import org.springframework.data.repository.CrudRepository;
import ru.battlefield.my.domain.Upgrade;

import java.util.List;

/**
 * Created by danil on 21.11.2017.
 */
public interface UpgradesRepository extends CrudRepository<Upgrade,Long>{
    /**
     * Запрос по имени для Upgrade
     * @param name искомое имя
     * @return первое Upgrade найденное по данному имени
     */
    Upgrade findByName(String name);
    /**
     * Запрос по убийствам для Upgrade
     * @param killsForUnlock убийства дляпоиска
     * @return все Upgrade количество убийств для открытия которых меньше или равно переданному значению убийств
     */
    List<Upgrade> findAllByKillsForUnlockIsLessThanEqual(int killsForUnlock);
}
