package ru.battlefield.my.repository;

import org.springframework.data.repository.CrudRepository;
import ru.battlefield.my.domain.Weapon;

import java.util.ArrayList;
import java.util.List;

public interface WeaponRepository extends CrudRepository<Weapon,Long>{
    /**
     * Запрос по имени для Weapon
     * @param name искомое имя
     * @return первое Weapon найденное по данному имени
     */
    Weapon findByName(String name);

    Weapon findById(long id);
}
