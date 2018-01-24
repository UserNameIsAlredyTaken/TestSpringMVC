package ru.battlefield.my.repository;

import org.springframework.data.repository.CrudRepository;
import ru.battlefield.my.domain.PlayerProfile;
import ru.battlefield.my.domain.Weapon;
import ru.battlefield.my.domain.WeaponKills;

/**
 * Created by danil on 21.01.2018.
 */
public interface WeaponKillsRepository extends CrudRepository<WeaponKills,Long> {
    WeaponKills findByWeaponAndPlayerProfile(Weapon weapon, PlayerProfile playerProfile);

    WeaponKills findTopByPlayerProfileOrderByCountOfKillsDesc(PlayerProfile playerProfile);
}
