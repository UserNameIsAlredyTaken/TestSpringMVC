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

    @NotNull
    @Column
    private String name;

    @Column
    private int rpm;

    @Column
    private String range;

    @Column
    private String ammunition;

    @Column
    private String category;

    @Column
    private String type;

    @OneToMany(mappedBy = "weapon", cascade = CascadeType.ALL)
    private Set<WeaponKills> weaponKills;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable
    private Set<Upgrade> upgrades;

    @Override
    public String toString(){
        return String.format(
                "Weapon[weaponId=%d, name='%s']",
                getId(), getName());
    }
}
