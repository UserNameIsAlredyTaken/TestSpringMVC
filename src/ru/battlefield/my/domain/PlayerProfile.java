package ru.battlefield.my.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
//@EqualsAndHashCode
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

    @JsonIgnore
    @Column(name = "hash_pass")
    private String hashPass;

    @Column(name = "privacy")
    private boolean privacy;

    @Column(name = "wins")
    private int wins;

    @Column(name = "losses")
    private int losses;

    @Column(name = "kills")
    private int kills;

    @Column(name = "deaths")
    private int deaths;

    @Column
    private int lvl;

    @Column(name = "score_for_this_lvl")
    private int scoreForThisLvl;

    @Column(name = "score_for_next_lvl")
    private int scoreForNextLvl;

    @Column(name = "assault_points")
    private int assaultPoints;

    @Column(name = "engineer_points")
    private int engineerPoints;

    @Column(name = "support_points")
    private int supportPoints;

    @Column(name = "recon_points")
    private int reconPoints;

    @Column(name = "total_score")
    private int totalScore;

    @JsonIgnore
    @OneToMany(mappedBy = "playerProfile", cascade = CascadeType.ALL)
    private Set<WeaponKills> weaponKills;

    @Override
    public String toString(){
        return String.format(
                "Player[playerId=%d, nickName='%s', lvl='%d']",
                getId(), getNickName(), getLvl());
    }
}
