package ru.battlefield.my.repository;

import org.springframework.data.repository.CrudRepository;
import ru.battlefield.my.domain.ClassWeapon;

import java.util.List;

public interface ClassWeaponsRepository extends CrudRepository<ClassWeapon,Long>{

}
