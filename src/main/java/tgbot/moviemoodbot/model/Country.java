package tgbot.moviemoodbot.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "country")
public class Country {

    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToMany(mappedBy = "countries")
    @ToString.Exclude
    private List<Film> films;

}
