package ru.battlefield.my.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.battlefield.my.domain.Camo;
import ru.battlefield.my.repository.CamosRepository;
import ru.battlefield.my.service.CamoService;

import java.util.List;

/**
 * Created by danil on 09.01.2018.
 */
@Service
@Transactional
public class CamoServiceImpl implements CamoService{
    @Autowired
    CamosRepository camosRepository;

    @Override
    public Camo findByName(String name) {
        return camosRepository.findByName(name);
    }

    @Override
    public List<Camo> findAllByNecessaryLvlIsLessThanEqual(int necessaryLvl) {
        return camosRepository.findAllByNecessaryLvlIsLessThanEqual(necessaryLvl);
    }

    @Override
    public Camo save(Camo camo) {
        return camosRepository.save(camo);
    }

    @Override
    public void delete(Camo camo) {
        camosRepository.delete(camo);
    }

    @Override
    public List<Camo> findAll() {
        return (List<Camo>) camosRepository.findAll();
    }
}
