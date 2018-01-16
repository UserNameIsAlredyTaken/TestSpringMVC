package ru.battlefield.my.repository;

import org.springframework.data.repository.CrudRepository;
import ru.battlefield.my.domain.CommonWeapon;

import java.util.List;

public interface CommonWeaponsRepository extends CrudRepository<CommonWeapon,Long>{
    /**
     * Запрос по уровню для CommonWeapon
     * @param lvlForUnlock уровень для поиска
     * @return все CommonWeapon уровень для открытия которых меньше или равен переданному значению уровня
     */
    List<CommonWeapon> findAllByLvlForUnlockIsLessThanEqual(int lvlForUnlock);
}
