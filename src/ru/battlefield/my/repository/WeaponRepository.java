package ru.battlefield.my.repository;

import org.springframework.data.repository.CrudRepository;
import ru.battlefield.my.domain.Weapon;

import java.util.List;

public interface WeaponRepository extends CrudRepository<Weapon,Long>{
    /**
     * Запрос по имени для Weapon
     * @param name искомое имя
     * @return первое Weapon найденное по данному имени
     */
    Weapon findByName(String name);
    /**
     * Запрос по минимальному урону для Weapon
     * @param minDamage урон для поиска
     * @return все Weapon минимальный урон которых больше или равен переданному значению урона
     */
    List<Weapon> findAllByMinDamageIsGreaterThanEqual(Double minDamage);
}
