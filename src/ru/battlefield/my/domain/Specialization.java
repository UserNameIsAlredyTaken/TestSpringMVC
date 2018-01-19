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
@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "specializations")
@NoArgsConstructor
@AllArgsConstructor
public class Specialization{

    @Id
    @Column(name = "specialization_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull
    @Column
    private String name;

    @Column(name = "necessary_lvl")
    private int necessaryLvl;

    @Override
    public String toString(){
        return String.format(
                "Specialization[specializationId=%d, name='%s'']",
                getId(), getName());
    }
}
