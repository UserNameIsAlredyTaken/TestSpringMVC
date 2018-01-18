package ru.battlefield.my.repository;

import org.springframework.data.repository.CrudRepository;
import ru.battlefield.my.domain.PlayerProfile;

import java.util.List;

/**
 * Created by danil on 21.11.2017.
 */
public interface PlayerProfilesRepository extends CrudRepository<PlayerProfile,Long>{
    /**
     * Запрос по имени для PlayerProfile
     * @param nickName искомый никнейм
     * @return первое PlayerProfile найденное по данному имени
     */
    PlayerProfile findByNickName(String nickName);

    /**
     * Запрос по уровню для Upgrade
     * @param lvl уровень для поиска
     * @return все PlayerProfile уровень которых равен переданному уровню
     */
    List<PlayerProfile> findAllByLvl(int lvl);
}
