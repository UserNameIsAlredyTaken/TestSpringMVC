package ru.battlefield.my.service;

import ru.battlefield.my.domain.Camo;

import java.util.List;

/**
 * Created by danil on 09.01.2018.
 */
public interface CamoService {
    Camo findByName(String name);
    List<Camo> findAllByNecessaryLvlIsLessThanEqual(int necessaryLvl);
    Camo save(Camo camo);
    void delete(Camo camo);
    List<Camo> findAll();
}
