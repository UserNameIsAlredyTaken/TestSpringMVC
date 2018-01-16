package ru.battlefield.my.repository;

import org.springframework.data.repository.CrudRepository;
import ru.battlefield.my.domain.Gadget;

import java.util.List;

public interface GadgetsRepository extends CrudRepository<Gadget,Long>{
    /**
     * Запрос по имени для Gadget
     * @param name искомое имя
     * @return первое Gadget найденное по данному имени
     */
    Gadget findByName(String name);
    /**
     * Запрос по уровню для Gadget
     * @param necessaryLvl уровень для поиска
     * @return все Gadget необходимый уровень для открытия которых меньше или равен переданному значению уровня
     */
    List<Gadget> findAllByNecessaryPointIsLessThanEqual(int necessaryLvl);
}
