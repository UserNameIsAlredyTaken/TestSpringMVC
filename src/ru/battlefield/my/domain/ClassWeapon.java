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
@Table(name = "class_weapons")
@NoArgsConstructor
@AllArgsConstructor
public class ClassWeapon{

    @Id
    @Column(name = "weapon_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "class")
    private String clazz;

    @Column(name = "points_for_unlock")
    private Integer pointsForUnlock;

    @OneToOne
    private Weapon weapon;

    @Override
    public String toString(){
        return String.format(
                "ClassW[weaponId=%d, lvlForUnlock='%d']",
                getId(), getPointsForUnlock());
    }
}
