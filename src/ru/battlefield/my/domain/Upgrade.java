package ru.battlefield.my.domain;

import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Table(name = "upgrades")
@NoArgsConstructor
@AllArgsConstructor
public class Upgrade{

    @Id
    @Column(name = "upgrade_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull
    @Column
    private String name;

    @Column
    private String type;

    @Column(name = "kills_for_unlock")
    private int killsForUnlock;

    @ManyToMany(mappedBy = "upgrades", cascade = CascadeType.ALL)
    private Set<Weapon> weapons;

    @Override
    public String toString(){
        return String.format(
                "Upgrades[upgradeId=%d, name='%s']",
                getId(), getName());
    }
}
