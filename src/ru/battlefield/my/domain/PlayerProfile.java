package ru.battlefield.my.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by danil on 21.11.2017.
 */
@Slf4j
@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "player_profiles")
@NoArgsConstructor
@AllArgsConstructor
public class PlayerProfile implements Serializable {

    @Id
    @Column(name = "player_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "nick_name")
    private String nickName;

    @Column
    private int lvl;

    @Column(name = "assault_points")
    private int assaultPoints;

    @Column(name = "engineer_points")
    private int engineerPoints;

    @Column(name = "support_points")
    private int supportPoints;

    @Column(name = "recon_points")
    private int reconPoints;

    @ManyToMany
    @JoinTable
    private Set<Camo> camos;

    @ManyToMany
    @JoinTable
    private Set<Specialization> specializations;

    @ManyToMany
    @JoinTable
    private Set<Gadget> gadgets;

    @OneToMany(mappedBy = "playerProfile", cascade = CascadeType.ALL)
    private Set<WeaponKills> weaponKills;

    @Override
    public String toString(){
        return String.format(
                "Player[playerId=%d, nickName='%s', lvl='%d']",
                getId(), getNickName(), getLvl());
    }
}
