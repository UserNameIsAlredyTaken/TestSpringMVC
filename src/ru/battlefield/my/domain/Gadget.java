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
@EqualsAndHashCode
@Entity
@Table(name = "gadgets")
@NoArgsConstructor
@AllArgsConstructor
public class Gadget{

    @Id
    @Column(name = "gadget_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull
    @Column
    private String name;

    @Column(name = "class")
    private String clazz;

    @Column(name = "necessary_point")
    private int necessaryPoint;

    @ManyToMany(mappedBy = "gadgets", cascade = CascadeType.ALL)
    private Set<PlayerProfile> playerProfiles;

    @Override
    public String toString(){
        return String.format(
                "Gadget[gadgetId=%d, name='%s'']",
                getId(), getName());
    }
}
