package ru.battlefield.my.repository;

import org.springframework.data.repository.CrudRepository;
import ru.battlefield.my.domain.ClassWeapon;

import java.util.List;

public interface ClassWeaponsRepository extends CrudRepository<ClassWeapon,Long>{
    /**
     * Запрос по очкам для ClassWeapon
     * @param pointsForUnlock очки для поиска
     * @return все ClassWeapon количество очков для открытия которых меньше или равен переданному значению очков
     */
    List<ClassWeapon> findAllByPointsForUnlockIsLessThanEqual(int pointsForUnlock);
}
