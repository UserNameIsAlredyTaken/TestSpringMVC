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
@Table(name = "camos")
@NoArgsConstructor
@AllArgsConstructor
public class Camo{

    @Id
    @Column(name = "camo_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull
    @Column
    private String name;

    @Column(name = "stealth_degree")
    private double stealthDegree;

    @Column(name = "necessary_lvl")
    private int necessaryLvl;

    @ManyToMany(mappedBy = "camos", cascade = CascadeType.ALL)
    private Set<PlayerProfile> playerProfiles;

    @Override
    public String toString(){
        return String.format(
                "Camo[camoId=%d, name='%s']",
                getId(), getName());
    }
}
