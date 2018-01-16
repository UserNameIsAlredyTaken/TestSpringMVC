package ru.battlefield.my.domain;

import com.sun.istack.internal.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import javax.persistence.*;
import java.util.Set;

/**
 * Created by danil on 21.11.2017.
 */
@Slf4j
@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(name = "weapon")
@NoArgsConstructor
@AllArgsConstructor
public class Weapon {

    @Id
    @Column(name = "weapon_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "type_of_weapon")
    private String typeOfWeapon;

    @NotNull
    @Column
    private String name;

    @Column
    private int rpm;

    @Column(name = "max_damage")
    private Double maxDamage;
    @Column(name = "min_damage")
    private Double minDamage;

    @Column
    private String range;

    @Column
    private int ammunition;

    @Column
    private double reload;

    @OneToMany(mappedBy = "weapon", cascade = CascadeType.ALL)
    private Set<WeaponKills> weaponKills;

    @ManyToMany
    @JoinTable
    private Set<Upgrade> upgrades;

    @OneToOne(mappedBy = "weapon", cascade = CascadeType.ALL)
    private CommonWeapon commonWeapon;

    @OneToOne(mappedBy = "weapon", cascade = CascadeType.ALL)
    private ClassWeapon classWeapon;

    @Override
    public String toString(){
        return String.format(
                "Weapon[weaponId=%d, name='%s']",
                getId(), getName());
    }
}
