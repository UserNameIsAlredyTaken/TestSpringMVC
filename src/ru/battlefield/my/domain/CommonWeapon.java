package ru.battlefield.my.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import javax.persistence.*;

/**
 * Created by danil on 21.11.2017.
 */
@Slf4j
@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(name = "common_weapons")
@NoArgsConstructor
@AllArgsConstructor
public class CommonWeapon{

    @Id
    @Column(name = "weapon_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "is_general")
    private boolean isGeneral;

    @OneToOne
    private Weapon weapon;


    @Override
    public String toString(){
        return String.format(
                "CommonW[weaponId=%d, lvlForUnlock='%d']",
                getId());
    }
}
