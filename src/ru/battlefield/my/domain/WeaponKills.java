package ru.battlefield.my.domain;

import com.sun.istack.internal.NotNull;
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
//@EqualsAndHashCode
@Table(name = "weapon_kills")
@NoArgsConstructor
@AllArgsConstructor
public class WeaponKills{
    @Id
    @Column(name = "weapon_kills_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull
    @Column(name = "count_of_kills")
    private int countOfKills;

    @ManyToOne(cascade=CascadeType.ALL)
    private PlayerProfile playerProfile;

    @ManyToOne(cascade=CascadeType.ALL)
    private Weapon weapon;
}
