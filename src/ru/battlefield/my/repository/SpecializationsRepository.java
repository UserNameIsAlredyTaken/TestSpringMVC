package ru.battlefield.my.repository;

import org.springframework.data.repository.CrudRepository;
import ru.battlefield.my.domain.Specialization;

import java.util.List;

public interface SpecializationsRepository extends CrudRepository<Specialization,Long>{
    /**
     * Запрос по имени для Specialization
     * @param name искомое имя
     * @return первое Specialization найденное по данному имени
     */
    Specialization findByName(String name);

    /**
     * Запрос по уровню для Specialization
     * @param necessaryLvl уровень для поиска
     * @return все Specialization необходимый уровень открытия которых меньше или равен данному уровню
     */
    List<Specialization> findByNecessaryLvlIsLessThanEqual(int necessaryLvl);
}
