package tgbot.moviemoodbot.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "film")
public class Film {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "originalName")
    @NotBlank(message = "can't be empty")
    private String originalName;

    @Column(name = "russianName")
    @NotBlank(message = "can't be empty")
    private String russianName;

    @NotNull(message = "can't be null")
    @Min(value = 1895, message = "can't be less than year of the first film in the world")
    private int year;

    @NotNull(message = "can't be null")
    private int castImageId;

    @NotNull(message = "can't be null")
    @Min(value = 0, message = "can't be less than 0")
    @Max(value = 10, message = "can't be more than 10")
    private float rating;

    @Column(name = "quote")
    @NotBlank(message = "can't be empty")
    private String quote;

    @ManyToMany
    @JoinTable(
            name = "film_country",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "country_id")
    )
    @ToString.Exclude
    private List<Country> countries;

    @ManyToMany
    @JoinTable(
            name = "film_genre",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @ToString.Exclude
    private List<Genre> genres;

    @ManyToMany
    @JoinTable(
            name = "film_user",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @ToString.Exclude
    private List<BotUser> users;
}
