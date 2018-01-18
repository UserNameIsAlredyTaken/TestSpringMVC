package ru.battlefield.my.repository;

import org.springframework.data.repository.CrudRepository;
import ru.battlefield.my.domain.CommonWeapon;

import java.util.List;

public interface CommonWeaponsRepository extends CrudRepository<CommonWeapon,Long>{

}
